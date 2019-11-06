package cn.dust.server.controller;

import cn.dust.common.response.BaseResponse;
import cn.dust.common.response.StatusCode;
import cn.dust.common.utils.Constant;
import cn.dust.model.entity.SysMenu;
import cn.dust.server.annotation.LogAnnotation;
import cn.dust.server.service.SysMenuService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/16 10:52
 */
@RestController
@RequestMapping("sys/menu")
@Slf4j
public class SysMenuController extends AbstractController{

    @Autowired
    private SysMenuService sysMenuService;

//菜单列表
    @RequestMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public List<SysMenu> list(){
        //第一种写法~借助mybatis-plus serviceImpl实现
//        List<SysMenuEntity> list=sysMenuService.list();
//
//        if (list!=null && !list.isEmpty()){
//            list.stream() .forEach(entity -> {
//                /*if (Constant.MenuType.BUTTON.getValue() == entity.getType() ){
//                    SysMenuEntity menu=sysMenuService.getById(entity.getParentId());
//                    entity.setParentName( (menu!=null && StringUtils.isNotBlank(menu.getName()))? menu.getName() : "");
//                }*/
//
//                SysMenuEntity menu=sysMenuService.getById(entity.getParentId());
//                entity.setParentName( (menu!=null && StringUtils.isNotBlank(menu.getName()))? menu.getName() : "");
//            });
//        }
//
//        return list;
        //第二种方式~自己写sql
        return sysMenuService.queryAll();
    }

    //获取树形层级列表数据
    @RequestMapping("/select")
    public BaseResponse select(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap= Maps.newHashMap();
        try {
            List<SysMenu> list=sysMenuService.queryNotButtonList();

            SysMenu root=new SysMenu();
            root.setMenuId(Constant.TOP_MENU_ID);
            root.setName(Constant.TOP_MENU_NAME);
            root.setParentId(-1L);
            root.setOpen(true);
            list.add(root);

            resMap.put("menuList",list);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }

        response.setData(resMap);
        return response;
    }

    //新增
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiresPermissions("sys:menu:save")
    public BaseResponse save(@RequestBody @Validated SysMenu sysMenu){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("新增菜单~接收到数据：{}",sysMenu);

            String result=this.validateForm(sysMenu);
            if (StringUtils.isNotBlank(result)){
                return new BaseResponse(StatusCode.Fail.getCode(),result);
            }
            sysMenuService.save(sysMenu);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //验证参数是否正确
    private String validateForm(SysMenu menu) {
        if (StringUtils.isBlank(menu.getName())) {
            return "菜单名称不能为空";
        }
        if (menu.getParentId() == null) {
            return "上级菜单不能为空";
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                return "菜单链接url不能为空";
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();

        if (menu.getParentId() != 0) {
            SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() || menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                return "上级菜单只能为目录类型";
            }
            return "";
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                return "上级菜单只能为菜单类型";
            }
            return "";
        }

        return "";
    }


    //获取菜单详情
    @RequestMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public BaseResponse info(@PathVariable Long menuId){
        if (menuId==null || menuId<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }

        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap= Maps.newHashMap();
        try {
            resMap.put("menu",sysMenuService.getById(menuId));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }

        response.setData(resMap);
        return response;
    }


    //修改
    @LogAnnotation("修改菜单")
    @RequestMapping(value = "/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("sys:menu:update")
    public BaseResponse update(@RequestBody SysMenu sysMenu){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("修改菜单~接收到数据：{}",sysMenu);

            String result=this.validateForm(sysMenu);
            if (StringUtils.isNotBlank(result)){
                return new BaseResponse(StatusCode.Fail.getCode(),result);
            }

            sysMenuService.updateById(sysMenu);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //删除
    @LogAnnotation("删除菜单")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @RequiresPermissions("sys:menu:delete")
    public BaseResponse delete(Long menuId){
        if (menuId==null || menuId<=0 ){
            return new BaseResponse(StatusCode.InvalidParams);
        }

        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("删除菜单~接收到数据：{}",menuId);

            SysMenu sysMenu=sysMenuService.getById(menuId);
            if (sysMenu==null){
                return new BaseResponse(StatusCode.InvalidParams);
            }

            List<SysMenu> list=sysMenuService.queryByParentId(sysMenu.getMenuId());
            if (list!=null && !list.isEmpty()){
                return new BaseResponse(StatusCode.MenuHasSubMenuListCanNotDelete);
            }

            sysMenuService.delete(menuId);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }

        return response;
    }



}
