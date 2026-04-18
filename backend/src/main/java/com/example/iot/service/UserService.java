package com.example.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.dto.LoginRequest;
import com.example.iot.dto.UserRegisterDTO;
import com.example.iot.dto.UserUpdateDTO;
import com.example.iot.entity.User;
import com.example.iot.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param dto 注册信息
     * @return 用户 ID
     */
    Long register(UserRegisterDTO dto);

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应（包含 Token）
     */
    String login(LoginRequest request);

    /**
     * 根据 ID 查询用户
     *
     * @param id 用户 ID
     * @return 用户信息
     */
    UserVO getById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserVO getByUsername(String username);

    /**
     * 分页查询用户列表
     *
     * @param page 页码
     * @param size 每页大小
     * @param keyword 搜索关键词
     * @param currentUserId 当前用户ID
     * @param currentUserRole 当前用户角色
     * @return 用户列表
     */
    IPage<UserVO> list(Integer page, Integer size, String keyword, Long currentUserId, String currentUserRole);

    /**
     * 更新用户信息
     *
     * @param id 用户 ID
     * @param dto 更新信息
     * @return 用户信息
     */
    UserVO update(Long id, UserUpdateDTO dto);

    /**
     * 删除用户
     *
     * @param id 用户 ID
     * @param currentUserId 当前用户ID
     * @param currentUserRole 当前用户角色
     */
    void delete(Long id, Long currentUserId, String currentUserRole);

    /**
     * 根据 ID 查询用户实体
     *
     * @param id 用户 ID
     * @return 用户实体
     */
    User getUserById(Long id);

    /**
     * 根据用户名查询用户实体
     *
     * @param username 用户名
     * @return 用户实体
     */
    User getUserByUsername(String username);
}
