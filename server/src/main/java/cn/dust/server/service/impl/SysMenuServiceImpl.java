package cn.dust.server.service.impl;

import cn.dust.model.entity.SysMenu;
import cn.dust.model.entity.SysRoleMenu;
import cn.dust.model.mapper.SysMenuMapper;
import cn.dust.server.service.SysMenuService;
import cn.dust.server.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: dust
 * @Date: 2019/10/16 11:40
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {


    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    //获取所有菜单列表
    @Override
    public List<SysMenu> queryAll() {
        return baseMapper.queryList();
    }

    //获取不包含菜单的树形层级列表数据
    @Override
    public List<SysMenu> queryNotButtonList() {
        return baseMapper.queryNotButtonList();
    }

    //根据父级菜单id查询其下的子菜单列表
    @Override
    public List<SysMenu> queryByParentId(Long menuId) {
        return baseMapper.queryListParentId(menuId);
    }

    //删除
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long menuId) {
        removeById(menuId);
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id",menuId));
    }


    @Override
    public List<SysMenu> getUserMenuList(Long currUserId) {
        return null;
    }
}