package com.hs.blog.Interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.blog.result.Result;
import com.hs.blog.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }
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
            Claims claims = JWTUtil.parseJWT(token);
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
