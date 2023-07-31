package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mapper.SysUserMapper;
import com.pojo.Follow;
import com.mapper.FollowMapper;
import com.pojo.SysUser;
import com.service.ArticleService;
import com.service.FollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.service.SysUserService;
import com.utils.UserThreadLocal;
import com.vo.ErrorCode;
import com.vo.Result;
import com.vo.params.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-29
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SysUserMapper sysUserMapper;

    /*
    * 关注  当前登录用户 关注--》文章作者
    * */
    @Override
    public Result attentionToUser(Long id) {
        Follow follow = new Follow();
        //1 获取当前登录用户id
        SysUser sysUser = UserThreadLocal.get();
        Long sysUserId = sysUser.getId();
        follow.setUserId(sysUserId);
        //2 关注用户的id
        //根据文章id查询出用户id
        Long followUserId=articleService.findAuthorId(id);
        //如果这是自己写的文章，不需要关注
        if (sysUserId == followUserId) {
            //不能关注自己
            return Result.fail(ErrorCode.NOT_ATTENTION_YOURSELF.getCode(), ErrorCode.NOT_ATTENTION_YOURSELF.getMsg());
        }
        //只能关注一次，根据需要关注的followUserId查询数据库中是否有数据
        LambdaQueryWrapper<Follow> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId,sysUserId)
                        .eq(Follow::getFollowUserId,followUserId);
        Follow follow1 = followMapper.selectOne(queryWrapper);
        if (follow1 != null) {
            //已经关注了
            return Result.fail(ErrorCode.NOT_ATTENTION_AGAIN.getCode(), ErrorCode.NOT_ATTENTION_AGAIN.getMsg());
        }
        follow.setFollowUserId(followUserId);
        //3 关注时间
        follow.setCreateTime(System.currentTimeMillis());
        // 插入表ms_follow
        followMapper.insert(follow);
        return Result.success("关注成功");
    }

    @Override
    public Result isFollow(Long id) {
        //1 获取当前登录用户id
        SysUser sysUser = UserThreadLocal.get();
        Long sysUserId = sysUser.getId();
        //根据文章id查询出用户id
        Long followUserId=articleService.findAuthorId(id);
        LambdaQueryWrapper<Follow> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId,sysUserId).eq(Follow::getFollowUserId,followUserId);
        Follow follow = followMapper.selectOne(queryWrapper);
        if (follow == null) {
            return Result.fail(50001,"未关注");
        }else {
            return Result.success("已关注");
        }
    }

    @Override
    public Result notAttentionToByArticleId(Long id) {
        //1 获取当前登录用户id
        SysUser sysUser = UserThreadLocal.get();
        Long sysUserId = sysUser.getId();
        //根据文章id查询出用户id
        Long followUserId=articleService.findAuthorId(id);
        LambdaQueryWrapper<Follow> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId,sysUserId).eq(Follow::getFollowUserId,followUserId);
        int delete = followMapper.delete(queryWrapper);
        return Result.success("取消关注成功");
    }

    /*
    * 获取当前用户的关注列表
    * */
    @Override
    public Result followList() {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        //根据当前登录用户获取其关注的列表(头像  昵称 UserVo)
        List<Long> followUserIds=followMapper.findFollowUserIds(userId);
        //
        if (followUserIds.size()==0){
            return Result.success(null);
        }
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(SysUser::getId,followUserIds);
        List<SysUser> sysUsers = sysUserMapper.selectList(queryWrapper);
        List<UserVo> userVoList = sysUsers.stream().map(sysUser1 -> {
            UserVo userVo = new UserVo();
            userVo.setId(sysUser1.getId());
            userVo.setAvatar(sysUser1.getAvatar());
            userVo.setNickname(sysUser1.getNickname());
            return userVo;
        }).collect(Collectors.toList());
        return Result.success(userVoList);
    }

    /*
    * 粉丝列表
    * */
    @Override
    public Result fansList() {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        //根据用户查询其粉丝
        List<Long> fansIds=followMapper.findFansIds(userId);
        if (fansIds.size()==0){
            return Result.success(null);
        }
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(SysUser::getId,fansIds);
        List<SysUser> sysUsers = sysUserMapper.selectList(queryWrapper);
        List<UserVo> userVoList = sysUsers.stream().map(sysUser1 -> {
            UserVo userVo = new UserVo();
            userVo.setId(sysUser1.getId());
            userVo.setAvatar(sysUser1.getAvatar());
            userVo.setNickname(sysUser1.getNickname());
            return userVo;
        }).collect(Collectors.toList());
        return Result.success(userVoList);
    }

    /*
    * 取消关注
    * */
    @Override
    public Result notAttentionTo(Long id) {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        //将ms_follow表中的数据删除
        LambdaQueryWrapper<Follow> queryWrapper=new LambdaQueryWrapper<>();
        //当关注的id为这个时，并且要是当前登录用户(不然多人关注同一个人，会因为一个人取关而全部取关)
        queryWrapper.eq(Follow::getFollowUserId,id)
                        .eq(Follow::getUserId,userId);
        followMapper.delete(queryWrapper);
        return Result.success("取消关注成功");
    }


}
