package com.sleepy.zeo.springboot.java.javaconfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private static final Log logger = LogFactory.getLog(CBConfig.class);

    public static class ClientBean1 {

        public ClientBean1() {
            logger.info("Client Bean1");
        }

        @Override
        public String toString() {
            return "ClientBean1[" +
                    "hash=" + Integer.toHexString(hashCode()) +
                    ']';
        }
    }

    public static class ClientBean2 {

        private ClientBean1 clientBean1;

        public ClientBean2(ClientBean1 bean) {
            logger.info("Client Bean2");
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

    public static class ClientBean3 {
        private ClientBean2 clientBean2;

        public ClientBean3(ClientBean2 bean) {
            logger.info("Client Bean3");
            this.clientBean2 = bean;
        }

        @Override
        public String toString() {
            return "ClientBean3[" +
                    "hash=" + Integer.toHexString(hashCode()) +
                    ", clientBean2=" + clientBean2 +
                    ']';
        }
    }

    /**
     * Bean注解：
     * 1. Bean注解只能标记在方法上，表示该方法返回一个Spring Bean，可以被IOC容器托管
     * 2. Bean注解等价于以往xml文件中的<bean/>元素
     *
     * 依赖注入其它Bean的方式，比如这里在其它的Bean里注入ClientBean：
     * 1. Bean注解的方法传入要注入的bean，等价于从容器要一个ClientBean的实例
     *      / @Bean
     *      / public ClientBean2 getClientBean2(ClientBean1 bean) {
     *      /     return new ClientBean2(bean);
     *      / }
     * 2. 调用Bean注解的方法，，等价于从容器要一个ClientBean的实例
     *      / @Bean
     *      / public ClientBean2 getClientBean2() {
     *      /     return new ClientBean2(getClientBean1());
     *      / }
     */
    @Bean
    public ClientBean1 getClientBean1() {
        logger.info("GET Client Bean1");
        return new ClientBean1();
    }

    @Bean
    public ClientBean2 getClientBean2() {
        logger.info("GET Client Bean2");
        return new ClientBean2(getClientBean1());
    }

    /**
     * singleton
     *      在整个Spring IoC容器中，使用singleton定义的Bean将只有一个实例
     *
     * prototype
     *      每次通过容器的getBean方法获取prototype定义的Bean时，都将产生一个新的Bean实例
     * 举例:
     *      如果这里三个都设置为prototype，那么当获取ClientBean2的时候，需要注入一个ClientBean1，
     *      这时候即使ClientBean1的实例已经存在于容器中了，由于ClientBean1是prototype类型，还是
     *      会重新创建一个新的ClientBean1传递给ClientBean2
     */
    @Bean
    public ClientBean3 getClientBean3() {
        logger.info("GET Client Bean3");
        return new ClientBean3(getClientBean2());
    }
}
