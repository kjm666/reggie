package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description: 登录过滤器，检验用户是否登录
 *
 * @since: 1.0.0
 * @author: KangJiaMing
 * @date: 2023/3/14 9:05
 * @Param null:
 * @return: null
 */
@WebFilter(filterName = "loginCheckFilter", value = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //专门进行路径比较
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、拿到请求地址
        String requestURI = request.getRequestURI();
        String[] urls = new String[]{
                "/employee/login", //不过滤登录路径
                "/employee/logout",//不过滤登出路径
                "/backend/**",     //不过滤前端路径
                "/front/**"        //不过滤前端路径
        };
        //2、判断本次请求是否需要处理
        boolean check = check(requestURI, urls);
        //3、如果不需要处理，放行
        if (check) {//true
            filterChain.doFilter(request, response);
            return;
        }
        //4、判断登录状态，如果已登录，放行
        Long eid = (Long) request.getSession().getAttribute("employee");
        if (null != eid) {
            //将当前用户id放入ThreadLocal
            BaseContext.setCurrentId(eid);
            filterChain.doFilter(request, response);
            return;
        }
        //5、如果未登录则返回登录结果,通过输出流的方式向客户端界面返回数据  request.js中有配置前端拦截器
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    //检验是否过滤
    public boolean check(String requestURI, String[] urls) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
