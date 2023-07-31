package com.service;

import com.pojo.Follow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2023-01-29
 */
public interface FollowService extends IService<Follow> {

    /*
    * 关注  当前登录用户 关注--》文章作者
    * */
    Result attentionToUser(Long id);

    /*
    * 获取当前用户的关注列表
    * */
    Result followList();

    /*
    * 取消关注
    * */
    Result notAttentionTo(Long id);

    /*
    * 粉丝列表
    * */
    Result fansList();

    /*
    * 进入文章浏览页面，判断是否有关注这篇文章作者
    * */
    Result isFollow(Long id);

    //从文章取消关注
    Result notAttentionToByArticleId(Long id);
}
