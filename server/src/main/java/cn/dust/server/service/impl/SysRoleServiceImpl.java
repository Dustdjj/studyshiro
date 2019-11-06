package cn.dust.server.service.impl;

import cn.dust.common.utils.PageUtil;
import cn.dust.common.utils.QueryUtil;
import cn.dust.model.entity.SysPost;
import cn.dust.model.entity.SysRole;
import cn.dust.model.mapper.SysRoleMapper;
import cn.dust.server.service.SysRoleDeptService;
import cn.dust.server.service.SysRoleMenuService;
import cn.dust.server.service.SysRoleService;
import cn.dust.server.service.SysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/17 13:04
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    private static final Logger log= LoggerFactory.getLogger(SysDeptServiceImpl.class);

    @Override
    public PageUtil queryPage(Map<String, Object> map) {
        String search= (map.get("search")!=null)? (String) map.get("search") : "";

        IPage<SysRole> iPage=new QueryUtil<SysRole>().getQueryPage(map);

        QueryWrapper wrapper=new QueryWrapper<SysRole>()
                .like(StringUtils.isNotBlank(search),"role_name",search);

        IPage<SysRole> resPage=this.page(iPage,wrapper);
        return new PageUtil(resPage);
    }

    //新增
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRole role) {
        role.setCreateTime(DateTime.now().toDate());
        this.save(role);

        SysRole role1=this.getOne(new QueryWrapper<SysRole>().eq("role_name",role.getRoleName()));

//        System.out.println("role1.getRoleId()=="+role1.getRoleId());
//        System.out.println(role1.toString());
        try{
            //插入角色~菜单关联信息
            sysRoleMenuService.saveOrUpdate(role1.getRoleId(),role.getMenuIdList());
        }catch (Exception e){
            System.out.println("1111111111111111111111111111111111111111111111111111111111111111111");
        }


        //插入角色~部门关联信息
        sysRoleDeptService.saveOrUpdate(role1.getRoleId(),role.getDeptIdList());
    }

    //修改
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRole role) {
//        this.updateById(role);

        //更新角色~菜单关联信息
        sysRoleMenuService.saveOrUpdate(role.getRoleId(),role.getMenuIdList());

        //更新角色~部门关联信息
        sysRoleDeptService.saveOrUpdate(role.getRoleId(),role.getDeptIdList());
    }


    //批量删除
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] ids) {
        List<Long> roleIds= Arrays.asList(ids);
        this.removeByIds(roleIds);

        //删除角色~菜单关联数据
        sysRoleMenuService.deleteBatch(roleIds);

        //删除角色~部门关联数据
        sysRoleDeptService.deleteBatch(roleIds);

        //删除角色~用户关联数据
        sysUserRoleService.deleteBatch(roleIds);
    }
}
