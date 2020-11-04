package com.sleepy.zeo.springboot.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "helloFilter", urlPatterns = "/hello")
public class HelloFilter implements Filter {

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doFilter before");
        filterChain.doFilter(request, response);
        System.out.println("doFilter after");
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
