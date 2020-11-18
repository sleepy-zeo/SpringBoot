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

## Spring Boot三个常用注解

```text
@Controller     展现层，表面被标注的类是控制器
@Repository     持久层，和数据库进行交互，即DAO
@Service        业务层，需要设计好接口，一般用于和DAO进行交互
```

## antMatchers匹配规则

```text
antMatchers主要是用来匹配常见的url

?           匹配任意的1个字符
*           匹配任意的0或多个字符
**          匹配任意的0或多个目录

举例:
/app/p?ttern	匹配/app/pattern，但是不匹配/app/pttern
/app/*.x	    匹配app路径下的所有以.x结尾的文件
/**/example	    匹配/app/example、/app/foo/example、/example
/**/*.jsp       匹配任意的jsp文件
/**             匹配所有的url(目前测试是所有的url都能匹配到)
```

## regexMatchers

```text
匹配复杂的url，可以使用regexMatchers，该函数使用正则匹配
```

## spring.resources.static-locations

```text
1. spring.resources.static-locations要设置成/static，这样访问/static/favicon.ico等只需要http://xxx/favicon.ico即可

2. 但是为了html文件中可以通过/static/xxx/xx.png访问到资源，需要配置
@RequestMapping("/static/{dir}/**")
public String src(@PathVariable("dir") String dir, HttpServletRequest request) {
    // 获取请求的全路径: /static/drawable/background.png
    String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    logger.info("path: " + path);

    // 获取匹配到controller的路径: /static/{dir}/**
    String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    logger.info("bestMatchPattern: " + bestMatchPattern);

    // 获取dir的值: drawable
    logger.info("suffix: " + dir);

    return "forward:/" + path.substring(8);
}
这样通过/static/xxx/xx.png也可以访问到资源

3. 配置好忽略
web.ignoring()
    .antMatchers("/static/**")
    .antMatchers("/favicon.ico")
    .antMatchers("/drawable/**");
```
## cookie

```text
path: Cookie的使用路径，最后一个字符必须为"/"
	如果设置为"/sessionWeb/"，则只有contextPath为"/sessionWeb"的程序可以访问该Cookie
	如果设置为"/"，则本域名下所有的contextPath都可以访问该Cookie
domain: 可以访问该Cookie的域名，第一个字符必须为"."
	如果设置为".google.com"，则所有以"google.com"结尾的域名都可以访问该Cookie


// 这里设置了sb-id，spring boot会去找到是否存在对应的session，如果不存在就会回调InvalidSessionStrategy
Cookie cookie = new Cookie("sb-id","NDY2ZWIxNjYtYTJmNy00YmY5LTg4NTctNmQzZjEwOWZlNmFj");
cookie.setMaxAge(3600);
cookie.setPath("/");
response.addCookie(cookie);
```

## session

```text
id ---> UUID.randomUUID().toString()
originalId
sessionAttrs
createTime
lastAccessedTime

sessionId(078b7c7a-bccb-4a86-b69e-38b9f5dfcd38) <-----(base64)-----> cookieValue(MDc4YjdjN2EtYmNjYi00YTg2LWI2OWUtMzhiOWY1ZGZjZDM4)
```

## java extends super ?

```text
java是单继承，所有继承的类构成一棵树

1. T<? super B>
?代表容器里的元素类型，该表示规定了元素类型必须是B的父类
2. T<? extends B>
?代表容器里的元素类型，该表示规定了元素类型必须是B的子类

// example
class FX {
    List<Number> list = new ArrayList<>();
    public <T extends Number> void add(T t) {
        list.add(t);
    }
}

// example
class Fx {
    public AbstractAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority a : authorities) {
            if (a == null) {
                throw new IllegalArgumentException("Authorities collection cannot contain any null elements");
            }
        }
        ArrayList<GrantedAuthority> temp = new ArrayList<>(authorities.size());
        temp.addAll(authorities);
        this.authorities = Collections.unmodifiableList(temp);
    }
}

// example
List<Apple> appleList = new ArrayList<>();
List<Banana> bananaList = new ArrayList<>();
List<? extends Food> list1 = new ArrayList<>(appleList);
List<? extends Food> list2 = new ArrayList<>(bananaList);
```