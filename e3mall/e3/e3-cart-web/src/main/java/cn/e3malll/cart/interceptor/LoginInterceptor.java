package cn.e3malll.cart.interceptor;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //执行Handler之前执行此方法
        //返回true放行，false拦截
        // a)从cookie中取token。
        String token = CookieUtils.getCookieValue(request, "token");
        if (StringUtils.isBlank(token)) {
            return true;
        }
        // c)有token。调用sso系统的服务，根据token查询用户信息。
        E3Result result = tokenService.getUserByToken(token);
        if (result.getStatus() != 200) {
            return true;
        }
        TbUser user = (TbUser) result.getData();
        request.setAttribute("user",user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 执行Handler之后返回ModelAndView之前
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
        // 返回ModelAndView之后，执行。异常处理。
    }
}
