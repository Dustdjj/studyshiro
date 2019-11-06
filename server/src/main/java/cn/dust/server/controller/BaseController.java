package cn.dust.server.controller;

import cn.dust.common.response.BaseResponse;
import cn.dust.common.response.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: dust
 * @Date: 2019/10/11 15:08
 */
@Controller
@RequestMapping("/base")
public class BaseController {

    private static final Logger log= LoggerFactory.getLogger(BaseController.class);

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public BaseResponse<String> info(String name){
        BaseResponse<String> response=new BaseResponse<>(StatusCode.Success);
        if(StringUtils.isEmpty(name)){
            name="我真帅";
        }

        response.setData(name);
        return response;
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public String  page(String name, ModelMap modelMap){

        if(StringUtils.isEmpty(name)){
            name="dddd";
        }
        modelMap.put("name",name);
        modelMap.put("app","权限管理系统");
        return "pageOne";
    }



}
