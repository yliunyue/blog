package com.service;

import com.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-27
 */
public interface CategoryService extends IService<Category> {

    /*
    * 查询分类列表（可根据分类名字模糊查询）
    * */
    Result categoryList(String categoryName);

    /*
    * 添加类别
    * */
    Result addCategory(Category category);

    /*
    * 修改
    * */
    Result updataCategory(Category category);

    /*
    * 删除
    * */
    Result deleteCategory(Long id);

    /*
    * 根据类别名字查询出所有的类别id
    * */
    List<Long> selectBycategoryName(String queryString);
}
