package com.service;

import com.entity.Comment;
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
public interface CommentService extends IService<Comment> {

    /*
     * 根据文章id,查询所有的评论列表
     * */
    Result commentByArticleId(Long id);

    /*
    * 根据评论id删除id
    * */
    Result deleteComment(Long id);

    /*
    * 分页查询全部评论
    * */
    Result pageComment(PageParam pageParam);
}
