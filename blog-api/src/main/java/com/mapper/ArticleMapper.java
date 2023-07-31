package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dos.Archives;
import com.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();

    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);

    Long findAuthorId(Long id);

    IPage<Article> myListArticle(Page<Article> page, Long categoryId, Long tagId, String year, String month, Long userId,String queryString);


    IPage<Article> articlePush(Page<Article> page, Long categoryId, Long tagId, String year, String month, List<Long> followUserIds,String queryString);

    // IPage<Article> findMyListArticle(Page<Article> page, String queryString,Long userId);

    // IPage<Article> FindPushArticle(Page<Article> page, List<Long> followUserIds,String queryString);
}
