package top.vetoer.web;

import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.vetoer.comm.Const;
import top.vetoer.comm.aop.LoggerManage;
import top.vetoer.domain.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController{

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @LoggerManage(description = "首页转发")
    public String redirect(){
        return "redirect:/timeline";
    }

    @RequestMapping(value="/timeline",method=RequestMethod.GET)
    @LoggerManage(description="首页")
    public String timeline(Model model){
        User user = super.getUser();
        if(null != user){
            model.addAttribute("user",user);
        }
        return "timeline";
    }


    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @LoggerManage(description = "登出")
    public String logout(HttpServletResponse response,Model model){
        getSession().removeAttribute(Const.LOGIN_SESSION_KEY);
        getSession().removeAttribute(Const.LAST_REFERER);
        Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY,"");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/timeline";
    }

}
