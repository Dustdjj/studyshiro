package cn.dust.server.service.impl;

import cn.dust.common.utils.CommonUtil;
import cn.dust.model.entity.SysRoleDept;
import cn.dust.model.mapper.SysRoleDeptMapper;
import cn.dust.server.service.SysRoleDeptService;
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
 * @Date: 2019/10/17 13:51
 */
@Service("sysRoleDeptService")
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptMapper, SysRoleDept> implements SysRoleDeptService{
    private static final Logger log= LoggerFactory.getLogger(SysRoleDeptServiceImpl.class);

    //维护角色~部门关联信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> deptIdList) {
        if(roleId!=null){
            //需要先清除旧的关联数据，再插入新的关联信息
            deleteBatch(Arrays.asList(roleId));
        }

        SysRoleDept sysRoleDept;
        if (deptIdList!=null && !deptIdList.isEmpty()){
            for (Long dId:deptIdList){
                sysRoleDept=new SysRoleDept();
                sysRoleDept.setRoleId(roleId);
                sysRoleDept.setDeptId(dId);
                this.save(sysRoleDept);
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


    //根据角色ID获取部门ID列表
    @Override
    public List<Long> queryDeptIdList(Long roleId) {
        return baseMapper.queryDeptIdListByRoleId(roleId);
    }
}
