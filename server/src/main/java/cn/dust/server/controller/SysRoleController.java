package cn.dust.server.controller;

import cn.dust.common.response.BaseResponse;
import cn.dust.common.response.StatusCode;
import cn.dust.common.utils.PageUtil;
import cn.dust.common.utils.ValidatorUtil;
import cn.dust.model.entity.SysRole;
import cn.dust.server.annotation.LogAnnotation;
import cn.dust.server.service.SysRoleDeptService;
import cn.dust.server.service.SysRoleMenuService;
import cn.dust.server.service.SysRoleService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/17 12:58
 */
@RestController
@RequestMapping("sys/role")
@Slf4j
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleDeptService sysRoleDeptService;


    //分页列表模糊查询
    @RequestMapping("/list")
    @RequiresPermissions("sys:role:list")
    public BaseResponse list(@RequestParam Map<String,Object> paramMap){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            Map<String,Object> resMap= Maps.newHashMap();

            PageUtil page=sysRoleService.queryPage(paramMap);
            resMap.put("page",page);

            response.setData(resMap);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


    //新增
    @LogAnnotation("新增角色")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions("sys:role:save")
    public BaseResponse save(@RequestBody @Validated SysRole sysRole, BindingResult result) {
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("新增角色~接收到数据：{}",sysRole);

            sysRoleService.saveRole(sysRole);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }


    //获取详情
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:role:info")
    public BaseResponse info(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String, Object> resMap = Maps.newHashMap();
        try {
            SysRole role=sysRoleService.getById(id);

            //获取角色对应的菜单列表
            List<Long> menuIdList=sysRoleMenuService.queryMenuIdList(id);
            role.setMenuIdList(menuIdList);

            //获取角色对应的部门列表
            List<Long> deptIdList=sysRoleDeptService.queryDeptIdList(id);
            role.setDeptIdList(deptIdList);

            resMap.put("role",role);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    //修改
    @LogAnnotation("修改角色")
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("sys:role:update")
    public BaseResponse update(@RequestBody @Validated SysRole sysRole, BindingResult result) {
        String res=ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("修改角色~接收到数据：{}",sysRole);

            sysRoleService.updateRole(sysRole);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;

    }



    //删除
    @LogAnnotation("删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("sys:role:delete")
    public BaseResponse delete(@RequestBody Long[] ids) {
        if (ids==null || ids.length<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("删除角色~接收到数据：{}",ids);

            sysRoleService.deleteBatch(ids);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;

    }

    //角色列表-select
    @RequestMapping("/select")
    public BaseResponse select(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("角色列表~select..");

            Map<String,Object> resMap=Maps.newHashMap();
            resMap.put("list",sysRoleService.list());

            response.setData(resMap);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


}
