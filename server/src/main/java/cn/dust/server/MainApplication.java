package cn.dust.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author: dust
 * @Date: 2019/10/11 14:36
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.dust.model.mapper")
public class MainApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainApplication.class);
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);
    }
}

