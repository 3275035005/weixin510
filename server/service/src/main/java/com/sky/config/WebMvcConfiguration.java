package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.interceptor.JwtTokenUserInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;
import java.util.List;

/**
 * Web组件的配置类(SpringBoot会自动扫描该类并将类中对应的Bean注入到IOC容器)
 */
@Slf4j
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    //注入Jwt令牌拦截器(用于注册到SpringBoot的拦截器链中)
    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    @Resource
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 添加自定义拦截器到SpringBoot的拦截器链中
     *
     * @param registry 拦截器注册器
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("【开始】 注入JwtTokenAdminInterceptor 商家端令牌校验拦截器");
        registry.addInterceptor(jwtTokenAdminInterceptor) //将自定义拦截器注入到拦截器链中
                .addPathPatterns("/admin/**")  //拦截所有/admin后的所有请求,无论层次,包括/admin本身
                .excludePathPatterns("/admin/employee/login","/admin/common/download","/admin/common/upload"); //放行登录请求
        log.info("【开始】 注入JwtTokenUserInterceptor 客户端令牌校验拦截器");
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**") //拦截所有/user后的所有请求,无论层次,包括/user本身
                .excludePathPatterns("/user/employee/register", "/user/employee/login", "/user/shop/status"); //放行登录请求与查看店铺状态请求
    }

    /**
     * 基于Knife4j生成接口文档(只要将Docket放到了IOC容器中,Knife4j就可以自动根据Docket中的相关信息生成接口文档)
     * 生成B端的Docket单据信息[自动将B端的表现层分成一组]
     *
     * @return Docket:单据(包含了要生成的接口文档的相关数据信息)
     */
    @Bean
    public Docket docket() {
        //声明Api接口文档的基本信息
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("菜谱小程序项目接口文档").version("2.0").description("菜谱小程序项目接口文档").build();
        log.info("【开始】 生成B端(商家端)接口文档");
        //生成单据Docket
        return new Docket(DocumentationType.SWAGGER_2).groupName("菜谱小程序-B端(商家端)接口").apiInfo(apiInfo).select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.admin")) //扫描指定包中的Controller里类的对应注解
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 基于Knife4j生成接口文档(只要将Docket放到了IOC容器中,Knife4j就可以自动根据Docket中的相关信息生成接口文档)
     * 生成C端的Docket单据信息[自动将C端的表现层分成一组]
     *
     * @return Docket:单据(包含了要生成的接口文档的相关数据信息)
     */
    @Bean
    public Docket docket2() {
        //声明Api接口文档的基本信息
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("菜谱小程序项目接口文档").version("2.0").description("菜谱小程序项目接口文档").build();
        log.info("【开始】 生成C端(用户端)接口文档");
        //生成单据Docket
        return new Docket(DocumentationType.SWAGGER_2).groupName("菜谱小程序-C端(客户端)接口").apiInfo(apiInfo).select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.user")) //扫描指定包中的Controller里类的对应注解
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 声明静态资源映射方案(用于访问生成的接口文档)
     *
     * @param registry 资源映射注册器
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //指定当前台通过Swagger访问接口文档的路径访问的时候,将请求映射给对应的路径。
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 拓展SpringMvc消息转换器(将自己指定消息转换的映射机制指定为默认的转换方式)
     *
     * @param converters 转换器List集合
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("【开始】 注入自定义Json日期格式转换器");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //创建自定义的对象映射转换器(日期的转换方式)
        JacksonObjectMapper jacksonObjectMapper = new JacksonObjectMapper();
        mappingJackson2HttpMessageConverter.setObjectMapper(jacksonObjectMapper);
        converters.add(0, mappingJackson2HttpMessageConverter);
    }
}
