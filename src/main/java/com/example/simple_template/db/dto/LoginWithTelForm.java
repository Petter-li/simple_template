package com.example.simple_template.db.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginWithTelForm {

    @NotBlank(message = "电话不可为空")
    @Pattern(regexp = "^1(3|4|5|6|7|8|9)\\d{9}$", message = "电话格式不正确")
    private String tel;

    @NotBlank(message = "验证码不可为空")
    @Pattern(regexp = "^[0-9]{6}$", message = "验证码必须是6位数字")
    private String captcha;
}
