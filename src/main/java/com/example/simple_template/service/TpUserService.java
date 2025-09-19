package com.example.simple_template.service;


import com.example.simple_template.db.dto.LoginWithTelForm;
import com.example.simple_template.db.dto.RegisterForm;
import com.example.simple_template.db.entity.TpUser;

import java.util.Set;

public interface TpUserService {

    public TpUser loginByTel(LoginWithTelForm form);

    public Set<String> searchUserPermissions(Long userId);

    // public TpUser login(LoginForm form);

    public TpUser searchById(Long userId);

    public String makeCaptcha(String tel);
}
