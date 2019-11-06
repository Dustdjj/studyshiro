package cn.dust.server.service;

import cn.dust.model.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: dust
 * @Date: 2019/10/17 21:16
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    void deleteBatch(List<Long> roleIds);

    void saveOrUpdate(Long userId, List<Long> roleIds);

    List<Long> queryRoleIdList(Long userId);
}
