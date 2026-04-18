package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.common.exception.BusinessException;
import com.example.iot.common.result.ResultCode;
import com.example.iot.dto.LoginRequest;
import com.example.iot.dto.UserRegisterDTO;
import com.example.iot.dto.UserUpdateDTO;
import com.example.iot.entity.User;
import com.example.iot.mapper.UserMapper;
import com.example.iot.security.JwtUserDetailsService;
import com.example.iot.service.OperationLogService;
import com.example.iot.service.UserService;
import com.example.iot.util.JwtUtil;
import com.example.iot.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final OperationLogService operationLogService;
    private final JwtUserDetailsService jwtUserDetailsService;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil, AuthenticationManager authenticationManager,
                          OperationLogService operationLogService, JwtUserDetailsService jwtUserDetailsService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.operationLogService = operationLogService;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    @Transactional
    public Long register(UserRegisterDTO dto) {
        // 检查用户名是否存在
        User existingUser = userMapper.selectByUsername(dto.getUsername());
        if (existingUser != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }

        // 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(StringUtils.hasText(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
        user.setRole("USER"); // 默认普通用户

        userMapper.insert(user);

        log.info("用户注册成功：{}", dto.getUsername());

        // 记录操作日志
        operationLogService.log(user.getId(), "REGISTER", "USER", user.getId(), null);

        return user.getId();
    }

    @Override
    public String login(LoginRequest request) {
        try {
            // Spring Security 认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // 查询用户信息
            User user = userMapper.selectByUsername(request.getUsername());
            if (user == null) {
                throw new BusinessException(ResultCode.USER_NOT_FOUND);
            }

            // 生成 JWT Token
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(userDetails, user.getId());

            log.info("用户登录成功：{}", request.getUsername());

            // 记录操作日志
            operationLogService.log(user.getId(), "LOGIN", "USER", user.getId(), null);

            return token;
        } catch (BadCredentialsException e) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
    }

    @Override
    public UserVO getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return convertToVO(user);
    }

    @Override
    public UserVO getByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return convertToVO(user);
    }

    @Override
    public IPage<UserVO> list(Integer page, Integer size, String keyword, Long currentUserId, String currentUserRole) {
        Page<User> userPage = new Page<>(page, size);

        List<User> users;
        if ("ADMIN".equals(currentUserRole)) {
            // Admin: return all users matching keyword (if provided)
            if (StringUtils.hasText(keyword)) {
                users = lambdaQuery()
                    .eq(User::getDeleted, 0)
                    .and(wrapper -> wrapper.like(User::getUsername, keyword)
                        .or().like(User::getNickname, keyword))
                    .list();
            } else {
                users = lambdaQuery().eq(User::getDeleted, 0).list();
            }
        } else {
            // Normal user: return only themselves
            users = lambdaQuery().eq(User::getId, currentUserId).eq(User::getDeleted, 0).list();
        }

        return userPage.setRecords(users).convert(this::convertToVO);
    }

    @Override
    @Transactional
    public UserVO update(Long id, UserUpdateDTO dto) {
        User user = userMapper.selectById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 更新用户信息
        if (StringUtils.hasText(dto.getNickname())) {
            user.setNickname(dto.getNickname());
        }
        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
        }

        userMapper.updateById(user);

        log.info("用户信息更新成功：{}", id);

        // 记录操作日志
        operationLogService.log(id, "UPDATE", "USER", id, null);

        return convertToVO(user);
    }

    @Override
    @Transactional
    public void delete(Long id, Long currentUserId, String currentUserRole) {
        User user = userMapper.selectById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if ("ADMIN".equals(currentUserRole)) {
            // Admin: cannot delete admin accounts or themselves
            if ("ADMIN".equals(user.getRole())) {
                throw new BusinessException(ResultCode.FORBIDDEN, "不能删除管理员账户");
            }
            if (user.getId().equals(currentUserId)) {
                throw new BusinessException(ResultCode.FORBIDDEN, "不能删除自己的账户");
            }
        } else {
            // Normal user: can only delete themselves — but this endpoint is ADMIN-only via @PreAuthorize
            throw new BusinessException(ResultCode.FORBIDDEN, "只能管理员才能删除用户");
        }

        // 逻辑删除
        userMapper.deleteById(id);

        log.info("用户删除成功：{}", id);

        // 记录操作日志
        operationLogService.log(id, "DELETE", "USER", id, null);
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 实体转 VO
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setRole(user.getRole());
        vo.setCreateTime(user.getCreateTime());
        vo.setUpdateTime(user.getUpdateTime());
        return vo;
    }
}
