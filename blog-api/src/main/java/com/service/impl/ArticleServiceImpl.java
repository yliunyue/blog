package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dos.Archives;
import com.mapper.CommentMapper;
import com.mapper.FollowMapper;
import com.pojo.*;
import com.mapper.ArticleMapper;
import com.service.*;
import com.utils.UserThreadLocal;
import com.vo.Result;
import com.vo.params.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleBodyService articleBodyService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private CommentService commentService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /*
    * 我的文章
    * */
    @Override
    public Result myListArticle(PageParams pageParams) {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();

        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.myListArticle(page, pageParams.getCategoryId(), pageParams.getTagId(), pageParams.getYear(), pageParams.getMonth(),userId,pageParams.getQueryString());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));

    }

/*    @Override
    public Result findMyListArticle(PageSearchParam pageSearchParam) {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        Page<Article> page = new Page<>(pageSearchParam.getPage(), pageSearchParam.getPageSize());
        IPage<Article> myListArticle = articleMapper.findMyListArticle(page, pageSearchParam.getQueryString(),userId);
        List<Article> records = myListArticle.getRecords();
        return Result.success(copyList(records,true,true));
    }*/
    /*
    * 文章推送
    * */
    @Override
    public Result articlePush(PageParams pageParams) {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        //获取其关注的用户
        List<Long> followUserIds = followMapper.findFollowUserIds(userId);
        if (followUserIds.size() == 0) {
            return Result.success(null);
        }
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.articlePush(page, pageParams.getCategoryId(), pageParams.getTagId(), pageParams.getYear(), pageParams.getMonth(),followUserIds,pageParams.getQueryString());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }

