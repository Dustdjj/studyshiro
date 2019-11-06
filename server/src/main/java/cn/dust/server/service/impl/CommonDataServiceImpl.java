package cn.dust.server.service.impl;/**
 * Created by Administrator on 2019/8/5.
 */

import cn.dust.common.utils.CommonUtil;
import cn.dust.common.utils.Constant;
import cn.dust.model.entity.SysUser;
import cn.dust.model.mapper.SysDeptMapper;
import cn.dust.model.mapper.SysUserMapper;
import cn.dust.server.service.CommonDataService;
import cn.dust.server.service.SysDeptService;
import cn.dust.server.shiro.ShiroUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 通用化的部门数据权限控制service
 * @Author:debug (SteadyJack)
 * @Date: 2019/8/5 10:02
 **/
@Service
public class CommonDataServiceImpl implements CommonDataService {

    private static final Logger log= LoggerFactory.getLogger(CommonDataServiceImpl.class);

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysDeptService sysDeptService;


    //获取当前登录用户的部门数据Id列表
    @Override
    public Set<Long> getCurrUserDataDeptIds() {
        Set<Long> dataIds= Sets.newHashSet();

        SysUser currUser= ShiroUtil.getUserEntity();
        if (Constant.SUPER_ADMIN == currUser.getUserId()){
            dataIds=sysDeptMapper.queryAllDeptIds();
        }else{
            //分配给用户的部门数据权限id列表
            Set<Long> userDeptDataIds=sysUserMapper.queryDeptIdsByUserId(currUser.getUserId());
            if (userDeptDataIds!=null && !userDeptDataIds.isEmpty()){
                dataIds.addAll(userDeptDataIds);
            }

            //用户所在的部门及其子部门Id列表 ~ 递归实现
            dataIds.add(currUser.getDeptId());

            List<Long> subDeptIdList=sysDeptService.getSubDeptIdList(currUser.getDeptId());
            dataIds.addAll(Sets.newHashSet(subDeptIdList));
        }
        return dataIds;
    }

    //将 部门数据Id列表 转化为 id拼接 的字符串
    @Override
    public String getCurrUserDataDeptIdsStr() {
        String result=null;

        Set<Long> dataSet=this.getCurrUserDataDeptIds();
        if (dataSet!=null && !dataSet.isEmpty()){
            result= CommonUtil.concatStrToInt(Joiner.on(",").join(dataSet),",");
        }

        return result;
    }
}



































