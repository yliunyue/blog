package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Comment;
import com.mapper.CommentMapper;
import com.service.ArticleService;
import com.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.service.SysUserService;
import com.vo.CommentVo;
import com.vo.CommentVo2;
import com.vo.Result;
import com.vo.UserVo;
import com.vo.params.*;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-27
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleService articleService;
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
        List<CommentVo> commentVos = comments.stream().map(comment -> {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);
            commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
            //根据author_id查询出是谁写的评论
            UserVo userVo=sysUserService.findByAuthorId(comment.getAuthorId());
            commentVo.setAuthor(userVo);
            //查询出其子评论
            Integer level = comment.getLevel();
            if (1 == level) {
                Long id1 = comment.getId();
                List<CommentVo> commentVoList = findCommentByParentId(id1);
                commentVo.setChildrens(commentVoList);
            }
            return commentVo;
        }).collect(Collectors.toList());
        //2.根据作者id查询作者信息
        //3.判断 如果 level=1 要去查询有没有自评论
        //4.有,根据评论id查找(parent_id)
        // return Result.success(commentVoList);
        return Result.success(commentVos);
    }
    /*
     * 查找子评论信息
     * */
    private List<CommentVo> findCommentByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id).eq(Comment::getLevel, 2);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVos = comments.stream().map(comment -> {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);
            commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
            //根据author_id查询出是谁写的评论
            UserVo userVo=sysUserService.findByAuthorId(comment.getAuthorId());
            commentVo.setAuthor(userVo);

            Integer level = comment.getLevel();
            //to user 给谁评论
            if (level > 1) {
                Long toUid = comment.getToUid();
                UserVo toUserVo = sysUserService.findByAuthorId(toUid);
                commentVo.setToUser(toUserVo);
            }
            return commentVo;
        }).collect(Collectors.toList());
        return commentVos;
    }

    /*
    * 根据评论id删除id
    * */
    @Override
    @Transactional
    public Result deleteComment(Long id) {
        //先判断改评论是属于第几级别的评论
        Comment comment = commentMapper.selectById(id);
        Integer level = comment.getLevel();
        if (level == 1) {
            //如果是第一级别的评论，需要将其子评论全部删除，也就是评论中parent_id是id的全部删除
            //先删除子评论
            LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Comment::getParentId,id);
            commentMapper.delete(queryWrapper);
        }
        //再删除该评论
        //同样，如果级别是第二级别，直接删除
        commentMapper.deleteById(id);
        return Result.success("删除成功");
    }

    /*
    * 评论列表
    * */
    @Override
    public Result pageComment(PageParam pageParam) {
        IPage<Comment> page=new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        //getQueryString不为空，条件查询
        if (pageParam.getQueryString()!=null){
            //按照内容查找
            queryWrapper.like(Comment::getContent,pageParam.getQueryString());
        }
        IPage<Comment> pageInfo = commentMapper.selectPage(page, queryWrapper);
        //返回页面数据
        PageResult<CommentVo2> commentVo2PageResult = new PageResult<>();
        commentVo2PageResult.setTotal(pageInfo.getTotal());
        //处理数据
        List<Comment> records = pageInfo.getRecords();
        List<CommentVo2> commentVo2List = records.stream().map(new Function<Comment, CommentVo2>() {
            @Override
            public CommentVo2 apply(Comment comment) {
                CommentVo2 commentVo2 = new CommentVo2();
                BeanUtils.copyProperties(comment,commentVo2);

                commentVo2.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
                //根据author_id查询出是谁写的评论
                UserVo userVo=sysUserService.findByAuthorId(comment.getAuthorId());
                commentVo2.setAuthor(userVo);

                Integer level = comment.getLevel();
                //to user 给谁评论
                if (level > 1) {
                    Long toUid = comment.getToUid();
                    UserVo toUserVo = sysUserService.findByAuthorId(toUid);
                    commentVo2.setToUser(toUserVo);
                }
                //文章标题
                String title=articleService.findTitleById(commentVo2.getArticleId());
                commentVo2.setTitle(title);
                return commentVo2;
            }
        }).collect(Collectors.toList());
        commentVo2PageResult.setList(commentVo2List);
        return Result.success(commentVo2PageResult);
    }
}
