package com.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mapper.ArticleMapper;
import com.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {
    //期望此操作在线程池中执行，不会影响原有的主线程

    @Async("taskExecutor")
    public void updateArticleVoCount(ArticleMapper articleMapper, Article article) {
        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts+1);
        LambdaUpdateWrapper<Article> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //设置一个 为了在多线程的环境下 线程安全
        updateWrapper.eq(Article::getViewCounts,viewCounts);//类似于乐观锁
        articleMapper.update(articleUpdate,updateWrapper);
    }
}
