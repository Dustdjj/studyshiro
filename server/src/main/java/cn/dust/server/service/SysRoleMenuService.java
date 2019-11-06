package cn.dust.server.service;

import cn.dust.model.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: dust
 * @Date: 2019/10/16 19:48
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    void deleteBatch(List<Long> roleIds);

    List<Long> queryMenuIdList(Long roleId);
}
