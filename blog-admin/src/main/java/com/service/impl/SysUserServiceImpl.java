package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.SysUser;
import com.mapper.SysUserMapper;
import com.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vo.Result;
import com.vo.params.PageParam;
import com.vo.params.PageResult;
import com.vo.SysUserVo;
import com.vo.UserVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2023-01-20
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    /*
    * 用户列表
    * */
    @Override
    public Result userList(PageParam pageParam) {
        //分页查询
        IPage<SysUser> page=new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        //如果查询条件不为空，根据account查询
        if (pageParam.getQueryString()!=null){
            queryWrapper.like(SysUser::getAccount,pageParam.getQueryString())
                    .or().like(SysUser::getNickname,pageParam.getQueryString());
        }
        sysUserMapper.selectPage(page,queryWrapper);
        //返回给前端总页数，和当前页面记录数，返回的是vo pageResult
        List<SysUser> records = page.getRecords();
        PageResult<SysUserVo> sysUserVoPageResult = new PageResult<>();
        List<SysUserVo> sysUserVoList = records.stream().map(sysUser -> {
            SysUserVo sysUserVo = new SysUserVo();
            sysUserVo.setId(String.valueOf(sysUser.getId()));
            BeanUtils.copyProperties(sysUser, sysUserVo);
            sysUserVo.setCreateDate(new DateTime(sysUser.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
            sysUserVo.setLastLogin(new DateTime(sysUser.getLastLogin()).toString("yyyy-MM-dd HH:mm"));
            return sysUserVo;
        }).collect(Collectors.toList());
        sysUserVoPageResult.setTotal(page.getTotal());
        sysUserVoPageResult.setList(sysUserVoList);
        return Result.success(sysUserVoPageResult);
    }

    /*
    * 根据用户id删除用户
    * */
    @Override
    public Result deleteUserById(Long id) {
        //删除用户，就是将表中delete字段改为1
        LambdaUpdateWrapper<SysUser> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getId,id).set(SysUser::getDeleted,1);
        sysUserMapper.update(null,updateWrapper);
        return Result.success(null);
    }

    @Override
    public Result notDeleteUser(Long id) {
        //启用，就是将表中delete字段改为0
        LambdaUpdateWrapper<SysUser> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getId,id).set(SysUser::getDeleted,0);
        sysUserMapper.update(null,updateWrapper);
        return Result.success(null);
    }

    /*
    * 根据authorId查询出用户（谁评论的）
    * */
    @Override
    public UserVo findByAuthorId(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }

    /*
    * 根据作者名字模糊查询，返回集合
    * */
    @Override
    public List<Long> selectByAuthorName(String queryString) {
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(SysUser::getNickname,queryString);
        List<SysUser> sysUsers = sysUserMapper.selectList(queryWrapper);
        List<Long> ids=new ArrayList<>();
        for (SysUser sysUser : sysUsers) {
            ids.add(sysUser.getId());
        }
        return ids;
    }
}
