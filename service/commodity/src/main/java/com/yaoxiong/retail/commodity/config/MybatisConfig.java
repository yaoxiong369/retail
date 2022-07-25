package com.yaoxiong.retail.commodity.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import io.seata.rm.datasource.xa.DataSourceProxyXA;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
@MapperScan("com.yaoxiong.retail.commodity.mapper")
public class MybatisConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     *
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DruidDataSource druidDataSource(){
        return new DruidDataSource();
    }
    /**
     *
     * @param druidDataSource
     * @return
     */
    @Primary
    @Bean("dataSource")
    public DataSource  dataSource(DataSource druidDataSource) {
        return new DataSourceProxyXA(druidDataSource);
    }
}
