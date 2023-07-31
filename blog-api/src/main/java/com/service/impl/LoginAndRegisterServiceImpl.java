package com.service.impl;

import com.alibaba.fastjson.JSON;
import com.pojo.SysUser;
import com.service.LoginAndRegisterService;
import com.utils.JWTUtils;
import com.vo.ErrorCode;
import com.vo.Result;
import com.vo.params.LoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginAndRegisterServiceImpl implements LoginAndRegisterService {
    @Autowired
    @Lazy
    private SysUserServiceImpl sysUserService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //加密盐
    private static final String salt = "dragon!@#";

    /*
     * 登录功能
     * */
    @Override
    public Result login(LoginParams loginParam) {
        //1.检查参数是否合法
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        // String code = loginParam.getCode();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            //参数错误
            Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //2.根据用户名和密码在ms_sys_user表中查询是否存在
        //对密码解析md5加密，但是由于md5会被破解，所以加上加密盐
        password = DigestUtils.md5Hex(password + salt);
        SysUser sysUser = sysUserService.findUser(account, password);
        //3.如果不存在，登录失败
        if (sysUser == null) {
            //密码错误，或者用户名错误
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        // 更新最后登录时间
        // long l = System.currentTimeMillis();
        // sysUser.setLastLogin(l);
        // sysUserService.updateById(sysUser);
        //4.如果存在，使用jwt生成token返回给前端
        String token = JWTUtils.createToken(sysUser.getId());
        //5.token放入redis中，redis token：user信息 设置过期时间（登录认证的时候先认证字符串是否合法，去redis看是否存在）
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
        //前端获取到token后，会存储在storage中h5
    }

    /*
     * token合法校验
     * */
    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null) {
            return null;
        }
        //从redis中获得登录用户的token
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)) {
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    /*
     * 退出登录
     * */
    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_" + token);
        return Result.success(null);
    }

    /*
     * 注册
     * */
    @Override
    public Result register(LoginParams loginParams) {
        //1.判断参数是否合法
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        String nickName = loginParams.getNickname();
        String code = loginParams.getCode();
        String email = loginParams.getEmail();
        System.out.println(email);
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickName)) {
            //账号不合法
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //2.判断账户是否唯一，存在，返回账户已经被注册
        SysUser sysUser = sysUserService.finduserByAccount(account);
        if (sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        //3.账户不存在，判断验证码是否与redis中的一致
        String codeRedis = redisTemplate.opsForValue().get(email);
        if (!code.equals(codeRedis) ) {
            return Result.fail(ErrorCode.CODE_ERROR.getCode(),ErrorCode.CODE_ERROR.getMsg());
        }
        sysUser = new SysUser();
        sysUser.setAccount(account);
        sysUser.setNickname(nickName);
        sysUser.setPassword(DigestUtils.md5Hex(password + salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("http://ruizmypmv.hn-bkt.clouddn.com/my-image-1683862533997");
        sysUser.setAdmin(1);
        sysUser.setDeleted(0);
        sysUser.setSalt(salt);
        sysUser.setStatus("");
        sysUser.setEmail(email);
        this.sysUserService.save(sysUser);
        //4.生成token
        String token = JWTUtils.createToken(sysUser.getId());
        //5.存入redis并返回token
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        //6.注意加上事务，一旦发生问题，就要回滚
        return Result.success("注册成功");
    }

    @Value("${spring.mail.username}")
    private String from;   // 邮件发送人
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMsg(String email, String subject, String context) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(context);
        // 真正的发送邮件操作，从 from到 to
        mailSender.send(mailMessage);
    }
}
