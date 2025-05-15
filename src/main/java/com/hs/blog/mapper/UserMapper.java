package com.hs.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.pojo.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {


    UserInfoVO getUserInfoById(Integer id);
}
