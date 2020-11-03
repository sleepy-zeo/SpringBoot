# SpringBoot

## 什么是Spring Boot

- Spring Boot通过pom.xml构建项目
- Spring Boot的核心就是约定大于配置，即Spring Boot是一个整合很多可插拔的组件的框架，提供了约定好的默认配置，开发者根据需求修改参数配置

## Spring Boot配置文件

- application.yml是Spring Boot的全局配置文件
- application.yml存放在resources目录下或resources/config目录下
- resources/config目录下的application.yml优先级更高
- 可以通过application-{profile}.properties配置多种环境，然后在application.yml中指定使用的环境:

```yml
spring:
 profiles:
  active: profile
```

## Spring Boot默认资源目录

Spring Boot的默认静态资源文件加载路径(前面路径的优先级要高于后面的路径)为：
```yml
spring:
 resources:
  static-locations:
   - classpath:/META-INF/resources/
   - classpath:/resources/
   - classpath:/static/
   - classpath:/public/
```