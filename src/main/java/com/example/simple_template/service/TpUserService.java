package com.example.simple_template.service;


import com.example.simple_template.db.entity.TpUser;

import java.util.Set;

public interface TpUserService {

    // public Long registerUser(RegisterForm form);

    public Set<String> searchUserPermissions(Long userId);

    // public TpUser login(LoginForm form);

    public TpUser searchById(Long userId);
}
