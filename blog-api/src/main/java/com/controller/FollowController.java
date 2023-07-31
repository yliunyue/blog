package com.controller;

import com.service.FollowService;
import com.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-01-29
 */
@RestController
@RequestMapping("/follow")
@Slf4j
public class FollowController {
    @Autowired
    private FollowService followService;

    /*
    * 关注  当前登录用户 关注--》文章作者
    * */
    @PostMapping("/attentionTo/{id}")//传的是文章id
    public Result attentionToUser(@PathVariable Long id){
        log.info("文章id：{}",id);
        return followService.attentionToUser(id);
    }

    /*
    * 确认是否关注
    * */
    @PostMapping("/isFollow/{id}")//传的是文章id
    public Result isFollow(@PathVariable Long id){
        log.info("文章id：{}",id);
        return followService.isFollow(id);
    }

    /*
    * 取消关注从文章中
    * */
    @DeleteMapping("/notAttentionToByArticleId/{id}")//传的是的文章id
    public Result notAttentionToByArticleId(@PathVariable Long id){
        log.info("需要取消关注的id：{}",id);
        return followService.notAttentionToByArticleId(id);
    }


    /*
    * 我的关注列表
    * */
    @GetMapping("/followList")
    public Result followList(){
        return followService.followList();
    }
    /*
    * 我的粉丝
    * */
    @GetMapping("/fansList")
    public Result fansList(){
        return followService.fansList();
    }

    /*
    * 取消关注
    * */
    @GetMapping("/notAttentionTo/{id}")//传的是关注的用户id
    public Result notAttentionTo(@PathVariable Long id){
        log.info("需要取消关注的id：{}",id);
        return followService.notAttentionTo(id);
    }

}
