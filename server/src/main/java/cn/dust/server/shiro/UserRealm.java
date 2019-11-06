package cn.dust.server.shiro;/**
 * Created by Administrator on 2019/7/30.
 */

import cn.dust.common.utils.Constant;
import cn.dust.model.entity.SysMenu;
import cn.dust.model.entity.SysUser;
import cn.dust.model.mapper.SysMenuMapper;
import cn.dust.model.mapper.SysUserMapper;
import cn.dust.server.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * shiro用于认证用户~授权
 * @Author:debug (SteadyJack)
 * @Date: 2019/7/30 14:33
 **/
@Component
@Slf4j
public class UserRealm extends AuthorizingRealm{

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 资源权限分配~授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SysUser sysUser=(SysUser) principalCollection.getPrimaryPrincipal();
        Long userId=sysUser.getUserId();

        List<String>perms= Lists.newLinkedList();


        //系统超级管理员拥有最高的权限，不需要发出sql的查询，直接拥有所有权限；否则，则需要根据当前用户id去查询权限列表
        if (userId.equals(Constant.SUPER_ADMIN)){
            List<SysMenu> list=sysMenuService.list();
//            System.out.println(list+"ddddddddddddddddddd");
            if (list!=null && !list.isEmpty()){
                perms=list.stream().map(SysMenu::getPerms).collect(Collectors.toList());
//                System.out.println(userId);
//                System.out.println(perms);
            }
        }else{
            perms=sysUserMapper.queryAllPerms(userId);
            System.out.println(perms+"====");
        }


        //对于每一个授权编码进行 , 的解析拆分
        Set<String> stringPermissions= Sets.newHashSet();
        if (perms!=null && !perms.isEmpty()){
            for (String p:perms){
                if (StringUtils.isNotBlank(p)){
                    stringPermissions.addAll(Arrays.asList(StringUtils.split(p.trim(),",")));
                }
            }
        }
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.setStringPermissions(stringPermissions);

        return info;
    }

    /**
     * 用户认证~登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
            protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
                UsernamePasswordToken token=(UsernamePasswordToken)authenticationToken;

                final String userName=token.getUsername();
                final String passWord=String.valueOf(token.getPassword());
                log.info("用户名：{}，密码：{}",userName,passWord);


                SysUser sysUser= sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username",userName));
                if(sysUser==null){
                    //账户不存在
            throw new  UnknownAccountException("账户不存在!");
        }
        if(sysUser.getStatus()==0){
            throw new DisabledAccountException("账户已被禁用，请联系管理员");
        }

//        System.out.println(sysUser.getPassword()+"=========================");

//        if(!sysUser.getPassword().equals(passWord)){
//            throw new IncorrectCredentialsException("账户密码不匹配");
//        }

//        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(sysUser,passWord,getName());

        //使用密码匹配器
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(sysUser,sysUser.getPassword(), ByteSource.Util.bytes(sysUser.getSalt()),getName());
        return info;
    }

    /**
     * 密码匹配器
     * @param credentialsMatcher
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentailsMatcher=new HashedCredentialsMatcher();
        shaCredentailsMatcher.setHashAlgorithmName(ShiroUtil.hashAlgorithmName);
        shaCredentailsMatcher.setHashIterations(ShiroUtil.hashIterations);
        super.setCredentialsMatcher(shaCredentailsMatcher);
    }
}























