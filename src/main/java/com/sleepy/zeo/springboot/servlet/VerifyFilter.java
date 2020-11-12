package com.sleepy.zeo.springboot.servlet;

import com.sleepy.zeo.springboot.data.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "verifyFilter")
@Component(value = "verifyFilterCnt")
public class VerifyFilter extends OncePerRequestFilter {

    private static final Log logger = LogFactory.getLog(VerifyServlet.class);

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    private boolean intercept(HttpServletRequest request) {
        boolean intercept = "POST".equals(request.getMethod()) && pathMatcher.match("/login", request.getServletPath());
        logger.info("intercept path: " + request.getServletPath() + ", result: " + intercept);
        logger.info(request.getMethod());
        logger.info(pathMatcher.match("/login", request.getServletPath()));
        return intercept;
    }

    private boolean verifyCodeValid(String inputVerifyCode) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String verifyCode = (String) request.getSession().getAttribute("verifycode");
        logger.info("inputCode: " + inputVerifyCode + ", requestCode: " + verifyCode);
        return verifyCode.toLowerCase().equals(inputVerifyCode.toLowerCase());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!Constants.ENABLE_VERIFY_FILTER) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        if (intercept(httpServletRequest)) {
            String verifyCode = httpServletRequest.getParameter("verifycode");
            if (verifyCodeValid(verifyCode)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                logger.info("verifycode error: " + verifyCode);
                httpServletRequest.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", new DisabledException("verify code error"));
                httpServletRequest.getRequestDispatcher("/login/error").forward(httpServletRequest, httpServletResponse);
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
