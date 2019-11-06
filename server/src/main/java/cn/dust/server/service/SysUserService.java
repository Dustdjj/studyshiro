package cn.dust.server.service;

import cn.dust.common.utils.PageUtil;
import cn.dust.model.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/13 11:29
 */
public interface SysUserService extends IService<SysUser> {

    //修改密码
    boolean updatePassword(Long userId,String oldPassword,String newPassword);

    PageUtil queryPage(Map<String,Object> map);

    void saveUser(SysUser sysUser);

    SysUser getInfo(Long userId);

    void updateUser(SysUser sysUser);

    void deleteUser(Long[] ids);

    void updatePsd(Long[] ids);
}
