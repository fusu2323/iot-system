package com.example.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.iot.entity.UserPreference;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户偏好 Mapper 接口
 */
@Mapper
public interface UserPreferenceMapper extends BaseMapper<UserPreference> {

    /**
     * 根据用户 ID 查询偏好列表
     */
    @Select("SELECT * FROM user_preference WHERE user_id = #{userId} AND deleted = 0")
    List<UserPreference> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据用户 ID 和内容类型查询偏好
     */
    @Select("SELECT * FROM user_preference WHERE user_id = #{userId} AND content_type = #{contentType} AND deleted = 0")
    UserPreference selectByUserIdAndContentType(@Param("userId") Long userId, @Param("contentType") String contentType);
}
