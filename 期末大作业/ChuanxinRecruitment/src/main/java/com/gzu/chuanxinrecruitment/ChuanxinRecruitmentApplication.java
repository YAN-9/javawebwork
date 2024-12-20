package com.gzu.chuanxinrecruitment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
// 扫描mapper文件夹下的所有mapper接口
@MapperScan("com.gzu.chuanxinrecruitment.mapper")
public class ChuanxinRecruitmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChuanxinRecruitmentApplication.class, args);
    }

}
