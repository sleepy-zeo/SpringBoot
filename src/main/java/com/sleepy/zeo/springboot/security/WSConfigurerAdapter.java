package com.sleepy.zeo.springboot.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * EnableWebSecurity
 *      开启Security服务
 *
 * EnableGlobalMethodSecurity
 *      开启全局Security注解
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WSConfigurerAdapter extends WebSecurityConfigurerAdapter {
    private Log logger = LogFactory.getLog(WSConfigurerAdapter.class);

    private UserDetailsService userDetailsService;
    private DataSource dataSource;

    @Autowired
    @Qualifier(value = "userDetailsServiceImpl")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setDataSource(DataSource source) {
        this.dataSource = source;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        logger.info("tr hash: "+tokenRepository.hashCode());
        tokenRepository.setDataSource(dataSource);
        // 自动在程序第一次启动的时候创建persistent_logins表
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    // 配置密码相关
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new PasswordEncoder() {
                    @Override
                    public String encode(CharSequence charSequence) {
                        logger.info("encode: " + charSequence);
                        return charSequence.toString();
                    }

                    @Override
                    public boolean matches(CharSequence charSequence, String s) {
                        logger.info("matches: " + charSequence + "--" + s);
                        return s.equals(charSequence.toString());
                    }
                });
    }

    // 配置针对全局的忽略规则
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置忽略拦截的文件夹和文件，常用于放行静态的资源文件
        web.ignoring()
                .antMatchers("/static/**")
                .antMatchers("/favicon.ico")
                .antMatchers("/drawable/**");
    }

    // 配置权限控制规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 允许某些匿名url，然后对于其它任何request都需要登陆认证
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                // 配置login和login成功跳转页
                .formLogin()
                .defaultSuccessUrl("/welcome").permitAll()
                .and()
                // 配置logout
                .logout().permitAll()
                .and()
                // 配置自动登陆
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(600) // s
                .userDetailsService(userDetailsService);

        // 关闭CSRF跨域
        http.csrf().disable();
    }
}
