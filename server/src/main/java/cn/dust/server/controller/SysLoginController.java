package cn.dust.server.controller;

import cn.dust.common.response.BaseResponse;
import cn.dust.common.response.StatusCode;
import cn.dust.server.shiro.ShiroUtil;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * @Author: dust
 * @Date: 2019/10/11 20:53
 */
@Controller
public class SysLoginController extends AbstractController{

    @Autowired
    private Producer producer;
    /**
     * 生成验证码
     * @param response
     * @throws Exception
     */
    @RequestMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtil.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        System.out.println("验证码："+text);
    }

    /**
     * 登录
     * @param username
     * @param password
     * @param captcha
     * @return
     */
    @RequestMapping(value = "/sys/login",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse login(String username,String password,String captcha){
        logger.info("用户名：{}，密码：{}，验证码：{}",username,password,captcha);

        /**
         * 校验验证码
         */
        String key=ShiroUtil.getKaptcha(Constants.KAPTCHA_SESSION_KEY);

        if(!key.equals(captcha)){
            return new BaseResponse(StatusCode.InvalidCode);
        }

        try{
            Subject subject= SecurityUtils.getSubject();
            if(!subject.isAuthenticated()){
                UsernamePasswordToken token=new UsernamePasswordToken(username,password);
//                System.out.println("username="+username+"   ppass"+password);
//                System.out.println("tokenUsername"+token.getUsername()+"      +tokenpasss"+String.valueOf(token.getPassword()));
                subject.login(token);
            }

        }catch (UnknownAccountException e) {
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }catch (IncorrectCredentialsException e) {
            return new BaseResponse(StatusCode.AccountPasswordNotMatch);
        }catch (LockedAccountException e) {
            return new BaseResponse(StatusCode.AccountHasBeenLocked);
        }catch (AuthenticationException e) {
            return new BaseResponse(StatusCode.AccountValidateFail);
        }

        return new BaseResponse(StatusCode.Success);
    }

    /**
     * 登出
     * @return
     */
    @RequestMapping(value = "logout",method = RequestMethod.GET)
    public String logout(){
        //销毁当前的shiro的用户session
        ShiroUtil.logout();
        return "redirect:login.html";
    }


}
