package com.sleepy.zeo.springboot.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 等价于配置
 * <servlet>
 *     <servlet-name>helloServlet</servlet-name>
 *     <servlet-class>xxx.HelloServlet</servlet-class>
 * </servlet>
 * <servlet-mapping>
 * 	   <servlet-name>helloServlet</servlet-name>
 *     <url-pattern>/helloServlet</url-pattern>
 * </servlet-mapping>
 */
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("HelloServlet doGet invoked.");
    }
}
