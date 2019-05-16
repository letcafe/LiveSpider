package com.letcafe.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY =
            "SELECT phone_number,PASSWORD(password),true " +
                    "FROM company_admin " +
                    "WHERE phone_number = ?";

    private DataSource dataSource;

    @Autowired
    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth/**", "/dist/**", "/bower_components/**", "/plugins/**", "/api/**", "/v2/api-docs").permitAll()
                .anyRequest().authenticated();
//                .and()
//                .httpBasic()
//                .and()
//                .csrf()
//                .disable();

//        http.authorizeRequests()
//                // 允许加载所有的静态资源而不拦截
//                .antMatchers("/oauth/authorize", "/dist/**", "/bower_components/**", "/plugins/**", "/api/**", "/v2/api-docs").permitAll()
//                // 允许特定路径被直接访问
////                .antMatchers("/api/**", "/websock").permitAll()
//                // 为匹配到的URL只需要对用户进行身份认证
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().loginPage("/pages/auth/login").defaultSuccessUrl("/index", true).failureUrl("/login?error").
//                // 设置以上页面允许访问，即默认只允许直接登录
//                permitAll();
//        http.logout()
//                // 退出登录URL,默认为"/logout"
//                .logoutUrl("/pages/auth/logout")
//                // 登陆成功后跳转的URL,默认为"/login?logout"
//                .logoutSuccessUrl("/pages/auth/login")
//                // 登陆成功后的跳转控制器logoutSuccessHandler,可以使用lambda或者自定义实现类
//                // Spring默认提供两种方式的实现SimpleUrlLogoutSuccessHandler,HttpStatusReturningLogoutSuccessHandler
////                .logoutSuccessHandler(logoutSuccessHandler)
//                // 是否删除HttpSession,默认删除
//                .invalidateHttpSession(true)
//                // 以链式的方式在最后添加退出登录的控制器
//                // Spring默认提供五种方式的实现：PersistentTokenBasedRememberMeServices,TokenBasedRememberMeServices,CookieClearingLogoutHandler,CsrfLogoutHandler,SecurityContextLogoutHandler
////                .addLogoutHandler(new CookieClearingLogoutHandler())
//                // 选择删除Cookie的名字
//                .deleteCookies("cookieNamesToClear");
    }

    /**
     * password 方案一：明文存储，用于测试，不能用于生产 : return NoOpPasswordEncoder.getInstance();
     * password 方案二：用 BCrypt 对密码编码 : return new BCryptPasswordEncoder();
     * password 方案三：支持多种编码，通过密码的前缀区分编码方式,推荐 : return PasswordEncoderFactories.createDelegatingPasswordEncoder();
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // 基于内存查询的用户登录
//        auth.
//                inMemoryAuthentication()
//                .withUser("user_1").password("{bcrypt}" + new BCryptPasswordEncoder().encode("123456")).authorities("USER")
//                .and()
//                .withUser("user_2").password("{bcrypt}" + new BCryptPasswordEncoder().encode("123456")).authorities("USER");

//         基于JDBC的用户登录查询
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery(DEF_AUTHORITIES_BY_USERNAME_QUERY);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
