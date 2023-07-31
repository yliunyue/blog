package com.mapper;

import com.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dragon
 * @since 2023-04-13
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

}
