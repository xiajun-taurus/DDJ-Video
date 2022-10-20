package com.xiajun.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MBP自动分页插件注册
 *
 * @author xiajun
 * @since 2022/10/20
 */
@Configuration
@MapperScan("com.xiajun.mapper")
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        final MybatisPlusInterceptor in = new MybatisPlusInterceptor();
        in.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return in;
    }

}
