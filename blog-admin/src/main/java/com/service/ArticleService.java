package com.service;

import com.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.params.PageParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-27
 */
public interface ArticleService extends IService<Article> {

    /*
    * 文章列表
    * */
    Result articleList(PageParam pageParam);

    /*
    * 删除
    * */
    Result deleteArticle(Long id);

    /*
    * 更具id找标题
    * */
    String findTitleById(Long articleId);
}
