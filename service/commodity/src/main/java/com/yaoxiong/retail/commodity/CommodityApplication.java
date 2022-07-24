package com.yaoxiong.retail.commodity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = "com.yaoxiong")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.yaoxiong")
public class CommodityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommodityApplication.class,args);
//        JacksonTypeHandler.setObjectMapper(new ObjectMapper());
    }
}
