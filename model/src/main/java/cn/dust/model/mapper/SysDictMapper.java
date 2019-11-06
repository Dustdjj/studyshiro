package cn.dust.model.mapper;

import cn.dust.model.entity.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SysDictMapper extends BaseMapper<SysDict> {
    int deleteByPrimaryKey(Long id);

    int insert(SysDict record);

    int insertSelective(SysDict record);

    SysDict selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysDict record);

    int updateByPrimaryKey(SysDict record);
}