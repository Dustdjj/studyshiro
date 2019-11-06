package cn.dust.server.service;

import cn.dust.model.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: dust
 * @Date: 2019/10/16 11:39
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> queryAll();

    List<SysMenu> queryNotButtonList();

    List<SysMenu> queryByParentId(Long menuId);

    void delete(Long menuId);

    List<SysMenu> getUserMenuList(Long currUserId);
}
