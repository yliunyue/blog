package com.service;

import com.pojo.ArticleBody;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.params.ArticleBodyVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-08
 */
public interface ArticleBodyService extends IService<ArticleBody> {

    /*
    * 根据id查找内容
    * */
    ArticleBodyVo findArticleBodyById(Long bodyId);
}
