package com.sleepy.zeo.springboot.java_config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration只能标记在类上，表示该类为JavaConfig类
 * Configuration注解的类相当于以往的一个xml文件
 * Configuration本身也是一个复合的注解，它还整合了@Component，也就是说它可以被Component-Scan识别，并以一个Bean的形式由Spring IOC容器托管，当然在大部分情况下开发者并不会需要使用这个bean
 */
@Configuration
public class AppConfig {

    static class MyBean {

    }

    static class MyBean2 {

        public MyBean2(MyBean bean) {

        }

    }

    static class MyBean3 {
        public MyBean3(MyBean2 bean) {

        }
    }

    /**
     * Bean只能标记在方法上，表示该方法返回一个Spring Bean，可以被IOC容器托管，相当于以前在xml文件中写的<bean/>元素
     *
     *
     */
    @Bean
    public MyBean myBean() {
        // instantiate, configure and return bean ...
        return new MyBean();
    }

    /**
     * 可以在方法参数里指定要注入的bean，
     * 或是在同一个@Configuration类里直接调用@Bean标记的方法，
     * 也可以在new出来对象后，使用set方法注入
     *
     */
    @Bean
    public MyBean2 myBean2(MyBean myBean) {
        return new MyBean2(myBean);
    }

    @Bean
    public MyBean3 myBean3(MyBean myBean) {
        return new MyBean3(myBean2(myBean));
    }

    /**
     * 你可能注意到创建MyBean3时，是调用了同@Configuration类里的其他@Bean方法
     * ，在这种情况下，Spring会利用CGLIB实现的AOP，在调用方法前到IOC容器里去找到
     * 对应的myBean2并返回。因此在创建MyBean3时，它被注入的myBean2是IOC容器里的myBean2，而非直接调用myBean2()方法，
     * 同时由于Bean默认的Scope是singleton，因此myBean2被注入时也以单例形式注入，
     * 如果你不太明白，那么可以看看下面这个例子：
     */
    static class ClientDao {

    }

    static class ClientDaoImpl extends ClientDao {

    }

    static class ClientService {

    }

    static class ClientServiceImpl extends ClientService {
        public void setClientDao(ClientDao dao) {

        }
    }

    @Bean
    public ClientService clientService1() {
        ClientServiceImpl clientService = new ClientServiceImpl();
        clientService.setClientDao(clientDao());
        return clientService;
    }

    @Bean
    public ClientService clientService2() {
        ClientServiceImpl clientService = new ClientServiceImpl();
        clientService.setClientDao(clientDao());
        return clientService;
    }

    @Bean
    public ClientDao clientDao() {
        return new ClientDaoImpl();
    }

    /*
    在该例中，clientService1()和clientService2()方法都调用了clientDao()方法，并且clientDao()方法是被@Bean标注的方法，因此被注入到clientService1和clientService2的clientDao此时指向的同一个bean对象。

你可能注意到了我前面强调了“同@Configuration类里的其他@Bean方法”，你可能会想@Bean能不能放在没有@Configuration标记的类的方法上，实际上确实是有这种用法的。

@Bean并不一定非要标记在@Configuration类里，当标记在普通的类的方法上时，Spring容器会使用精简模式来创建Bean，此时调用其他@Bean方法只是普通的方法调用，而非通过AOP方式进行依赖注入，并且由于没有@Configuration注解，该类无法通过扫描的方式装配bean，必须手动注册到ApplicationContext中才能装配bean。
     */
}
