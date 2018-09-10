package top.vetoer.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.vetoer.comm.Const;
import top.vetoer.comm.aop.LoggerManage;
import top.vetoer.domain.User;
import top.vetoer.domain.result.ExceptionMsg;
import top.vetoer.domain.result.Response;
import top.vetoer.domain.result.ResponseData;
import top.vetoer.repository.UserRepository;
import top.vetoer.utils.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    UserRepository userRepository;


    @PostMapping("/login")
    @LoggerManage(description = "登录")
    public ResponseData login(User user, HttpServletResponse response) throws ServletException {
        try{
            User loginUser = userRepository.findByUsername(user.getUsername());
            if(loginUser == null){
                return new ResponseData(ExceptionMsg.LoginNameNotExists);
            }else if(!loginUser.getPassword().equals(getPwd(user.getPassword()))){
                return new ResponseData(ExceptionMsg.LoginNameOrPassWordError);
            }
            Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY,cookieSign(loginUser.getId().toString()));
            cookie.setMaxAge(Const.COOKIE_TIMEOUT);
            cookie.setPath("/");
            response.addCookie(cookie);
            getSession().setAttribute(Const.LOGIN_SESSION_KEY,loginUser);
            String preUrl = "/";
            if(null != getSession().getAttribute(Const.LAST_REFERER)){
                preUrl = String.valueOf(getSession().getAttribute(Const.LAST_REFERER));
            }
            return new ResponseData(ExceptionMsg.SUCCESS,preUrl);

        }catch (Exception e){
            logger.error("login failed,",e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }
    @PostMapping("/register")
    @LoggerManage(description = "注册")
    public Response register(User user) throws ServletException{
        try{
            User registUser = userRepository.findByUsername(user.getUsername());
            if(null != registUser){
                return result(ExceptionMsg.UserNameUsed);
            }
            user.setPassword(getPwd(user.getPassword()));
            user.setCreateTime(DateUtils.getCurrentTime());
            user.setLastModifyTime(DateUtils.getCurrentTime());
            userRepository.save(user);
            logger.info(user.getUsername() + " regist success!");
            getSession().setAttribute(Const.LOGIN_SESSION_KEY,user);
        }catch (Exception e){
            logger.error("create user failed,",e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    @GetMapping("/info")
    public String getInfo(){
        String x = "hello ";
        return x;
    }
}
