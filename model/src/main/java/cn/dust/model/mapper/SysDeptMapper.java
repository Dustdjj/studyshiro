package cn.dust.model.mapper;

import cn.dust.model.entity.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
//    int deleteByPrimaryKey(Long deptId);
//
//    int insert(SysDept record);
//
//    int insertSelective(SysDept record);
//
//    SysDept selectByPrimaryKey(Long deptId);
//
//    int updateByPrimaryKeySelective(SysDept record);
//
//    int updateByPrimaryKey(SysDept record);

    List<SysDept> queryList(Map<String,Object> params);

    //根据父级部门id查询子部门id列表
    List<Long> queryDeptIds(Long parentId);

    List<SysDept> queryListV2(Map<String, Object> params);

    Set<Long> queryAllDeptIds();

}