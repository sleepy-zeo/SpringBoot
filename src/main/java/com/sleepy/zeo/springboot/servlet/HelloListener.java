package com.sleepy.zeo.springboot.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class HelloListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("ServletContext initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("ServletContext destroyed");
    }
}
