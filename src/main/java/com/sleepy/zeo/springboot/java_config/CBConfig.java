package com.sleepy.zeo.springboot.java_config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1. Configuration注解的类等价于以往的一个xml文件
 * 2. Configuration注解只能标记在类上，表示该类为JavaConfig类
 * 3. Configuration是一个整合了@Component注解的复合注解，它可以被Component-Scan识别，并以一个bean的形式由Spring IOC容器托管，
 * 但是为了和Bean注解的功能不冲突，一般不会去使用这个bean
 * 4. 标记在非Configuration类里的Bean注解只是普通的方法调用，无法扫描装配成Bean
 */
@Configuration
public class CBConfig {

    public static class SleepyBean {

        public SleepyBean() {
        }

        @Override
        public String toString() {
            return "SleepyBean[" +
                    "hash=" + Integer.toHexString(hashCode()) +
                    ']';
        }
    }

    public static class ClientBean1 {

        private SleepyBean sleepyBean;

        public ClientBean1(SleepyBean bean) {
            this.sleepyBean = bean;
        }

        @Override
        public String toString() {
            return "ClientBean1[" +
                    "hash=" + Integer.toHexString(hashCode()) +
                    ", sleepyBean=" + sleepyBean +
                    ']';
        }
    }

    public static class ClientBean2 {
        private ClientBean1 clientBean1;

        public ClientBean2(ClientBean1 bean) {
            this.clientBean1 = bean;
        }

        @Override
        public String toString() {
            return "ClientBean2[" +
                    "hash=" + Integer.toHexString(hashCode()) +
                    ", clientBean1=" + clientBean1 +
                    ']';
        }
    }

    /**
     * Bean注解：
     * 1. Bean注解只能标记在方法上，表示该方法返回一个Spring Bean，可以被IOC容器托管
     * 2. Bean注解等价于以往xml文件中的<bean/>元素
     *
     * 依赖注入其它Bean的方式，比如这里在其它的Bean里注入SleepyBean：
     * 1. Bean注解的方法传入要注入的bean
     *      / @Bean
     *      / public ClientBean1 getClientBean1(SleepyBean bean) {
     *      /     return new ClientBean1(bean);
     *      / }
     * 2. 调用Bean注解的方法
     *      / @Bean
     *      / public ClientBean1 getClientBean1() {
     *      /     return new ClientBean1(getSleepyBean());
     *      / }
     */
    @Bean
    public SleepyBean getSleepyBean() {
        return new SleepyBean();
    }

    @Bean
    public ClientBean1 getClientBean1() {
        return new ClientBean1(getSleepyBean());
    }

    @Bean
    public ClientBean2 getClientBean2() {
        return new ClientBean2(getClientBean1());
    }
}
