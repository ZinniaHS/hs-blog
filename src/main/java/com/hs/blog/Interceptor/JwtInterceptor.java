package com.hs.blog.Interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.blog.result.Result;
import com.hs.blog.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setStatus(HttpStatus.OK.value());
            return false; // 直接放行
        }
        System.out.println("==================拦截器执行了==================");
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(Result.error("无效的token"));
            response.getWriter().write(jsonResponse);
            return false;
        }
        token = token.replace("Bearer ", "");
        try {
            Claims claims = jwtUtil.validateJWT(token);
            System.out.println(claims.get("username"));
            // claims = {password=123456,
            //           username=admin,
            //           iat=1745846750,
            //           exp=1745850350}
//            request.setAttribute("user", claims);
            return true;
        } catch (Exception e) {
            System.out.println("解析token失败"+e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
