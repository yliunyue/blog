package com.service.impl;

import com.pojo.ArticleBody;
import com.mapper.ArticleBodyMapper;
import com.service.ArticleBodyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vo.params.ArticleBodyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-08
 */
@Service
public class ArticleBodyServiceImpl extends ServiceImpl<ArticleBodyMapper, ArticleBody> implements ArticleBodyService {
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    /*
    * 根据id查找内容
    * */
    @Override
    public ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
