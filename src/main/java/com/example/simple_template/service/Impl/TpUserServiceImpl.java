package com.example.simple_template.service.Impl;

import com.example.simple_template.db.dao.TpUserDao;
import com.example.simple_template.db.entity.TpUser;
import com.example.simple_template.service.TpUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class TpUserServiceImpl implements TpUserService {

    @Autowired
    private TpUserDao tpUserDao;

    @Override
    public Set<String> searchUserPermissions(Long userId) {
        return tpUserDao.searchUserPermissions(userId);
    }

    @Override
    public TpUser searchById(Long userId) {
        return tpUserDao.select(userId);
    }
}
