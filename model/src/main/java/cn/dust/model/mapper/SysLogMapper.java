package cn.dust.model.mapper;

import cn.dust.model.entity.SysLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SysLogMapper extends BaseMapper<SysLog> {
    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);
}