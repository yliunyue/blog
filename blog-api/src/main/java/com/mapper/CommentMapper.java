package com.mapper;

import com.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dragon
 * @since 2023-01-08
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
