package top.vetoer.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.vetoer.comm.Const;
import top.vetoer.domain.User;
import top.vetoer.repository.UserRepository;
import top.vetoer.utils.Des3EncryptionUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ServletInterceptor extends HandlerInterceptorAdapter {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        logger.info(uri);
        if(request.getSession().getAttribute(Const.LOGIN_SESSION_KEY)==null){
            Cookie[] cookies = request.getCookies();
            if(Const.GreenUrlSet.contains(uri)){
                logger.debug("don't check url ,",uri);
                return true;
            }else if(cookies!=null){
                boolean flag = true;
                for(int i = 0;i<cookies.length;i++){
                    Cookie cookie = cookies[i];
                    if(cookie.getName().equals(Const.LOGIN_SESSION_KEY)){
                        if(StringUtils.isNotBlank(cookie.getValue())){
                            flag = false;
                        }else{
                            break;
                        }
                        String value = getUserId(cookie.getValue());
                        Long userId = 0l;
                        if(userRepository==null){
                            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                            userRepository = (UserRepository)factory.getBean("userRepository");
                        }
                        if(StringUtils.isNotBlank(value)){
                            userId = Long.parseLong(value);
                        }
                        User user = userRepository.findById((long)userId);
                        String html = "";
                        if(null == user){
                            html = "<script type=\"text/javascript\">window.location.href=\"_BP_login\"</script>";
                        }else{
                            logger.info("userId: "+ user.getId());
                            request.getSession().setAttribute(Const.LOGIN_SESSION_KEY,user);
                            String referer = this.getRef(request);
                            if(referer.indexOf("/login")>=0 || referer.indexOf("/register")>=0 || referer.indexOf("/error")>=0){
                                return true;
                            }else{
                                html = "<script type=\"text/javascript\">window.location.href=\"_BP_\"</script>";
                            }
                        }
                        html = html.replace("_BP_",Const.BASE_PATH);
                        response.getWriter().write(html);
                        return false;
                    }
                }
                if(flag){
                    // 跳转到登录
                    jumpToLogin(request,response);
                    return false;
                }
            }else{
                jumpToLogin(request,response);
                return false;
            }
        }else{
            return true;
        }
        return super.preHandle(request, response, handler);
    }

    public void jumpToLogin(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String referer = this.getRef(request);
        logger.debug("security filter, deney,"+request.getRequestURI());
        String html = "<script type=\"text/javascript\">window.location.href=\"_BP_login\"</script>";
        html = html.replace("_BP_",Const.BASE_PATH);
        response.getWriter().write(html);
    }

    public  String codeToString(String str) {
        String strString = str;
        try {
            byte tempB[] = strString.getBytes("ISO-8859-1");
            strString = new String(tempB);
            return strString;
        } catch (Exception e) {
            return strString;
        }
    }

    public String getRef(HttpServletRequest request){
        String referer = "";
        String param = this.codeToString(request.getQueryString());
        if(StringUtils.isNotBlank(request.getContextPath())){
            referer = referer + request.getContextPath();
        }
        if(StringUtils.isNotBlank(request.getServletPath())){
            referer = referer + request.getServletPath();
        }
        if(StringUtils.isNotBlank(param)){
            referer = referer + "?" + param;
        }
        request.getSession().setAttribute(Const.LAST_REFERER, referer);
        return referer;
    }

    public String getUserId(String value){
        try {
            String userId = Des3EncryptionUtil.decode(Const.DES3_KEY,value);
            userId = userId.substring(0,userId.indexOf(Const.PASSWORD_KEY));
            return userId;
        }catch (Exception e){
            logger.error("解析cookie异常：",e);
        }
        return null;
    }
}
