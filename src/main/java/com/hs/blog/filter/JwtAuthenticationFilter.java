package com.hs.blog.filter;

import com.hs.blog.context.CustomUserDetails;
import com.hs.blog.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod; // 引入 HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 如果是 OPTIONS 预检请求，直接放行，不执行任何 JWT 相关的逻辑
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            log.info("OPTIONS request, skipping JWT filter...");
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURI();
        if ("/api/admin/login".equals(requestURI)) {
            log.info("Login request, skipping JWT filter...");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("JwtAuthenticationFilter is running for request: {}", request.getRequestURI());

        String token = request.getHeader("Authorization");

        log.info("Authorization Header: {}", token);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
            try {
                Claims claims = jwtUtil.validateJWT(token);
                String username = String.valueOf(claims.get("username"));
                String userId = String.valueOf(claims.get("userId"));
                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_USER"));

                CustomUserDetails userDetails = new CustomUserDetails(userId, username, authorities);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
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