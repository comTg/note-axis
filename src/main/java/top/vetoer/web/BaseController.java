package top.vetoer.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.vetoer.comm.Const;
import top.vetoer.domain.User;
import top.vetoer.domain.result.ExceptionMsg;
import top.vetoer.domain.result.Response;
import top.vetoer.utils.Des3EncryptionUtil;
import top.vetoer.utils.MD5Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class BaseController {

    protected Logger logger =  LoggerFactory.getLogger(this.getClass());

    protected Response result(ExceptionMsg msg){
        return new Response(msg);
    }
    protected Response result(){
        return new Response();
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    protected User getUser() {
        return (User) getSession().getAttribute(Const.LOGIN_SESSION_KEY);
    }

    protected long getUserId() {
        Long id=0l;
        User user=getUser();
        if(user!=null){
            id=user.getId();
        }
        return id;
    }

    protected String getUserName() {
        String userName="test";
        User user=getUser();
        if(user!=null){
            userName=user.getUsername();
        }
        return userName;
    }

    protected String getUserIp() {
        String value = getRequest().getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(value) && !"unknown".equalsIgnoreCase(value)) {
            return value;
        } else {
            return getRequest().getRemoteAddr();
        }
    }

    protected String getPwd(String password){
        try {
            String pwd = MD5Util.encrypt(password+Const.PASSWORD_KEY);
            return pwd;
        } catch (Exception e) {
            logger.error("密码加密异常：",e);
        }
        return null;
    }

    protected String cookieSign(String value){
        try{
            value = value + Const.PASSWORD_KEY;
            String sign = Des3EncryptionUtil.encode(Const.DES3_KEY,value);
            return sign;
        }catch (Exception e){
            logger.error("cookie签名异常：",e);
        }
        return null;
    }
}

