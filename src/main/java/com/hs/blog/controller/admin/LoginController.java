package com.hs.blog.controller.admin;

import com.hs.blog.pojo.dto.AdminLoginDTO;
import com.hs.blog.result.Result;
import com.hs.blog.utils.JWTUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

//@CrossOrigin
@Tag(name = "管理员登录接口",description = "管理员登录接口")
@RestController
@Slf4j
@RequestMapping("/admin/login")
public class LoginController {

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping
    public Result login(@RequestBody AdminLoginDTO adminLoginDTO) {
//        System.out.println(adminLoginDTO);
        if (!Objects.equals("admin", adminLoginDTO.getUsername())
                || !Objects.equals("123456", adminLoginDTO.getPassword())) {
            return Result.error("账号密码错误");
        } else {
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("username", adminLoginDTO.getUsername());
            claims.put("password", adminLoginDTO.getPassword());
            String token = jwtUtil.createJWT(claims);
//            System.out.println("生成的token:  "+token);
            return Result.success(token);
        }
    }

}
