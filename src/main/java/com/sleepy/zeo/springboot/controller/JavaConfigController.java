package com.sleepy.zeo.springboot.controller;

import com.sleepy.zeo.springboot.java_config.CBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/jc")
public class JavaConfigController {

    private CBConfig.SleepyBean sleepyBean;
    private CBConfig.ClientBean1 clientBean1;
    private CBConfig.ClientBean2 clientBean2;

    @Autowired
    public void setSleepyBean(CBConfig.SleepyBean sleepyBean) {
        this.sleepyBean = sleepyBean;
    }

    @Autowired
    public void setClientBean1(CBConfig.ClientBean1 clientBean1) {
        this.clientBean1 = clientBean1;
    }

    @Autowired
    public void setClientBean2(CBConfig.ClientBean2 clientBean2) {
        this.clientBean2 = clientBean2;
    }

    @RequestMapping("")
    public void javaConfig(HttpServletResponse response) throws IOException {
        response.getWriter().write(sleepyBean + "## " + clientBean1 + "## " + clientBean2);
    }
}
