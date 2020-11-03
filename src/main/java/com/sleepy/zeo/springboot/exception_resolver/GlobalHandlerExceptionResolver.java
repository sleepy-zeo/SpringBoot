package com.sleepy.zeo.springboot.exception_resolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 捕获除404之外的所有异常
@Configuration
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {
    private static final Log logger = LogFactory.getLog(GlobalHandlerExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object o, Exception e) {
        logger.info(e);

        ModelAndView modelView = new ModelAndView();
        modelView.setViewName("error");
        return modelView;
    }
}
