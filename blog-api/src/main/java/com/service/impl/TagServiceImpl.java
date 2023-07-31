package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pojo.Tag;
import com.mapper.TagMapper;
import com.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vo.Result;
import com.vo.params.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-06
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Override
    public List<TagVo> findTagByArticledId(Long articleId) {
        List<TagVo> tagByArticledId = tagMapper.findTagByArticledId(articleId);
        return tagByArticledId;
    }

    /*
    * 最热标签
    * */
    @Override
    public Result hotTag(int limit) {
        /*
        * 什么是最热标签：
        * 标签拥有的文章最多就是最热标签--->根据tag_id分组查询拥有的文章数量，再排序取前limit个。
        * */
        //查询热标签的id
        List<Long> tagIds=tagMapper.findHotTagIds(limit);
        if (CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        //根据上面返回的ids查询热标签的名字
        LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getTagName,Tag::getId).in(Tag::getId,tagIds);
        List<Tag> tags = tagMapper.selectList(queryWrapper);
        TagVo hotTags = new TagVo();
        BeanUtils.copyProperties(tags,hotTags);
        return Result.success(tags);
    }
    // @Override
    // public List<TagVo> findTagByArticledId(Long articleId) {
    //     List<Tag> tagByArticledId = tagMapper.findTagByArticledId(articleId);
    //     return copyList(tagByArticledId);
    // }
    //
    // public TagVo copy(Tag tag){
    //     TagVo tagVo = new TagVo();
    //     BeanUtils.copyProperties(tag,tagVo);
    //     return tagVo;
    // }
    // public List<TagVo> copyList(List<Tag> tagList){
    //     List<TagVo> tagVoList=new ArrayList<>();
    //     for (Tag tag : tagList) {
    //         tagVoList.add(copy(tag));
    //     }
    //     return tagVoList;
    // }

    /*
    * 查询所有标签
    * */
    @Override
    public Result findAll() {
        List<Tag> tags = tagMapper.selectList(new LambdaUpdateWrapper<>());
        List<TagVo> tagVoList = tags.stream().map(new Function<Tag, TagVo>() {
            @Override
            public TagVo apply(Tag tag) {
                TagVo tagVo = new TagVo();
                BeanUtils.copyProperties(tag, tagVo);
                return tagVo;
            }
        }).collect(Collectors.toList());
        return Result.success(tagVoList);
    }
    /*
     * 查询所有标签 详情
     * */
    @Override
    public Result findAllDetail() {
        List<Tag> tags = tagMapper.selectList(new LambdaUpdateWrapper<>());
        return Result.success(tags);
    }

    /*
    * 根据标签id查询标签
    * */

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(tag);
    }
}

