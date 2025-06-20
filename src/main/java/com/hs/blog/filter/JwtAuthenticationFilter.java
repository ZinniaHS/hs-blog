package com.hs.blog.filter;

import com.hs.blog.context.CustomUserDetails;
import com.hs.blog.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
            try {
                Claims claims = jwtUtil.validateJWT(token);
                // 这里可以根据claims中的用户名创建Authentication对象
                String username = String.valueOf(claims.get("username"));
                String userId = String.valueOf(claims.get("userId"));
//                System.out.println("username: " + username);
//                System.out.println("userId: " + userId);
                // 设置默认权限（ROLE_USER），可以根据业务需求动态配置权限
                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_USER"));

                CustomUserDetails userDetails = new CustomUserDetails(userId, username, authorities);

                // 创建认证对象，可以自定义UserDetails
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // 设置到SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Token无效，清空认证信息
                e.printStackTrace();
                SecurityContextHolder.clearContext();
                setUnauthorizedResponse(response, "Token已过期，请重新登录");
                return;
            }
        }
        // 放行
        filterChain.doFilter(request, response);
    }

    private void setUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\": 401, \"message\": \"" + message + "\"}");
        writer.flush();
    }

}
