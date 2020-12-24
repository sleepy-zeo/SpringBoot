# SpringBoot

### security builder

```java

// SecurityBuilder
//
// 提供创建类型O实例的建造器
public interface SecurityBuilder<O> {
    O build() throws Exception;
}

// AbstractSecurityBuilder
//
// 提供创建类型O实例的建造器，添加了一些方法
public abstract class AbstractSecurityBuilder<O> implements SecurityBuilder<O> {

    private AtomicBoolean building = new AtomicBoolean();
    private O object;

    public final O build() throws Exception {
        if (this.building.compareAndSet(false, true)) {
            this.object = doBuild();
            return this.object;
        }
        throw new AlreadyBuiltException("This object has already been built");
    }

    protected abstract O doBuild() throws Exception;
}

// Abstract configured builder
//
// 提供创建类型O实例的建造器，添加了更多的方法
public abstract class AbstractConfiguredSecurityBuilder<O, B extends SecurityBuilder<O>>
        extends AbstractSecurityBuilder<O> {

    protected final O doBuild() throws Exception {
        synchronized (configurers) {

            buildState = BuildState.INITIALIZING;
            beforeInit();
            init();
            buildState = BuildState.CONFIGURING;
            beforeConfigure();
            configure();
            buildState = BuildState.BUILDING;
            O result = performBuild();
            buildState = BuildState.BUILT;
            return result;
        }
    }

    protected abstract O performBuild() throws Exception;

    private void configure() throws Exception {
        Collection<SecurityConfigurer<O, B>> configurers = getConfigurers();

        for (SecurityConfigurer<O, B> configurer : configurers) {
            configurer.configure((B) this);
        }
    }
}
```


### security configurer

```java
// SecurityConfigurer
//
// 提供了配置(建造O类型的建造器)的配置器
public interface SecurityConfigurer<O, B extends SecurityBuilder<O>> {
    void init(B builder) throws Exception;
    void configure(B builder) throws Exception;
}

// WebSecurityConfigurer
//
// 提供了配置(建造Filter类型的建造器)的配置器
public interface WebSecurityConfigurer<T extends SecurityBuilder<Filter>> extends SecurityConfigurer<Filter, T> {
}

// WebSecurityConfigurerAdapter
//
// 提供了配置(WebSecurity类型)的配置器
public abstract class WebSecurityConfigurerAdapter implements WebSecurityConfigurer<WebSecurity> {
}

// WebSecurity是一个(创建Filter实例)的建造器
public final class WebSecurity extends AbstractConfiguredSecurityBuilder<Filter, WebSecurity> implements SecurityBuilder<Filter>, ApplicationContextAware {
}

// HttpSecurity是一个(创建DefaultSecurityFilterChain)实例的建造器
public final class HttpSecurity extends AbstractConfiguredSecurityBuilder<DefaultSecurityFilterChain, HttpSecurity> implements SecurityBuilder<DefaultSecurityFilterChain>, HttpSecurityBuilder<HttpSecurity> {
}
```