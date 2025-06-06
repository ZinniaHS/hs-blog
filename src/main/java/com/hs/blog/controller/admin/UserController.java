package com.hs.blog.controller.admin;

import com.hs.blog.pojo.dto.UserDetailDTO;
import com.hs.blog.pojo.dto.UserPageQueryDTO;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.pojo.vo.UserInfoVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理端用户接口")
@RestController("admin-userController")
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 管理端分页查询所有用户
     * @param userPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "管理端分页查询所有用户")
    public Result<PageResult> userPage(UserPageQueryDTO userPageQueryDTO) {
        PageResult result = userService.userPage(userPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/getUserById")
    @Operation(summary = "获取用户信息")
    public Result<User> getUserInfoById(@RequestParam("id") Integer id) {
        return userService.getUserById(id);
    }

    /**
     * 修改用户个人信息
     * @param userDetailDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "修改用户个人信息")
    public Result updateUserDetail(@RequestBody UserDetailDTO userDetailDTO) {
        userService.updateUserDetail(userDetailDTO);
        return Result.success();
    }

    /**
     * 更新用户状态
     * @param lockStatus
     * @param id
     * @return
     */
    @PostMapping("/lockStatus/{lockStatus}")
    @Operation(summary = "更新用户状态")
    public Result updateUserLockStatus(@PathVariable("lockStatus") Integer lockStatus,
                                       @RequestParam("id") Integer id) {
        userService.updateUserLockStatus(lockStatus,id);
        return Result.success();
    }

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    @DeleteMapping("/batchDelete")
    @Operation(summary = "批量删除用户")
    public Result batchDeleteUser(@RequestParam("ids") List<Integer> ids) {
        userService.batchDeleteUser(ids);
        return Result.success();
    }

}
