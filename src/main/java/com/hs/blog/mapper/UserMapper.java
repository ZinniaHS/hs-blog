package com.hs.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.pojo.vo.UserInfoVO;
import com.hs.blog.pojo.vo.UserSubscribeBloggerVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {


    UserInfoVO getUserInfoById(Integer id);

    /**
     * 关注博主
     * @param bloggerId,followerId
     * @return
     */
    @Insert("insert into `hs-blog`.user_follower(user_id, follower_id) values (#{bloggerId},#{followerId});")
    void subscribeBlogger(Integer bloggerId, Integer followerId);

    /**
     * 取关博主
     * @param bloggerId
     * @return
     */
    @Delete("delete from `hs-blog`.user_follower where user_id = #{bloggerId} and follower_id = #{followerId}")
    void unsubscribeBlogger(Integer bloggerId, Integer followerId);

    /**
     * 获取关注的博主列表
     * @param userId
     * @return
     */
    List<UserSubscribeBloggerVO> getSubscribedBlogger(Integer userId);
}
