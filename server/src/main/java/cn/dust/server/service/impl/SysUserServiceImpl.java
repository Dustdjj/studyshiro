package cn.dust.server.service.impl;

import cn.dust.common.utils.Constant;
import cn.dust.common.utils.PageUtil;
import cn.dust.common.utils.QueryUtil;
import cn.dust.model.entity.*;
import cn.dust.model.mapper.SysUserMapper;
import cn.dust.server.service.SysDeptService;
import cn.dust.server.service.SysUserPostService;
import cn.dust.server.service.SysUserRoleService;
import cn.dust.server.service.SysUserService;
import cn.dust.server.shiro.ShiroUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/13 11:32
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper,SysUser> implements SysUserService {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserPostService sysUserPostService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    //更新密码   mybatis-plus方法
    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        SysUser sysUser=new SysUser();
        sysUser.setPassword(newPassword);
        return this.update(sysUser,new QueryWrapper<SysUser>().eq("user_id",userId).eq("password",oldPassword));
    }

    @Override
    public PageUtil queryPage(Map<String, Object> map) {
        String search=(map.get("username")!=null)? (String) map.get("username") :"";

        IPage<SysUser> iPage=new QueryUtil<SysUser>().getQueryPage(map);

        QueryWrapper wrapper=new QueryWrapper<SysUser>()
                .like(StringUtils.isNotBlank(search),"username",search.trim())
                .or(StringUtils.isNotBlank(search.trim()))
                .like(StringUtils.isNotBlank(search),"name",search.trim());
        IPage<SysUser> resPage=this.page(iPage,wrapper);

        //获取用户所属的部门、用户的岗位信息
        SysDept dept;
        for (SysUser user:resPage.getRecords()){
            try {
                dept=sysDeptService.getById(user.getDeptId());
                user.setDeptName((dept!=null && StringUtils.isNotBlank(dept.getName()))? dept.getName() : "");

                String postName=sysUserPostService.getPostNameByUserId(user.getUserId());
                user.setPostName(postName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new PageUtil(resPage);
    }

    //保存用户
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUser sysUser) {
        if (this.getOne(new QueryWrapper<SysUser>().eq("username",sysUser.getUsername())) !=null ){
            throw new RuntimeException("用户名已存在!");
        }

        sysUser.setCreateTime(new Date());

        //加密密码串
        String salt= RandomStringUtils.randomAlphanumeric(20);
        String password= ShiroUtil.sha256(sysUser.getPassword(),salt);
        sysUser.setPassword(password);
        sysUser.setSalt(salt);

//        System.out.println(sysUserMapper.insert(sysUser)+"==========");
//        System.out.println(sysUser.toString());

        this.save(sysUser);

        SysUser sysUser1=this.getOne(new QueryWrapper<SysUser>().eq("username",sysUser.getUsername()));
            //维护好用户~角色的关联关系
            sysUserRoleService.saveOrUpdate(sysUser1.getUserId(),sysUser.getRoleIdList());
        try{
            //维护好用户~岗位的关联关系
            sysUserPostService.saveOrUpdate(sysUser1.getUserId(),sysUser.getPostIdList());
        }catch (Exception e){
            System.out.println("2222222222222222222222222222222222222222");
        }
    }

    @Override
    public SysUser getInfo(Long userId) {
        SysUser sysUser=this.getById(userId);


//        System.out.println(sysUser.toString());

        //获取用户分配的角色关联信息
        List<Long> roleIds=sysUserRoleService.queryRoleIdList(userId);
        sysUser.setRoleIdList(roleIds);

        //获取用户分配的岗位关联信息
        List<Long> postIds=sysUserPostService.queryPostIdList(userId);
        sysUser.setPostIdList(postIds);

        return sysUser;
    }

    //修改
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUser sysUser) {
        SysUser old=this.getById(sysUser.getUserId());
        if (old==null){
            return;
        }
        if (!old.getUsername().equals(sysUser.getUsername())){
            if (this.getOne(new QueryWrapper<SysUser>().eq("username",sysUser.getUsername())) !=null ){
                throw new RuntimeException("修改后的用户名已存在!");
            }
        }


        if (StringUtils.isNotBlank(sysUser.getPassword())){
            String password=ShiroUtil.sha256(sysUser.getPassword(),old.getSalt());
            sysUser.setPassword(password);

            /*String newSalt=RandomStringUtils.randomAlphanumeric(20);
            String password=ShiroUtil.sha256(entity.getPassword(),newSalt);
            entity.setPassword(password);
            entity.setSalt(newSalt);*/
        }
        this.updateById(sysUser);

        //维护好用户~角色的关联关系
        sysUserRoleService.saveOrUpdate(sysUser.getUserId(),sysUser.getRoleIdList());

        //维护好用户~岗位的关联关系
        sysUserPostService.saveOrUpdate(sysUser.getUserId(),sysUser.getPostIdList());
    }

    //删除用户：除了删除 用户本身 信息之外，还需要删除用户~角色、用户~岗位 关联关系信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long[] ids) {
        if (ids!=null && ids.length>0){
            List<Long> userIds= Arrays.asList(ids);

            this.removeByIds(userIds);

            /*for (Long uId:userIds){
                sysUserRoleService.remove(new QueryWrapper<SysUserRoleEntity>().eq("user_id",uId));
                sysUserPostService.remove(new QueryWrapper<SysUserPostEntity>().eq("user_id",uId));
            }*/

            //java8的写法
            userIds.stream().forEach(uId -> sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id",uId)));
            userIds.stream().forEach(uId -> sysUserPostService.remove(new QueryWrapper<SysUserPost>().eq("user_id",uId)));
        }
    }

    //重置密码
    @Override
    public void updatePsd(Long[] ids) {
        if (ids!=null && ids.length>0){
            SysUser sysUser;
            for (Long uId:ids){
                sysUser=new SysUser();

                String salt=RandomStringUtils.randomAlphanumeric(20);
                String newPsd=ShiroUtil.sha256(Constant.DefaultPassword,salt);
                sysUser.setPassword(newPsd);
                sysUser.setSalt(salt);

                this.update(sysUser,new QueryWrapper<SysUser>().eq("user_id",uId));
            }
        }
    }
}
