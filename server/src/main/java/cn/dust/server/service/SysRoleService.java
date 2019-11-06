package cn.dust.server.service;

import cn.dust.common.utils.PageUtil;
import cn.dust.model.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/17 13:03
 */
public interface SysRoleService extends IService<SysRole> {


    PageUtil queryPage(Map<String,Object> map);

    void saveRole(SysRole role);

    void updateRole(SysRole role);

    void deleteBatch(Long[] ids);
}
