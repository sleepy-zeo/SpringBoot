package com.sleepy.zeo.springboot.security;

import com.sleepy.zeo.springboot.servlet.VerifyFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private VerifyFilter verifyFilter;
    private AuthenticationDetailsSource<HttpServletRequest, HttpServletResponse> detailsSource;
    private AuthenticationProvider authenticationProvider;

    @Bean
    public PasswordEncoder defaultPasswordEncoder() {
        return new PasswordEncoder() {
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
        };
    }

    @Autowired
    @Qualifier(value = "userDetailsServiceImpl")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    @Qualifier("verifyFilterCnt")
    public void setVerifyFilter(VerifyFilter verifyFilter) {
        this.verifyFilter = verifyFilter;
    }

    @Autowired
    @Qualifier("wAuthenticationDetailsSource")
    public void setDetailsSource(AuthenticationDetailsSource detailsSource) {
        this.detailsSource = detailsSource;
    }

    @Autowired
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Autowired
    public void setDataSource(DataSource source) {
        this.dataSource = source;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        logger.info("==> tr hash: " + tokenRepository.hashCode());
        tokenRepository.setDataSource(dataSource);
        // 自动在程序第一次启动的时候创建persistent_logins表
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    // 配置密码相关
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(defaultPasswordEncoder());
        auth.authenticationProvider(authenticationProvider);
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
                .antMatchers("/verifycode").permitAll()
                .anyRequest().authenticated()
                .and()
                // 配置login和login成功和失败默认的跳转页
                // TODO: 这里如果不配置loginPage，那么即使配置failureUrl也不会跳转到自定义的Controller
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/welcome").permitAll()
                .failureUrl("/login/error")
                .authenticationDetailsSource(detailsSource)
                .and()
                // 配置logout
                .logout().permitAll()
                .and()
                // 配置自动登陆
                .rememberMe()
                .rememberMeCookieName("sb-token")
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60) // 单位为s，从login rememberMe开始计算，refresh不影响rememberMe的token，只影响cookie的有效期
                .userDetailsService(userDetailsService)
                .and()
                // 配置filter的顺序
                .addFilterBefore(verifyFilter, UsernamePasswordAuthenticationFilter.class);

        // 关闭CSRF跨域
        http.csrf().disable();
    }
}
