package cn.dust.server.service.impl;

import cn.dust.common.utils.CommonUtil;
import cn.dust.model.entity.SysUserRole;
import cn.dust.model.mapper.SysUserRoleMapper;
import cn.dust.server.service.SysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: dust
 * @Date: 2019/10/17 21:17
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    public void deleteBatch(List<Long> roleIds) {
        if (roleIds!=null && !roleIds.isEmpty()){
            String delIds= Joiner.on(",").join(roleIds);
            baseMapper.deleteBatch(CommonUtil.concatStrToInt(delIds,","));
        }
    }

    //维护用户~角色的关联关系
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long userId, List<Long> roleIds) {
        //需要先清除旧的关联数据，再插入新的关联信息
        this.remove(new QueryWrapper<SysUserRole>().eq("user_id",userId));

        if (roleIds!=null && !roleIds.isEmpty()){
            SysUserRole sysUserRole;
            for (Long rId:roleIds){
                sysUserRole=new SysUserRole();
                sysUserRole.setRoleId(rId);
                sysUserRole.setUserId(userId);
                this.save(sysUserRole);
            }
        }
    }

    //获取分配给用户的角色id列表
    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }
}
