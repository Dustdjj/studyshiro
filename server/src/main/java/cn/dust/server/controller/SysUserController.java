package cn.dust.server.controller;

import cn.dust.common.response.BaseResponse;
import cn.dust.common.response.StatusCode;
import cn.dust.common.utils.Constant;
import cn.dust.common.utils.PageUtil;
import cn.dust.common.utils.ValidatorUtil;
import cn.dust.model.entity.SysUser;
import cn.dust.server.annotation.LogAnnotation;
import cn.dust.server.service.SysUserService;
import cn.dust.server.shiro.ShiroUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static cn.dust.server.shiro.ShiroUtil.getUserId;

/**
 * @Author: dust
 * @Date: 2019/10/12 17:32
 */
@RestController
@RequestMapping("sys/user")
@Slf4j
public class SysUserController extends AbstractController{

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public BaseResponse currInfo(){
        Map<String,Object> resMap= Maps.newHashMap();
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try{
            resMap.put("user",getCurrLoginUser());
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        response.setData(resMap);
        return response;
    }

    //修改登录密码
    @RequestMapping("/password")
    @LogAnnotation("修改登录密码")
    public BaseResponse updatePassword(String password,String newPassword){
        if (StringUtils.isBlank(password) || StringUtils.isBlank(newPassword)){
            return new BaseResponse(StatusCode.PasswordCanNotBlank);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);

        try {
            //真正的处理逻辑：先校验旧密码输入是否正确，再更新新的密码
            SysUser sysUser=getCurrLoginUser();
            final String salt=sysUser.getSalt();

            String oldPsd= ShiroUtil.sha256(password,salt);
            if (!sysUser.getPassword().equals(oldPsd)){
                return new BaseResponse(StatusCode.OldPasswordNotMatch);
            }
            String newPsd=ShiroUtil.sha256(newPassword,salt);

            //执行更新密码的逻辑
            log.info("~~~~旧密码正确，开始更新新的密码~~~~");

            sysUserService.updatePassword(sysUser.getUserId(),oldPsd,newPsd);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.UpdatePasswordFail);
        }

        return response;
    }


    //分页列表模糊查询
    @RequestMapping("/list")
    @RequiresPermissions(value = {"sys:user:list"})
    public BaseResponse list(@RequestParam Map<String,Object> paramMap){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap= Maps.newHashMap();
        try {
            log.info("用户模块~分页列表模糊查询：{}",paramMap);

            PageUtil page=sysUserService.queryPage(paramMap);
            resMap.put("page",page);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }


    //新增
    @LogAnnotation("新增用户")
    @RequestMapping("/save")
    @RequiresPermissions(value = {"sys:user:save"})
    public BaseResponse save(@RequestBody @Validated SysUser sysUser, BindingResult result){
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        if (StringUtils.isBlank(sysUser.getPassword())){
            return new BaseResponse(StatusCode.PasswordCanNotBlank);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            sysUserService.saveUser(sysUser);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //获取详情
    @RequestMapping("/info/{userId}")
    @RequiresPermissions(("sys:user:list"))
    public BaseResponse info(@PathVariable Long userId){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap=Maps.newHashMap();
        try {
            log.info("用户模块~获取详情：{}",userId);

            resMap.put("user",sysUserService.getInfo(userId));
            response.setData(resMap);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.UpdatePasswordFail);
        }
        return response;
    }


    //修改
    @LogAnnotation("修改用户")
    @RequestMapping("/update")
    @RequiresPermissions(value = {"sys:user:update"})
    public BaseResponse update(@RequestBody @Validated SysUser sysUser,BindingResult result){
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("用户模块~修改用户：{}",sysUser);

            sysUserService.updateUser(sysUser);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


    //删除
    @LogAnnotation("删除用户")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public BaseResponse delete(@RequestBody Long[] ids){
        if (ids==null || ids.length<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        //超级管理员~admin不能删除；当前登录用户不能删
        //if (Arrays.asList(ids).contains(Constant.SUPER_ADMIN)){
        if (ArrayUtils.contains(ids, Constant.SUPER_ADMIN)){
            return new BaseResponse(StatusCode.SysUserCanNotBeDelete);
        }
        if (ArrayUtils.contains(ids,getUserId())){
            return new BaseResponse(StatusCode.CurrUserCanNotBeDelete);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            sysUserService.deleteUser(ids);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.UpdatePasswordFail);
        }
        return response;
    }


    //重置密码
    @LogAnnotation("重置用户密码")
    @RequestMapping("/psd/reset")
    @RequiresPermissions("sys:user:resetPsd")
    public BaseResponse restPsd(@RequestBody Long[] ids){
        if (ids==null || ids.length<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        //超级管理员~admin不能删除；当前登录用户不能删
        if (ArrayUtils.contains(ids,Constant.SUPER_ADMIN) || ArrayUtils.contains(ids,getUserId())){
            return new BaseResponse(StatusCode.SysUserAndCurrUserCanNotResetPsd);
        }

        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            sysUserService.updatePsd(ids);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.UpdatePasswordFail);
        }
        return response;
    }





}
