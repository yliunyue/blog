package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pojo.Article;
import com.vo.Result;
import com.vo.params.ArticleParam;
import com.vo.params.PageParams;
import com.vo.params.PageSearchParam;
import com.vo.params.SearchParam;


public interface ArticleService extends IService<Article> {

    /*
    * 分页查询文章列表
    * */
    Result listArticle(PageParams pageParams);

    /*
    * 首页最热文章显示
    * */
    Result hotArticle(int limit);

    /*
    * 首页最新文章
    * */
    Result newArticles(int limit);

    /*
    * 文章归档
    * */
    Result listArchives();

    /*
    * 查看文章详情
    * */
    Result findByArticleId(Long articleId);

    /*
    * 提交文章
    * */
    Result publish(ArticleParam articleParam);

    /*
    * 根据文章id查询作者id
    * */
    Long findAuthorId(Long id);

    /*
    * 我的文章
    * */
    Result myListArticle(PageParams pageParams);

    /*
    * 文章推送
    * */
    Result articlePush(PageParams pageParams);

    /*
    * 查找文章
    * */
    Result search(SearchParam searchParam);

    /*
    * 删除
    * */
    Result deleteArticleById(Long id);

    // Result findMyListArticle(PageSearchParam pageSearchParam);

    // Result FindPushArticle(PageSearchParam pageSearchParam);
}
