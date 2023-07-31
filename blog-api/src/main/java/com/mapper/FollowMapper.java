package com.mapper;

import com.pojo.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dragon
 * @since 2023-01-29
 */
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

    /*
    * 获取关注列表的用户id
    * */
    List<Long> findFollowUserIds(Long sysUserId);

    /*
    * 获取粉丝id
    * */
    List<Long> findFansIds(Long userId);
}
