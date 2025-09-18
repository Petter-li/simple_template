package com.example.simple_template.common.util;

import cn.hutool.core.util.StrUtil;
import com.example.simple_template.exception.OwnException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Random;


@Component
public class Methods {

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 根据请求中的token获取用的userId
     * @param request
     * @return
     */
    public Integer getUserIdByRequest(HttpServletRequest request) {
        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }
        int userId = jwtUtil.getUserId(token);
        return userId;
    }

    /**
     * 随机生成字符串
     * @param digits
     * @return
     */
    public String getRandomString(Integer digits) {
        if(digits > 0) {
            StringBuilder sb = new StringBuilder();
            String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            Random random = new Random();

            for (int i = 0; i < digits; i++) {
                int index = random.nextInt(characters.length());
                char randomChar = characters.charAt(index);
                sb.append(randomChar);
            }

            return sb.toString();
        }else {
            throw new OwnException("请指定字符串长度");
        }

    }
}
