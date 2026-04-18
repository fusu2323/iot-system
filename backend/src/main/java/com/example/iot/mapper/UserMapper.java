package com.example.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.iot.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM user WHERE username = #{username} AND deleted = 0")
    User selectByUsername(@Param("username") String username);

    /**
     * 根据微信 openid 查询用户
     */
    @Select("SELECT * FROM user WHERE openid = #{openid} AND deleted = 0")
    User selectByOpenid(@Param("openid") String openid);

    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT * FROM user WHERE email = #{email} AND deleted = 0")
    User selectByEmail(@Param("email") String email);
}
