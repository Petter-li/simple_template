package com.example.simple_template.db.dto;

import com.example.simple_template.enumeration.UserSourceEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterForm {

    @NotNull(message = "用户来源不可为空")
    private UserSourceEnum userSource;

    @NotBlank(message = "验证码不可为空")
    @Pattern(regexp = "^1(3|4|5|6|7|8|9)\\d{9}$", message = "电话格式不正确")
    private String tel;
}
