package cn.dust.server.service.impl;

import cn.dust.common.utils.PageUtil;
import cn.dust.model.entity.SysLog;
import cn.dust.model.mapper.SysLogMapper;
import cn.dust.server.service.SysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/21 13:47
 */
@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        return null;
    }

    @Override
    public void truncate() {

    }
}
