package com.sleepy.zeo.springboot.controller;

import com.sleepy.zeo.springboot.java.javaconfig.CBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/jc")
public class JavaConfigController {

    /**
     * 容器不会提前创建Bean的实例，只有在需要注入的时候(比如这里需要注入)，容器才会去创建Bean的实例
     */
    private CBConfig.ClientBean1 clientBean1;
    private CBConfig.ClientBean2 clientBean2;
    private CBConfig.ClientBean3 clientBean3;

    @Autowired
    public void setClientBean1(CBConfig.ClientBean1 clientBean1) {
        this.clientBean1 = clientBean1;
    }

    @Autowired
    public void setClientBean2(CBConfig.ClientBean2 clientBean2) {
        this.clientBean2 = clientBean2;
    }

    @Autowired
    public void setClientBean3(CBConfig.ClientBean3 clientBean3) {
        this.clientBean3 = clientBean3;
    }

    @RequestMapping("")
    public void javaConfig(HttpServletResponse response) throws IOException {
        response.getWriter().write(clientBean1 + "## " + clientBean2 + "## " + clientBean3);
    }
}
