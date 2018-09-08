package com.letcafe.conf;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

     @Bean
     public Docket restApi() {
         return new Docket(DocumentationType.SWAGGER_2)
                 .genericModelSubstitutes(DeferredResult.class)
                 .useDefaultResponseMessages(false)
                 .forCodeGeneration(false)
                 .securitySchemes(newArrayList(apiKey()))
                 .securityContexts(newArrayList(securityContext()))
                 .pathMapping("/")
                 .select()
                 .apis(RequestHandlerSelectors.basePackage("com.letcafe.api"))
                 .build()
                 .apiInfo(apiInfo());
     }

    private ApiKey apiKey() {
        return new ApiKey("api_key", "api_key", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/*.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(new SecurityReference("api_key", authorizationScopes));
    }


     private ApiInfo apiInfo() {
         return new ApiInfoBuilder()
                 .title("LiveSpider虎牙直播数据接口文档")
                 .description("关于虎牙直播，个人使用爬虫所爬取的直播的信息，相关合作：letcafe@outlook.com")
//                 .contact(new Contact("dongyuguo", "tanklab.tju.edu.cn", "dongyuguo@tju.edu.cn"))
//                 .license("Apache 2.0")
//                 .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                 .version("1.0")
                 .build();
     }

}
