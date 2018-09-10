package top.vetoer.comm.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import top.vetoer.comm.Const;
import top.vetoer.repository.UserRepository;
import top.vetoer.utils.Des3EncryptionUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SecurityFilter implements Filter {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private static Set<String> GreenUrlSet = new HashSet<>();

    @Autowired
    private UserRepository userRepository;

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
        GreenUrlSet.add("/login");
        GreenUrlSet.add("/register");
        GreenUrlSet.add("/index");
        GreenUrlSet.add("/error");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //Todo:
        filterChain.doFilter(servletRequest,servletResponse);
    }

    private boolean containsSuffix(String url) {
        if (url.endsWith(".js")
                || url.endsWith(".css")
                || url.endsWith(".jpg")
                || url.endsWith(".gif")
                || url.endsWith(".png")
                || url.endsWith(".html")
                || url.endsWith(".eot")
                || url.endsWith(".svg")
                || url.endsWith(".ttf")
                || url.endsWith(".woff")
                || url.endsWith(".ico")
                || url.endsWith(".woff2")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean containsKey(String url) {

        if (url.contains("/media/")
                || url.contains("/login")||url.contains("/user/login")
                || url.contains("/register")||url.contains("/user/regist")||url.contains("/index")
                || url.contains("/forgotPassword")||url.contains("/user/sendForgotPasswordEmail")
                || url.contains("/newPassword")||url.contains("/user/setNewPassword")
                || (url.contains("/collector") && !url.contains("/collect/detail/"))
                || url.contains("/collect/standard/")||url.contains("/collect/simple/")
                || url.contains("/user")||url.contains("/favorites")||url.contains("/comment")
                || url.contains("/lookAround")
                || url.startsWith("/user/")
                || url.startsWith("/feedback")
                || url.startsWith("/standard/")) {
            return true;
        } else {
            return false;
        }
    }

    public String getUserId(String value){
//        try {
//            String userId = Des3EncryptionUtil.decode(Const.DES3_KEY,value);
//            userId = userId.substring(0,userId.indexOf(Const.PASSWORD_KEY));
//            return userId;
//        }catch (Exception e){
//            logger.error("解析cookie异常：",e);
//        }
        return null;
    }

    @Override
    public void destroy() {

    }
}
