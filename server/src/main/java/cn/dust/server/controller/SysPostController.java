package cn.dust.server.controller;

import cn.dust.common.response.BaseResponse;
import cn.dust.common.response.StatusCode;
import cn.dust.common.utils.PageUtil;
import cn.dust.common.utils.ValidatorUtil;
import cn.dust.model.entity.SysPost;

import cn.dust.server.annotation.LogAnnotation;
import cn.dust.server.service.SysPostService;
import com.google.common.collect.Maps;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/13 15:21
 */
@RestController
@RequestMapping("/sys/post")
@Slf4j
public class SysPostController extends AbstractController{


    @Autowired
    private SysPostService sysPostService;

    /**
     * 列表分页模糊查询
     * @param paramMap
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:post:list")
    public BaseResponse list(@RequestParam Map<String,Object> paramMap){

        BaseResponse response=new BaseResponse(StatusCode.Success);

        Map<String,Object>resMap= Maps.newHashMap();

        try{

            PageUtil page=sysPostService.queryPage(paramMap);

            resMap.put("page",page);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail);
        }
        response.setData(resMap);
        return response;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiresPermissions("sys:post:save")
    public BaseResponse save(@RequestBody SysPost sysPost, BindingResult result ){

        String res= ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }

//        if (StringUtils.isBlank(sysPost.getPostCode(){
//            return new BaseResponse(StatusCode.InvalidParams.getCode(),"岗位编码不能为空");
//        }
//
//        if( StringUtils.isBlank(sysPost.getPostName())){
//            return new BaseResponse(StatusCode.InvalidParams.getCode(),"岗位名称不能为空");
//        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try{
        log.info("新增岗位接收到的数据:{}",sysPost);

        sysPostService.savePost(sysPost);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
            return response;
    }

    //详情
    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    @RequiresPermissions("sys:post:info")
    public BaseResponse info(@PathVariable Long id){

        if(id==null || id<0){
            return new BaseResponse(StatusCode.InvalidParams);
        }

        BaseResponse response=new BaseResponse(StatusCode.Success);

        Map<String,Object>resMap=Maps.newHashMap();

        try{
            log.info("岗位详情~接收到的数据:{}",id);
            resMap.put("post",sysPostService.getById(id));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    //修改
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @RequiresPermissions("sys:post:update")
    public BaseResponse update(@RequestBody @Validated SysPost sysPost, BindingResult result ){
        String res= ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }

        if(sysPost.getPostId()==null||sysPost.getPostId()<0){
            return new BaseResponse(StatusCode.InvalidParams);
        }

        BaseResponse response=new BaseResponse(StatusCode.Success);
        try{
            log.info("新增岗位接收到的数据:{}",sysPost);

            sysPostService.savePost(sysPost);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //删除
    @LogAnnotation("删除岗位")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @RequiresPermissions("sys:post:delete")
    public BaseResponse delete(@RequestBody Long[] ids){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("删除岗位~接收到数据：{}", Arrays.asList(ids));
            sysPostService.deletePatch(ids);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }

        return response;
    }

    //岗位列表-select
    @RequestMapping("/select")
    public BaseResponse select(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("岗位列表~select..");

            Map<String,Object> resMap=Maps.newHashMap();
            resMap.put("list",sysPostService.list());

            response.setData(resMap);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


}
