package com.example.simple_template.controller;

import com.example.simple_template.common.util.JwtUtil;
import com.example.simple_template.common.util.R;
import com.example.simple_template.db.dto.LoginWithTelForm;
import com.example.simple_template.service.TpUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "用户模块接口")
public class UserController {

    @Autowired
    private TpUserService tpUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    // 过期时间
    // 此注解为值注入
    @Value("${project.login.expire}")
    private int msgExpire;

    private void saveCacheToken(String token, Long userId) {
        redisTemplate.opsForValue().set(token, userId + "", msgExpire, TimeUnit.DAYS);
    }

    @PostMapping("/loginWithTel")
    public R loginWithTelApi(@Valid @RequestBody LoginWithTelForm form) {
        Long userId = tpUserService.loginByTel(form).getId();
        String token = jwtUtil.createToken(userId);
        Set<String> permissionSet = tpUserService.searchUserPermissions(userId);
        saveCacheToken(token, userId);
        return R.ok().put("token", token).put("permission", permissionSet);
    }

    @GetMapping("/sendCaptcha")
    @Operation(description = "发送验证码")
    public R sendCaptchaApi(@NotBlank(message = "请传入手机号") @RequestParam String tel) {
        tpUserService.makeCaptcha(tel);
        return new R().put("data", true);
    }
}
