package cn.dust.model.mapper;

import cn.dust.model.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface SysUserMapper extends BaseMapper<SysUser> {
    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    //查询用户的所有权限
    List<String> queryAllPerms(Long userId);

    //查询用户的所有权限
    List<Long> queryAllMenuId(Long userId);

    //根据用户id获取部门数据Id列表 ~ 数据权限
    Set<Long> queryDeptIdsByUserId(Long userId);

    SysUser selectByUserName(@Param("userName") String userName);
}