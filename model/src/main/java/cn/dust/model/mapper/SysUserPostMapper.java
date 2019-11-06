package cn.dust.model.mapper;

import cn.dust.model.entity.SysUserPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface SysUserPostMapper extends BaseMapper<SysUserPost> {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUserPost record);

    int insertSelective(SysUserPost record);

    SysUserPost selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUserPost record);

    int updateByPrimaryKey(SysUserPost record);

    //根据用户Id 获取角色Id列表
    List<Long> queryPostIdList(Long userId);

    //根据角色Id列表，批量删除
    int deleteBatch(@Param("roleIds") String roleIds);

    //根据用户id获取用户-岗位详情
    List<SysUserPost> getByUserId(@Param("userId") Long userId);

    Set<String> getPostNamesByUserId(@Param("userId") Long userId);

}