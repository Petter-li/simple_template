package com.example.simple_template.db.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginWithWXForm {

    @NotBlank(message = "openId不可为空")
    private String openId;

    @NotBlank(message = "telCode不可为空")
    private String telCode;
}
