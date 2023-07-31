package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.entity.ArticleTag;
import com.entity.Tag;
import com.mapper.ArticleTagMapper;
import com.mapper.TagMapper;
import com.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vo.Result;
import com.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-20
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    /*
     * 标签列表
     * */
    @Override
    public Result tagList(String tagName) {
        //如果tagName不为空，则根据tagname模糊查询
        LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
        if (tagName!=null){
            queryWrapper.like(Tag::getTagName,tagName);
        }
        List<Tag> tags = tagMapper.selectList(queryWrapper);
        return Result.success(tags);
    }

    /*
    * 添加标签
    * */
    @Override
    public Result addTag(Tag tag) {
        tagMapper.insert(tag);
        //图片得上传到天牛云服务器中，
        return Result.success(null);
    }

    /*
    * 修改标签
    * */
    @Override
    public Result uploadTag(Tag tag) {
        //修改标签
        LambdaUpdateWrapper<Tag> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(Tag::getId,tag.getId())
                .set(Tag::getAvatar,tag.getAvatar())
                .set(Tag::getTagName,tag.getTagName());
        tagMapper.update(null,updateWrapper);
        return Result.success("修改标签成功");
    }

    /*
    * 删除
    * */
    @Override
    @Transactional
    public Result deleteTag(Long id) {
        //删除标签，需要将ms_article_tag表中的关联数据全部删除
        LambdaQueryWrapper<ArticleTag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getTagId,id);
        articleTagMapper.delete(queryWrapper);
        //删除成功，在删除ms_tag表中的数据
        tagMapper.deleteById(id);
        return Result.success("标签删除成功");
    }
    /*
    * 根据标签名字查询出所包含的文章id
    * */
    @Override
    public List<Long> selectByTagName(String queryString) {
        List<Long> articleIds=new ArrayList<>();
        //根据标签名字模糊查询。可能会返回多个标签id
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper=new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.like(Tag::getTagName,queryString);
        List<Tag> tags = tagMapper.selectList(tagLambdaQueryWrapper);
        List<Long> tagIds=new ArrayList<>();
        for (Tag tag : tags) {
            tagIds.add(tag.getId());
        }
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper=new LambdaQueryWrapper<>();
        if (tagIds.size()==0){
            return articleIds;
        }
        articleTagLambdaQueryWrapper.in(ArticleTag::getTagId,tagIds);
        List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);

        for (ArticleTag articleTag : articleTags) {
            articleIds.add(articleTag.getArticleId());
        }
        return articleIds;
    }

    /*
    * 根据文章id查询标签
    * */
    @Override
    public List<TagVo> selectByArticleId(Long id) {
        LambdaQueryWrapper<ArticleTag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagMapper.selectList(queryWrapper);
        List<Long> tagIds=new ArrayList<>();
        for (ArticleTag articleTag : articleTags) {
            tagIds.add(articleTag.getTagId());
        }
        //根据tagids查询标签
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper=new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.in(Tag::getId,tagIds);
        List<Tag> tags = tagMapper.selectList(tagLambdaQueryWrapper);
        List<TagVo> tagVos=new ArrayList<>();
        for (Tag tag : tags) {
            TagVo tagVo = new TagVo();
            BeanUtils.copyProperties(tag,tagVo);
            tagVos.add(tagVo);
        }
        return tagVos;
    }
}
