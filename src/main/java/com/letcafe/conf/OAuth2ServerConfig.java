package com.letcafe.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public OAuth2ServerConfig(AuthenticationManager authenticationManager, RedisConnectionFactory redisConnectionFactory) {
        this.authenticationManager = authenticationManager;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许客户端的表单认证
        security
                // 开启check_token的使用
                .checkTokenAccess("permitAll()")
                // 允许客户端的表单认证的方式
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("android")
                .scopes("xx")
                .secret("{bcrypt}" + new BCryptPasswordEncoder().encode("android"))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials")
                .authorities("oauth2")
                .and()
                .withClient("webapp")
                .scopes("xx")
                .secret("{bcrypt}" + new BCryptPasswordEncoder().encode("android"))
                .authorizedGrantTypes("implicit");
    }

    private AuthorizationServerTokenServices customTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(new RedisTokenStore(redisConnectionFactory));
        // 允许使用refresh_token刷新token
        tokenServices.setSupportRefreshToken(true);
        // 设置token的过期时间 : 1小时
        tokenServices.setAccessTokenValiditySeconds(3600);
        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 使用Redis存储验证Token
                .tokenStore(new RedisTokenStore(redisConnectionFactory))
                // 设置TokenService给access_token设置超时时间
                .tokenServices(customTokenServices())
                // 允许获取端点的方法设置为GET、POST
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .authenticationManager(authenticationManager);
    }
}
