package com.config;

import com.common.JacksonObjectMapper;
import com.handler.LoginInterception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
@Slf4j
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterception loginInterception;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置
        registry.addMapping("/**").allowedOrigins("http://localhost:8080").allowedMethods("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterception)
                // .addPathPatterns("/**")
                // .excludePathPatterns("/register")
                // .excludePathPatterns("/login");
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish")
                .addPathPatterns("/follow/**")
                .addPathPatterns("/articles/myListArticle")
                .addPathPatterns("/articles/articlePush")
                .addPathPatterns("/articles/FindMyListArticle")
                .addPathPatterns("/articles/FindPushArticle")
                .addPathPatterns("/sysUser/userMessage")
                .addPathPatterns("/question/ask")
                .addPathPatterns("/question/answer")
                .addPathPatterns("/question/status")
                .addPathPatterns("/feedback")
                .addPathPatterns("/sysUser/isCode")
                .addPathPatterns("/sysUser/updataEmail")
                .addPathPatterns("/sysUser/updataMes");

    }

    /*
     * 拓展mvc的消息转换器
     * */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("拓展消息转换器");
        //创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
        //设置对象转换器底层使用JackSon将java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);
    }
}
