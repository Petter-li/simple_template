package com.example.simple_template.db.dao;

import com.example.simple_template.db.entity.TpUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Mapper
public interface TpUserDao {

    public Long insert(HashMap param);

    public TpUser select(Long id);

    public TpUser selectByTel(String tel);

    public Set<String> searchUserPermissions(Long userId);

}