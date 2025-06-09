package com.hs.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.blog.pojo.dto.BookPageQueryDTO;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.pojo.vo.UserInfoVO;
import com.hs.blog.pojo.vo.UserSubscribeBloggerVO;
import com.hs.blog.result.Result;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 获取用户对该博主的关注状态
     * 进入博主信息页中触发判断，判断是否已经关注该博主
     * @param bloggerId 对象博主
     * @return Boolean true 已关注，false 未关注
     */
    @Select("SELECT EXISTS(SELECT 1 FROM `hs-blog`.user_follower " +
            "WHERE user_id = #{bloggerId} AND follower_id = #{userId})")
    Boolean getSubscribeStatus(Integer bloggerId, Integer userId);

    /**
     * 收藏图书
     * @param bookId
     * @return
     */
    @Insert("INSERT INTO `hs-blog`.user_book (user_id, book_id) VALUES (#{userId}, #{bookId})")
    void collectBook(Integer userId, Integer bookId);

    /**
     * 取消收藏图书
     * @param bookId
     * @return
     */
    @Delete("DELETE FROM `hs-blog`.user_book WHERE user_id = #{userId} AND book_id = #{bookId}")
    void removeCollectBook(Integer userId, Integer bookId);

    /**
     * 查询图书是否已加入书架
     * @param bookId
     * @return
     */
    @Select("SELECT COUNT(1) > 0 FROM `hs-blog`.user_book WHERE user_id = #{userId} AND book_id = #{bookId}")
    Boolean checkCollectBookStatus(Integer userId, Integer bookId);

    /**
     * 查询用户收藏的图书
     * @param bookPageQueryDTO
     * @return
     */
    IPage<Book> getCollectBooks(Page<Book> page, BookPageQueryDTO bookPageQueryDTO, Integer userId);
}
