package com.dwl.serurity_service.config;

import com.dwl.serurity_service.filter.TokenAuthenticationFilter;
import com.dwl.serurity_service.filter.TokenLoginFilter;
import com.dwl.serurity_service.security.DefaultPasswordEncoder;
import com.dwl.serurity_service.security.TokenLogoutHandler;
import com.dwl.serurity_service.security.TokenManager;
import com.dwl.serurity_service.security.UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 * Spring Security 的核心配置就是继承 WebSecurityConfigurerAdapter 并注解 @EnableWebSecurity 的配置
 * 这个配置指明了用户名密码的处理方式、请求路WebAsyncManager径、登录
 * 登出控制等和安全相关的配置
 * </p>
 *
 * @author qy
 * @since 2019-11-18
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {
    // 自定义查询数据库用户名密码和权限信息
    private UserDetailsService userDetailsService;
    // token 管理工具类（生成 token）
    private TokenManager tokenManager;
    // 密码管理工具类
    private DefaultPasswordEncoder defaultPasswordEncoder;
    // redis 操作工具类
    private RedisTemplate redisTemplate;

    @Autowired
    public TokenWebSecurityConfig(UserDetailsService userDetailsService, TokenManager tokenManager, DefaultPasswordEncoder defaultPasswordEncoder, RedisTemplate redisTemplate) {
        this.userDetailsService = userDetailsService;
        this.tokenManager = tokenManager;
        this.defaultPasswordEncoder = defaultPasswordEncoder;
        this.redisTemplate = redisTemplate;
    }

    public TokenWebSecurityConfig() {
    }


    /**
     * 配置设置
     * 设置退出的地址和 token，redis 操作地址
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 允许配置异常处理 这在使用时自动应用
        http.exceptionHandling()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and()
                // 关闭 csrf
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                // 需要认证
                .authenticated()
                // 配置默认的退出路径
                .and().logout().logoutUrl("/admin/acl/index/logout")
                // 配置登出接口
                .addLogoutHandler(new TokenLogoutHandler(tokenManager, redisTemplate)).and()
                // 配置自定义拦截器
                .addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, redisTemplate))
                .addFilter(new TokenAuthenticationFilter(authenticationManager(), tokenManager, redisTemplate)).httpBasic();
    }

    /**
     * 密码处理
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);
    }

    /**
     * 配置哪些请求不拦截
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/api/**",
                "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**"
        );
        // web.ignoring().antMatchers("/*/**");
    }
}