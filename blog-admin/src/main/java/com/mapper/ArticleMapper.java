package com.mapper;

import com.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dragon
 * @since 2023-01-27
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    void setCategoryNull(List<Article> articles);

    String findTitleById(Long articleId);
}
