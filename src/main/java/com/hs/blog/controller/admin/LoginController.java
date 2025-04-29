package com.hs.blog.controller.admin;

import com.hs.blog.pojo.dto.AdminLoginDTO;
import com.hs.blog.result.Result;
import com.hs.blog.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

//@CrossOrigin
@RestController("adminLoginController")
@Slf4j
@RequestMapping("/admin/login")
public class LoginController {

    @PostMapping
    public Result login(@RequestBody AdminLoginDTO adminLoginDTO) {
        System.out.println(adminLoginDTO);
        if (!Objects.equals("admin", adminLoginDTO.getUsername())
                || !Objects.equals("123456", adminLoginDTO.getPassword())) {
            return Result.error("账号密码错误");
        } else {
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("username", adminLoginDTO.getUsername());
            claims.put("password", adminLoginDTO.getPassword());
            String token = JWTUtil.createJWT(claims);
            System.out.println("生成的token:  "+token);
            return Result.success(token);
        }
    }

}
