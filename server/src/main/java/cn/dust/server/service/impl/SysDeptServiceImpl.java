package cn.dust.server.service.impl;

import cn.dust.common.utils.Constant;
import cn.dust.model.entity.SysDept;
import cn.dust.model.mapper.SysDeptMapper;
import cn.dust.server.service.CommonDataService;
import cn.dust.server.service.SysDeptService;
import cn.dust.server.shiro.ShiroUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/14 19:49
 */
@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private static final Logger log= LoggerFactory.getLogger(SysDeptServiceImpl.class);


    @Autowired
    private CommonDataService commonDataService;

    @Override
    public List<SysDept> queryAll(Map<String, Object> map) {
//        return baseMapper.queryList(map);

        if(ShiroUtil.getUserId()!= Constant.SUPER_ADMIN){
           String depetDataIds= commonDataService.getCurrUserDataDeptIdsStr();
            map.put("deptDataIds",(StringUtils.isNotBlank(depetDataIds))?depetDataIds:null);
        }
       return baseMapper.queryListV2(map);
    }

    //根据父级Id
   @Override
    public List<Long> queryDeptIds(Long parentId) {
        return baseMapper.queryDeptIds(parentId);
    }



    //获取当前部门的子部门id列表
    @Override
    public List<Long> getSubDeptIdList(Long deptId) {
        List<Long> deptIdList= Lists.newLinkedList();

        //第一级部门Id列表
        List<Long> subIdList=baseMapper.queryDeptIds(deptId);
        getDeptTreeList(subIdList,deptIdList);

        return deptIdList;
    }

    /**
     * 递归
     * @param subIdList 第一级部门数据Id列表
     * @param deptIdList 每次递归时循环存储的结果数据Id列表
     */
    private void getDeptTreeList(List<Long> subIdList,List<Long> deptIdList){
        List<Long> list;
        for (Long subId:subIdList){
            list=baseMapper.queryDeptIds(subId);
            if (list!=null && !list.isEmpty()){
                //调用递归之处
                getDeptTreeList(list,deptIdList);
            }

            //执行到这里时，就表示当前递归结束
            deptIdList.add(subId);
        }
    }


}
