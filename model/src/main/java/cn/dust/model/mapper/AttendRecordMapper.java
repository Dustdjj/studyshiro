package cn.dust.model.mapper;

import cn.dust.model.entity.AttendRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface AttendRecordMapper extends BaseMapper<AttendRecord> {
    int deleteByPrimaryKey(Integer id);

    int insert(AttendRecord record);

    int insertSelective(AttendRecord record);

    AttendRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AttendRecord record);

    int updateByPrimaryKey(AttendRecord record);
}