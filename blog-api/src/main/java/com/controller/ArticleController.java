package com.controller;

import com.common.aop.LogAnnotation;
import com.common.cache.Cache;
import com.service.ArticleService;
import com.vo.Result;
import com.vo.params.ArticleParam;
import com.vo.params.PageParams;
import com.vo.params.PageSearchParam;
import com.vo.params.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /*
     * 首页文章列表
     * */
    @PostMapping
    @LogAnnotation(module="文章",operation="获取文章列表")
    // @Cache(expire = 5*60*1000,name = "listArticle")
    public Result listArticle(@RequestBody PageParams pageParams) {
        Result result = articleService.listArticle(pageParams);
        return result;
    }
    /*
     * 我的文章
     * */
    @PostMapping("/myListArticle")
    @LogAnnotation(module="文章",operation="我的文章")
    // @Cache(expire = 5*60*1000,name = "listArticle")
    public Result myListArticle(@RequestBody PageParams pageParams) {
        Result result = articleService.myListArticle(pageParams);
        return result;
    }

    // @PostMapping("/FindMyListArticle")
    // public Result findMyListArticle(@RequestBody PageSearchParam pageSearchParam){
    //     Result result=articleService.findMyListArticle(pageSearchParam);
    //     return result;
    // }
    /*
     * 文章推送（关注的人写的文章）
     * */
    @PostMapping("/articlePush")
    @LogAnnotation(module="文章",operation="文章推送")
    // @Cache(expire = 5*60*1000,name = "listArticle")
    public Result articlePush(@RequestBody PageParams pageParams) {
        Result result = articleService.articlePush(pageParams);
        return result;
    }
    // @PostMapping("/FindPushArticle")
    // public Result FindPushArticle(@RequestBody PageSearchParam pageSearchParam){
    //     Result result=articleService.FindPushArticle(pageSearchParam);
    //     return result;
    // }

    /*
     * 首页最热文章显示
     * */
    //使用aop实现的缓存
    @Cache(expire = 5*60*1000,name = "hot_article")
    @PostMapping("/hot")
    public Result hotArticles() {
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /*
     * 首页最新文章
     * */
    @PostMapping("/new")
    @Cache(expire = 5*60*1000,name = "new_article")
    public Result newArticles() {
        int limit = 5;
        return articleService.newArticles(limit);
    }

    /*
     * 文章归档：每一篇文章创建的时间（年月），某年某月发表了多少文章
     *
     * 注意，如果在mapper中使用了格式化快捷键会出问题，year会和后面的语句分开
     * */
    @PostMapping("/listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

    /*
    * 查询文章详情
    * */
    @PostMapping("/view/{id}")
    public Result view(@PathVariable("id") Long articleId){
        return articleService.findByArticleId(articleId);
    }

    /*
    * 提交文章
    * */
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

    @PostMapping("/{id}")
    public Result articleById(@PathVariable("id")Long articleId){
        return articleService.findByArticleId(articleId);
    }

    /*
    * 查找文章
    * */
    @PostMapping("/search")
    public Result search(@RequestBody SearchParam searchParam){
        return articleService.search(searchParam);
    }

    /*
    * 删除文章
    * */
    @DeleteMapping("/delete/{id}")
    public Result deleteArticleById(@PathVariable("id") Long id){
        return articleService.deleteArticleById(id);
    }


}
