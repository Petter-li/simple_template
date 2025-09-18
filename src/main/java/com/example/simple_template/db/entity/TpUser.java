package com.example.simple_template.db.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * tp_user
 */
@Data
public class TpUser implements Serializable {
    private Long id;

    private String openId;

    private String password;

    private String nickname;

    private String photo;

    private String name;

    private Object sex;

    private String tel;

    private String email;

    private Object role;

    private Boolean root;

    private Integer deptId;

    private Byte status;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}