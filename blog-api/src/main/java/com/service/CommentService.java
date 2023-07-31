package com.service;

import com.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;
import com.vo.params.CommentParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-08
 */
public interface CommentService extends IService<Comment> {

    /*
    * 根据文章id,查询所有的评论列表
    * */
    Result commentByArticleId(Long id);

    /*
    * 评论
    * */
    Result comments(CommentParam commentParam);
}
