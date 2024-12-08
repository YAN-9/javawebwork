package com.gzu.chuanxinrecruitment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@SpringBootTest
class ChuanxinRecruitmentApplicationTests {

    @Test
    void PasswordEncryptor () { // 密码加密、解密
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        加密
        System.out.println(passwordEncoder.encode("password123"));
//        解密验证
        System.out.println(passwordEncoder.matches("123456","$2a$10$YKd/VMxuh.5.A3l02kMZUOrpBjNfiniJ/tU0y2efLINVOy8gGQmVe"));
    }

}
