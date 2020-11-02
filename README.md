# SpringBoot

## 什么是Spring Boot

- Spring Boot通过pom.xml构建项目，简化了使用Spring的各种繁琐配置
- Spring Boot本质上就是一个整合很多可插拔的组件的框架，提供了默认配置，并且开发者可以根据需求修改参数配置

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