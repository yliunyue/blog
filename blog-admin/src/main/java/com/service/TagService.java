package com.service;

import com.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.TagVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-20
 */
public interface TagService extends IService<Tag> {

    /*
    * 标签列表
    * */
    Result tagList(String tagName);

    /*
    * 添加标签
    * */
    Result addTag(Tag tag);

    /*
    * 修改标签
    * */
    Result uploadTag(Tag tag);

    Result deleteTag(Long id);

    /*
    * 根据标签名字查询出所包含的文章id
    * */
    List<Long> selectByTagName(String queryString);

    /*
    * 根据文章id查询标签
    * */
    List<TagVo> selectByArticleId(Long id);
}