/*    @Override
    public Result FindPushArticle(PageSearchParam pageSearchParam) {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        //获取其关注的用户
        List<Long> followUserIds = followMapper.findFollowUserIds(userId);
        if (followUserIds.size() == 0) {
            return Result.success(null);
        }
        Page<Article> page = new Page<>(pageSearchParam.getPage(), pageSearchParam.getPageSize());
        IPage<Article> articleIPage = articleMapper.FindPushArticle(page,followUserIds,pageSearchParam.getQueryString());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }*/

    /*
    * 自己写的
    * */
    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page, pageParams.getCategoryId(), pageParams.getTagId(), pageParams.getYear(), pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }
    /*
    * mybatisplus的
    * */
    /*@Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //当需要根据类别查询时
        queryWrapper.eq(pageParams.getCategoryId() != null, Article::getCategoryId, pageParams.getCategoryId());
        //当需要根据标签查询时
        List<Long> articleIdList=new ArrayList<>();
        if (pageParams.getTagId()!=null){
            //加上标签条件
            //article表中没有tagId字段，要在article_tag表中茶轴（一对多）
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper=new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
            List<ArticleTag> articleTagList = articleTagService.list(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTagList) {
                articleIdList.add(articleTag.getArticleId());
            }
            //把条件加入
            if (articleIdList.size() > 0) {
                queryWrapper.in(Article::getId,articleIdList);
            }
        }
        //置顶排序,时间排序
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
        //注意，在文章归档时，前端会穿送year和month
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        //获取当前页面数据
        List<Article> records = articlePage.getRecords();
        //将当前页面数据转换为前端所展示的内容
        List<ArticleVo> articleVoList = copyList(records, true, true);
        return Result.success(articleVoList);
    }*/

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Comment::getArticleId,record.getId());
            int count = commentService.count(queryWrapper);
            record.setCommentCounts(count);
            articleVoList.add(copy(record, isTag, true, isAuthor, false));
        }
        // log.info("转换为前端数据为",articleVoList.toString());
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        // log.info("转换为前端数据为",articleVoList.toString());
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        //不是所有的文章都有标签和作者
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        // articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM"));
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if (isTag) {
            Long articleVoId = article.getId();
            List<TagVo> tagByArticledId = tagService.findTagByArticledId(articleVoId);
            articleVo.setTags(tagByArticledId);
        }
        if (isAuthor) {
            //将作者名字设置进去
            Long authorId = article.getAuthorId();
            SysUser byIdSysUser = sysUserService.findByIdSysUser(authorId);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(byIdSysUser,userVo);
            // articleVo.setAuthor(byIdSysUser.getNickname());
            articleVo.setAuthor(userVo);
        }
        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(articleBodyService.findArticleBodyById(bodyId));
        }
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    /*
     * 首页最热文章显示
     * */
    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts)
                .select(Article::getId, Article::getTitle)
                .last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        //Article的属性不适合给前端，应该将ArticleArticleArticleVo返回给前端,进行属性copy
        List<ArticleVo> articleVoList = copyList(articles, false, false);
        return Result.success(articleVoList);
    }

    /*
     * 首页最新文章
     * */
    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate)
                .select(Article::getId, Article::getTitle)
                .last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        //Article的属性不适合给前端，应该将ArticleArticleArticleVo返回给前端,进行属性copy
        List<ArticleVo> articleVoList = copyList(articles, false, false);
        return Result.success(articleVoList);
    }

    /*
     * 文章归档
     * */
    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    /*
     * 查看文章详情
     * */

    @Override
    public Result findByArticleId(Long articleId) {
        //1.根据id查询文章信息
        //2.根据body——id和category——id做关联查询
        ArticleVo articleVo = new ArticleVo();
        Article article = this.articleMapper.selectById(articleId);
        articleVo = copy(article, true, true, true, true);
        //查看文章之后，新增阅读数，
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他读操作，性能就会降低
        //更新增加了此次接口的耗时 一旦更新出问题，不能影响 查看文章的操作
        //线程池   可以把更新操作放到线程池中执行，就和主线程不相关了
        threadService.updateArticleVoCount(articleMapper, article);
        return Result.success(articleVo);
    }

    /*
     * 提交文章
     * */
    @Override
    public Result publish(ArticleParam articleParam) {
        //这个接口添加到拦截器中，获取当前队登录用户
        SysUser sysUser = UserThreadLocal.get();
        //1.发布文章，就是要构建article对象
        Article article = new Article();
        if (articleParam.getId() != null) {
            article.setId(articleParam.getId());
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCategoryId(articleParam.getCategory().getId());
            articleMapper.updateById(article);
        }
        else {
            article.setWeight(Article.Article_Common);
            article.setViewCounts(0);
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCommentCounts(0);
            article.setCreateDate(System.currentTimeMillis());
            article.setCategoryId(articleParam.getCategory().getId());
            //插入之后就会自动生成一个文章id。
            articleMapper.insert(article);
        }
        //2.作者id,被拦截器拦截，从ThreadLocal中获取作者id
        article.setAuthorId(sysUser.getId());
        //3.标签，将文章的标签加入到article——tag表中
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            List<ArticleTag> articleTagList = new ArrayList<>();
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(tag.getId());
                articleTagList.add(articleTag);
            }
            articleTagService.saveBatch(articleTagList);
        }
        //body  (id content content_html article_id)
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyService.save(articleBody);
        //body_id 上面执行完成后就会有bodyid
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        /*ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());*/
        Map<String, String> map = new HashMap<>();
        String s = article.getId().toString();
        map.put("id", s);
        return Result.success(map);
    }

    @Override
    public Long findAuthorId(Long id) {
        return articleMapper.findAuthorId(id);
    }

    /*
    * 查找文章
    * */
    @Override
    public Result search(SearchParam searchParam) {
        //查找文章，后期可以的话，记得改进
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(Article::getTitle,searchParam.getSearch())
                .orderByDesc(Article::getViewCounts);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        List<ArticleVo> articleVoList = copyList(articles, true, true);
        return Result.success(articleVoList);
    }

    @Override
    @Transactional
    public Result deleteArticleById(Long id) {
        //删除文章还需要删除article_body表中的内容
        LambdaQueryWrapper<ArticleBody> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId,id);
        boolean remove = articleBodyService.remove(queryWrapper);
        //删除标签关联表中的内容
        LambdaQueryWrapper<ArticleTag> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(ArticleTag::getArticleId,id);
        boolean remove1 = articleTagService.remove(queryWrapper1);
        //删除对应评论
        LambdaQueryWrapper<Comment> queryWrapper2=new LambdaQueryWrapper<>();
        queryWrapper2.eq(Comment::getArticleId,id);
        boolean remove2 = commentService.remove(queryWrapper2);
        //删除article里面的内容
        int i = articleMapper.deleteById(id);
        if (i <= 0) {
            return Result.fail(50004,"删除失败");
        }
        return Result.success("删除成功");
    }


}
