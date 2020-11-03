package com.sleepy.zeo.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBootApplication由@SpringBootConfiguration @EnableAutoConfiguration @ComponentScan三个注解组成
 *
 * SpringBootConfiguration
 *      区别@Configuration而新提供的专属于SpringBoot的注解，功能和@Configuration完全一致
 *
 * EnableAutoConfiguration
 *      借助@Import注解将所有符合配置条件的bean加载到IoC容器中，这里的Import是@Import(EnableAutoConfigurationImportSelector.class)，
 *      EnableAutoConfigurationImportSelector又借助了SpringFactoriesLoader的支持，才能完成所有配置类的加载，具体的实现为：
 *          1. SpringFactoriesLoader从classpath中搜寻所有的META-INF/spring.factories文件，
 *          2. 将其中Key[org.springframework.boot.autoconfigure.EnableAutoConfiguration]对应的Value配置项通过反射的方式实例化为标注了@Configuration的IoC容器配置类
 *          3. 将所有的实例汇总到当前使用的IoC容器中
 *
 * ComponentScan
 *      自动扫描并加载符合条件的组件或Bean，将这些实例加载到当前使用的容器中
 */
@SpringBootApplication
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
