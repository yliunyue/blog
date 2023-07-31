package com.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dos.SysUserMessage;
import com.mapper.FollowMapper;
import com.pojo.Follow;
import com.pojo.SysUser;
import com.mapper.SysUserMapper;
import com.service.FollowService;
import com.service.LoginAndRegisterService;
import com.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.UserThreadLocal;
import com.vo.ErrorCode;
import com.vo.Result;
import com.vo.params.LoginUserVO;
import com.vo.params.UpdataEmailParam;
import com.vo.params.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-06
 */
@Service

public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private LoginAndRegisterService loginService;

    @Autowired
    private FollowMapper followMapper;

    @Override
    public SysUser findByIdSysUser(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("dragon");
        }
        return sysUser;
    }

    /*
     * 根据账号密码查找用户
     * */
    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        //密码是加密的，不能直接比较,在loginService里面处理加密信息
        queryWrapper.eq(SysUser::getPassword, password);
        queryWrapper.select(SysUser::getAccount, SysUser::getId, SysUser::getAvatar, SysUser::getNickname);
        queryWrapper.last("limit 1");//查到就停止，防止查到最后。
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }

    /*
     * 根据token查询用户信息
     * */
    @Override
    public Result findUserByToken(String token) {
        //1.token合法性校验
        //1.1 是否为空
        //1.2 解析是否成功
        //1.3 redis是否存在
        SysUser sysUser = loginService.checkToken(token);
        //2. 校验失败，返回错误
        if (sysUser == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        //3 成功，返回对应结果（LoginUserVO）
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(sysUser, loginUserVO);
        return Result.success(loginUserVO);
    }

    /*
     * 根据账户查找用户
     * */
    @Override
    public SysUser finduserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account).last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("dragon");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }

    @Override
    public Result findSysUserMessage() {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        SysUser sysUser1 = sysUserMapper.selectById(userId);
        SysUserMessage sysUserMessage = new SysUserMessage();
        BeanUtils.copyProperties(sysUser1,sysUserMessage);
        sysUserMessage.setCreateDate(new DateTime(sysUser1.getCreateDate()).toString("yyyy-MM-dd"));
        //粉丝数
        LambdaQueryWrapper<Follow> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getFollowUserId,userId);
        Integer fans = followMapper.selectCount(queryWrapper);
        sysUserMessage.setFans(fans);
        //关注数
        LambdaQueryWrapper<Follow> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Follow::getUserId,userId);
        Integer followerNum = followMapper.selectCount(queryWrapper1);
        sysUserMessage.setFollowerNums(followerNum);
        return Result.success(sysUserMessage);
    }

    @Override
    public Result isCode(String code) {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        //查询邮件
        SysUser sysUser1 = sysUserMapper.selectById(userId);
        String email = sysUser1.getEmail();
        //从redis中获取验证码
        String codeRedis = redisTemplate.opsForValue().get(email);
        if (!code.equals(codeRedis) ) {
            return Result.fail(ErrorCode.CODE_ERROR.getCode(),ErrorCode.CODE_ERROR.getMsg());
        }
        return Result.success("通过");
    }

    @Override
    public Result updataEmail(String code,String email) {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        //验证码
        String codeRedis = redisTemplate.opsForValue().get(email);
        if (code.equals(codeRedis)){
            //邮箱和验证码相同
            LambdaUpdateWrapper<SysUser> updateWrapper=new LambdaUpdateWrapper<>();
            updateWrapper.eq(SysUser::getId,userId).set(SysUser::getEmail,email);
            int update = sysUserMapper.update(null, updateWrapper);
            return Result.success("修改邮箱成功");
        }
        return Result.fail(50002,"邮箱修改失败");
    }

    @Override
    public Result updataMes(String avatar, String nickname,String token) {
        //获取当前登录用户
        SysUser sysUser = UserThreadLocal.get();
        Long userId = sysUser.getId();
        //修改redis中的信息
        // 先获取
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)) {
            return null;
        }
        SysUser sysUserOfRedis = JSON.parseObject(userJson, SysUser.class);
        System.out.println("redis中的sysuser信息："+sysUserOfRedis);
        //修改信息
        sysUserOfRedis.setAvatar(avatar);
        sysUserOfRedis.setNickname(nickname);
        //将其设置到redis中
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUserOfRedis), 1, TimeUnit.DAYS);
        //邮箱和验证码相同
        LambdaUpdateWrapper<SysUser> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getId,userId).set(SysUser::getAvatar,avatar).set(SysUser::getNickname,nickname);
        int update = sysUserMapper.update(null, updateWrapper);
        return Result.success("修改信息成功");
    }
}
