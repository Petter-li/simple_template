package com.example.simple_template.service.Impl;

import com.example.simple_template.common.util.Methods;
import com.example.simple_template.db.dao.TpUserDao;
import com.example.simple_template.db.dto.LoginWithTelForm;
import com.example.simple_template.db.dto.RegisterForm;
import com.example.simple_template.db.entity.TpUser;
import com.example.simple_template.exception.OwnException;
import com.example.simple_template.service.TpUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TpUserServiceImpl implements TpUserService {

    @Autowired
    private TpUserDao tpUserDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private Methods methods;

    // 过期时间
    // 此注解为值注入
    @Value("${project.login.expire}")
    private int msgExpire;

    private Long registerUser(RegisterForm form) {
        return 0L;
    }

    @Override
    public TpUser loginByTel(LoginWithTelForm form) {
        return null;
    }

    @Override
    public Set<String> searchUserPermissions(Long userId) {
        return tpUserDao.searchUserPermissions(userId);
    }

    @Override
    public TpUser searchById(Long userId) {
        return tpUserDao.select(userId);
    }

    @Override
    public String makeCaptcha(String tel) {
        // redis中没有该tel，说明可以发送
        if(!redisTemplate.hasKey(tel)) {
            String captcha = methods.getRandomIntString(6);
            redisTemplate.opsForValue().set(tel, captcha, msgExpire, TimeUnit.MINUTES);
            try {
                //TODO 接入短信服务
            }catch (Exception exception) {
                // 发送失败后如果已写入email,则删除不然下次发送会等5分钟
                if(redisTemplate.hasKey(tel)) {
                    redisTemplate.delete(tel);
                }
                throw new OwnException("邮件发送失败");
            }
            return captcha;

        }else {
            throw new OwnException("您的邮件已发送，请查看邮箱");
        }
    }

}
