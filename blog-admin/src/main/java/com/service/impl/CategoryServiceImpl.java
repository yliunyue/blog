package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.entity.Article;
import com.entity.Category;
import com.entity.Tag;
import com.mapper.ArticleMapper;
import com.mapper.CategoryMapper;
import com.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-27
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleMapper articleMapper;
    /*
    * 查询分类列表
    * */
    @Override
    public Result categoryList(String categoryName) {
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        if (!"null".equals(categoryName)) {
            queryWrapper.like(Category::getCategoryName,categoryName);
        }
        List<Category> categories = categoryMapper.selectList(queryWrapper);

        return Result.success(categories);
    }

    /*
    * 添加类别
    * */
    @Override
    public Result addCategory(Category category) {
        categoryMapper.insert(category);
        return Result.success("添加类别成功");
    }

    /*
    * 修改
    * */
    @Override
    public Result updataCategory(Category category) {
        //修改标签
        LambdaUpdateWrapper<Category> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(Category::getId,category.getId())
                .set(Category::getAvatar,category.getAvatar())
                .set(Category::getCategoryName,category.getCategoryName())
                .set(Category::getDescription,category.getDescription());
        categoryMapper.update(null,updateWrapper);
        return Result.success("修改成功");
    }

    /*
    * 删除类别，文章包含了改类别的需要将文章类别id置为空
    * */
    @Override
    @Transactional
    public Result deleteCategory(Long id) {
        //根据id查询出该类别下包的文章
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCategoryId,id);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        //将这些文章的所有类别id设置为null
        if (articles.size() != 0) {
            articleMapper.setCategoryNull(articles);
        }
        //根据id删除类别
        categoryMapper.deleteById(id);
        return Result.success("删除成功");
    }
    /*
    * 根据类别名字查询出所有的类别id
    * */

    @Override
    public List<Long> selectBycategoryName(String queryString) {
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(Category::getCategoryName,queryString);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        List<Long> ids=new ArrayList<>();
        for (Category category : categories) {
            ids.add(category.getId());
        }
        return ids;
    }
}
