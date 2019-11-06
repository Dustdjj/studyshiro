package cn.dust.model.mapper;

import cn.dust.model.entity.SysRoleDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.jmx.export.annotation.ManagedOperation;


import java.util.List;

@Mapper
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept> {
    int deleteByPrimaryKey(Long id);

    int insert(SysRoleDept record);

    int insertSelective(SysRoleDept record);

    SysRoleDept selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRoleDept record);

    int updateByPrimaryKey(SysRoleDept record);

    //根据角色Id，获取部门Id列表
    List<Long> queryDeptIdList(Long[] roleIds);

    //根据角色Id数组，批量删除
    int deleteBatch(@Param("roleIds") String roleIds);

    List<Long> queryDeptIdListByRoleId(@Param("roleId") Long roleId);
}