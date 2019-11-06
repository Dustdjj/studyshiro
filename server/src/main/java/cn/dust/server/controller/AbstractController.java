package cn.dust.server.controller;

import cn.dust.model.entity.SysUser;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * @Author: dust
 * @Date: 2019/10/11 20:55
 */
@Controller
public abstract class AbstractController {

    protected Logger logger= LoggerFactory.getLogger(getClass());

    protected SysUser getCurrLoginUser(){
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }

    protected Long getCurrLoginUserId(){
        return getCurrLoginUser().getUserId();
    }

    protected String getCurrLoginUserName(){
        return getCurrLoginUser().getUsername();
    }

    protected Long getCurrLoginUserDeptId(){
        return getCurrLoginUser().getDeptId();
    }

}
