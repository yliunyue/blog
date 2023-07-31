package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.handler.WordFilter;
import com.mapper.ArticleMapper;
import com.pojo.Article;
import com.pojo.Comment;
import com.mapper.CommentMapper;
import com.pojo.SysUser;
import com.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.service.SysUserService;
import com.utils.UserThreadLocal;
import com.vo.Result;
import com.vo.params.CommentParam;
import com.vo.params.CommentVo;
import com.vo.params.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-08
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleMapper articleMapper;
    /*
     * 根据文章id,查询所有的评论列表
     * */
    @Override
    public Result commentByArticleId(Long id) {
        //1.根据文章id查询 评论列表 从comment表中查询
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        //只要查询lever等于1的
        queryWrapper.eq(Comment::getArticleId, id).eq(Comment::getLevel, 1).orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        //2.根据作者id查询作者信息
        //3.判断 如果 level=1 要去查询有没有自评论
        //4.有,根据评论id查找(parent_id)

        //查询所有评论数
        Integer integer = CommentCount(id);
        LambdaUpdateWrapper<Article> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,id).set(Article::getCommentCounts,integer);
        int update = articleMapper.update(null,updateWrapper);
        return Result.success(commentVoList);
    }
    //查找评论数
    public Integer CommentCount(Long articleId){
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,articleId);
        Integer commentCount = commentMapper.selectCount(queryWrapper);
        return commentCount;
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //子评论
        Integer level = comment.getLevel();
        if (1 == level) {
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        //to user 给谁评论
        if (level > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    /*
     * 查找父评论信息
     * */
    private List<CommentVo> findCommentByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id).eq(Comment::getLevel, 2);
        return copyList(commentMapper.selectList(queryWrapper));
    }

    /*
    * 评论
    * */
    @Override
    public Result comments(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        String content = commentParam.getContent();
        //调用wordfilter将敏感字过滤。
        String newContent = WordFilter.replaceWords(content);
        comment.setContent(newContent);
        comment.setCreateDate(System.currentTimeMillis());
        //父评论id
        Long parent = commentParam.getParent();
        if (parent==null||parent==0){
            comment.setLevel(1);
        }else {
            comment.setLevel(2);
        }
        comment.setParentId(parent==null?0:parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId==null?0:toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }
}
