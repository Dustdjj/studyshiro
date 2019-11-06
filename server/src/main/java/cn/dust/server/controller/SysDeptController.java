package cn.dust.server.controller;


import cn.dust.common.response.BaseResponse;
import cn.dust.common.response.StatusCode;
import cn.dust.common.utils.Constant;
import cn.dust.common.utils.ValidatorUtil;
import cn.dust.model.entity.SysDept;
import cn.dust.server.annotation.LogAnnotation;
import cn.dust.server.service.SysDeptService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static cn.dust.server.shiro.ShiroUtil.getUserId;

/**
 * @Author: dust
 * @Date: 2019/10/14 19:44
 */
@RestController
@Slf4j
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController{

    @Autowired
    private SysDeptService sysDeptService;

    //获取一级部门/顶级部门的deptId
    @RequestMapping("/info")
    public BaseResponse info(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap=Maps.newHashMap();
        Long deptId=0L;

        try {
            //数据视野决定的顶级部门id可能不是0
            if (getUserId() != Constant.SUPER_ADMIN){
                //涉及到数据视野的问题

                /*List<SysDeptEntity> list = sysDeptService.queryAll(new HashMap<String, Object>());
                Long pId = null;
                for(SysDeptEntity dept : list){
                    if(pId == null){
                        pId = dept.getParentId();
                        continue;
                    }

                    if(pId > dept.getParentId().longValue()){
                        pId = dept.getParentId();
                    }
                }
                deptId = pId;*/
            }
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        resMap.put("deptId",deptId);
        response.setData(resMap);
        return response;
    }

    @RequestMapping(value = "/list")
    @RequiresPermissions("sys:dept:list")
    public List<SysDept> list(){
        return sysDeptService.queryAll(Maps.newHashMap());
    }

    @RequestMapping("/select")
    public BaseResponse select(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap=Maps.newHashMap();

        List<SysDept>deptList= Lists.newLinkedList();
        try {

            deptList=sysDeptService.queryAll(Maps.newHashMap());

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        resMap.put("deptList",deptList);

        response.setData(resMap);

        return response;
    }

    //新增
    @LogAnnotation("新增部门")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiresPermissions("sys:dept:save")
    public BaseResponse save(@RequestBody @Validated SysDept sysDept, BindingResult result){
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("新增部门~接收到数据：{}",sysDept);

            sysDept.setDelFlag(0);
            sysDeptService.save(sysDept);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;

    }


    //详情
    @RequestMapping("/detail/{deptId}")
    @RequiresPermissions("sys:dept:info")
    public BaseResponse detail(@PathVariable Long deptId){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap=Maps.newHashMap();
        try {
            resMap.put("dept",sysDeptService.getById(deptId));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);

        return response;
    }

    //修改
    @LogAnnotation("修改部门")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @RequiresPermissions("sys:dept:update")
    public BaseResponse update(@RequestBody @Validated SysDept sysDept, BindingResult result){
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        if (sysDept.getDeptId()==null || sysDept.getDeptId()<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("修改部门~接收到数据：{}",sysDept);

            sysDeptService.updateById(sysDept);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;

    }

    //删除
    @LogAnnotation("删除部门")
    @RequestMapping(value = "/delete")
    @RequiresPermissions("sys:dept:delete")
    public BaseResponse delete(Long deptId){
        if (deptId==null || deptId<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("删除部门~接收到数据：{}",deptId);

            //如果当前部门有子部门，则需要要求先删除下面的所有子部门，再删除当前部门
            List<Long> subIds=sysDeptService.queryDeptIds(deptId);
            if (subIds!=null && !subIds.isEmpty()){
                return new BaseResponse(StatusCode.DeptHasSubDeptCanNotBeDelete);
            }

            sysDeptService.removeById(deptId);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;

    }

}
