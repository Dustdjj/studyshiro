package cn.dust.server.service;

import cn.dust.common.utils.PageUtil;
import cn.dust.model.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/21 13:46
 */
public interface SysLogService extends IService<SysLog> {

    PageUtil queryPage(Map<String, Object> params);

    void truncate();

}
