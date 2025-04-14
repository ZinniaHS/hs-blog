package com.hs.blog.controller;

import com.hs.blog.pojo.dto.AdminLoginDTO;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.result.Result;
import io.swagger.annotations.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Slf4j
public class LoginController {

    @PostMapping("/login")
    @CrossOrigin
    public Result login(@RequestBody AdminLoginDTO adminLoginDTO) {
        System.out.println(adminLoginDTO);
        if (!Objects.equals("admin", adminLoginDTO.getUsername()) || !Objects.equals("123456", adminLoginDTO.getPassword())) {
            System.out.println("账号密码错误");
            return Result.error("账号密码错误");
        } else {
            System.out.println("成功");
            System.out.println(Result.success("登录成功"));
            return Result.success("登录成功");
        }
    }

}
