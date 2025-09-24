package com.example.simple_template.config.filter;

import cn.hutool.core.util.StrUtil;
import com.example.simple_template.common.util.JwtUtil;
import com.example.simple_template.service.Impl.TpUserServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Scope("prototype")
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${template.jwt.cacheExpire}")
    private int cacheExpire;

    @Autowired
    private TpUserServiceImpl tpUserService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (StrUtil.isBlank(token) || !token.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        token = token.substring(7);
        Claims claims = jwtUtil.getClaimByToken(token);
        if (claims == null) {
            chain.doFilter(request, response);
            return;
        }

        String userId = claims.getSubject();

        // Handle expired token with Redis refresh logic
        if (jwtUtil.isTokenExpired(claims.getExpiration())) {
            String refreshToken = redisTemplate.opsForValue().get("token:" + userId);
            if (StrUtil.isNotBlank(refreshToken)) {
                // Generate new token
                String newToken = jwtUtil.createToken(Long.parseLong(userId));
                // Store new token in Redis
                redisTemplate.opsForValue().set("token:" + userId, newToken, cacheExpire, TimeUnit.DAYS);
                // Add new token to response header
                response.setHeader("Authorization", "Bearer " + newToken);
                // Authenticate user for this request
                authenticateUser(userId);
            }
        } else {
            // Token is valid, authenticate user
            authenticateUser(userId);
        }

        chain.doFilter(request, response);
    }

    private void authenticateUser(String userId) {
        Set<String> permissions = tpUserService.searchUserPermissions(Long.parseLong(userId));
        /*List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }*/
        // List<SimpleGrantedAuthority> authorities = Arrays.stream(permissions.toArray(new String[0])).map(SimpleGrantedAuthority::new).toList();
        List<SimpleGrantedAuthority> authorities = permissions.stream().map(SimpleGrantedAuthority::new).toList();
        // In a real application, you would load user details and authorities from the database
        UserDetails userDetails = new User(userId, "", authorities);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
