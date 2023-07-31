package com.service;

import com.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.params.CategoryVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-08
 */
public interface CategoryService extends IService<Category> {
    /*
     * 根据id查询类别
     * */
    CategoryVo findCategoryById(Long categoryId);

    /*
    * 查询所有文章类别
    * */
    Result findAll();

    /*
    * 查询所有文章分类细节
    * */
    Result findAllDetail();

    /*
    * 根据类别id查询所有文章
    * */
    Result categoryDetailById(Long id);
}
