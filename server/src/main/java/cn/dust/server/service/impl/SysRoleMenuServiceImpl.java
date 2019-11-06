package cn.dust.server.service.impl;

import cn.dust.common.utils.CommonUtil;
import cn.dust.model.entity.SysRoleMenu;
import cn.dust.model.mapper.SysRoleMenuMapper;
import cn.dust.server.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: dust
 * @Date: 2019/10/16 19:49
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    private static final Logger log= LoggerFactory.getLogger(SysRoleMenuServiceImpl.class);

    //维护角色~菜单关联信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {

//        System.out.println("roleId=="+roleId);
        if(roleId!=null){
            //需要先清除旧的关联数据，再插入新的关联信息
            deleteBatch(Arrays.asList(roleId));
        }
        SysRoleMenu sysRoleMenu;
        if (menuIdList!=null && !menuIdList.isEmpty()){
            for (Long mId:menuIdList){
                sysRoleMenu=new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setMenuId(mId);
                this.save(sysRoleMenu);
            }
        }
    }


    //根据角色id批量删除
    @Override
    public void deleteBatch(List<Long> roleIds) {
        if (roleIds!=null && !roleIds.isEmpty()){
            String delIds= Joiner.on(",").join(roleIds);
            baseMapper.deleteBatch(CommonUtil.concatStrToInt(delIds,","));
        }
    }

    //获取角色对应的菜单列表
    @Override
    public List<Long> queryMenuIdList(Long roleId) {
        return baseMapper.queryMenuIdList(roleId);
    }
}
