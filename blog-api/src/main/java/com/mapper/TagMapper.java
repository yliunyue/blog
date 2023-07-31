package com.mapper;

import com.pojo.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vo.params.TagVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dragon
 * @since 2023-01-06
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /*
    * 根据文章id查询标签列表
    * */
    List<TagVo> findTagByArticledId(Long articleId);

    /*
    * 查找热标签的id集合
    * */
    List<Long> findHotTagIds(int limit);
}
