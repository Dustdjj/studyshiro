package cn.dust.model.mapper;

import cn.dust.model.entity.SysPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {
    int deleteByPrimaryKey(Long postId);

    int insert(SysPost record);

    int insertSelective(SysPost record);

    SysPost selectByPrimaryKey(@Param("post_id") Long postId);

    int updateByPrimaryKeySelective(SysPost record);

    int updateByPrimaryKey(SysPost record);

    int deleteBatch(@Param("ids") String ids);
}