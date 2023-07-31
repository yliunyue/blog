package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.Article;
import com.entity.ArticleBody;
import com.entity.Category;
import com.entity.SysUser;
import com.mapper.ArticleMapper;
import com.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vo.Result;
import com.vo.ArticleVo;
import com.vo.params.PageParam;
import com.vo.params.PageResult;
import com.vo.TagVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-27
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleBodyService articleBodyService;

    /*
     * 文章列表，（文章名字、作者名字、文章类别、标签）查询
     * */
    @Override
    public Result articleList(PageParam pageParam) {
        IPage<Article> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //条件查询
        if (pageParam.getQueryString() != null) {
            //根据文章名字(标题title)查询
            queryWrapper.like(Article::getTitle, pageParam.getQueryString());
            //根据作者名字查询。1.先根据 查询内容 查出作者（list）2.根据查询到的作者ids来查询文章。
            List<Long> authorIds = sysUserService.selectByAuthorName(pageParam.getQueryString());
            if (authorIds.size() != 0) {
                queryWrapper.or().in(Article::getAuthorId, authorIds);
            }
            //根据文章类别查询 1.先根据类别名字查询出类别（list） 2.根据ids查询文章
            List<Long> categoryIds = categoryService.selectBycategoryName(pageParam.getQueryString());
            if (categoryIds.size() != 0) {
                queryWrapper.or().in(Article::getCategoryId, categoryIds);
            }
            //根据类别标签查询 1.先根据标签名字查询出标签ids 2.在ms_article_tag表中查询出有关所有的文章ids 3.根据文章ids查询文章
            List<Long> articleIds = tagService.selectByTagName(pageParam.getQueryString());
            if (articleIds.size() != 0) {
                queryWrapper.or().in(Article::getId, articleIds);
            }
        }
        IPage<Article> articleIPage = articleMapper.selectPage(page, queryWrapper);
        //返回页面数据
        PageResult<ArticleVo> articleVoPageResult = new PageResult<>();
        //对文章对象做处理，展示到页面Vo
        List<Article> articles = articleIPage.getRecords();
        List<ArticleVo> articleVos = new ArrayList<>();
        for (Article article : articles) {
            ArticleVo articleVo = new ArticleVo();
            BeanUtils.copyProperties(article, articleVo);
            //autor
            SysUser sysUser = sysUserService.getById(article.getAuthorId());
            articleVo.setAuthor(sysUser.getNickname());
            //createtime
            articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
            // tags
            List<TagVo> tagVos = tagService.selectByArticleId(article.getId());
            articleVo.setTags(tagVos);
            //category
            Category category = categoryService.getById(article.getCategoryId());
            articleVo.setCategory(category);
            articleVos.add(articleVo);
        }
        articleVoPageResult.setList(articleVos);
        articleVoPageResult.setTotal(articleIPage.getTotal());
        return Result.success(articleVoPageResult);
    }

    /*
     * 删除
     * */
    @Override
    public Result deleteArticle(Long id) {
        //删除文章还需要删除article_body表中的内容
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId, id);
        boolean remove = articleBodyService.remove(queryWrapper);
        //删除article里面的内容
        articleMapper.deleteById(id);

        return Result.success("删除成功");
    }

    @Override
    public String findTitleById(Long articleId) {
        return articleMapper.findTitleById(articleId);
    }
}
