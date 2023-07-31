package com.service;

import com.pojo.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.params.TagVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-06
 */
public interface TagService extends IService<Tag> {
    //根据文章id查找标签
    List<TagVo> findTagByArticledId(Long articleId);

    //最热标签
    Result hotTag(int limit);

    /*
    * 查询所有标签
    * */
    Result findAll();

    /*
     * 查询所有标签 详情
     * */
    Result findAllDetail();

    /*
    * 根据标签id查询标签
    * */
    Result findDetailById(Long id);
}
