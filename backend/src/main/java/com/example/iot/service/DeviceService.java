package com.example.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.dto.DeviceCreateDTO;
import com.example.iot.dto.DeviceStatusUpdateDTO;
import com.example.iot.dto.DeviceUpdateDTO;
import com.example.iot.vo.DeviceStatisticsVO;
import com.example.iot.vo.DeviceVO;

import java.util.List;

/**
 * 设备服务接口
 */
public interface DeviceService {

    /**
     * 分页查询设备列表
     *
     * @param page 页码
     * @param size 每页大小
     * @param keyword 搜索关键词
     * @param type 设备类型
     * @param room 房间
     * @param status 状态
     * @param isOnline 在线状态
     * @param userId 用户 ID
     * @return 设备列表
     */
    IPage<DeviceVO> list(Integer page, Integer size, String keyword, String type, String room,
                          Integer status, Integer isOnline, Long userId);

    /**
     * 根据 ID 查询设备详情
     *
     * @param id 设备 ID
     * @return 设备信息
     */
    DeviceVO getById(Long id);

    /**
     * 创建设备
     *
     * @param dto 创建请求
     * @param userId 用户 ID
     * @return 设备 ID
     */
    Long create(DeviceCreateDTO dto, Long userId);

    /**
     * 更新设备信息
     *
     * @param id 设备 ID
     * @param dto 更新请求
     * @return 设备信息
     */
    DeviceVO update(Long id, DeviceUpdateDTO dto);

    /**
     * 删除设备
     *
     * @param id 设备 ID
     */
    void delete(Long id);

    /**
     * 更新设备状态
     *
     * @param id 设备 ID
     * @param dto 状态更新请求
     * @return 设备信息
     */
    DeviceVO updateStatus(Long id, DeviceStatusUpdateDTO dto);

    /**
     * 统计各类型设备数量
     *
     * @return 统计列表
     */
    List<DeviceStatisticsVO> countByType();

    /**
     * 统计各房间设备数量
     *
     * @return 统计列表
     */
    List<DeviceStatisticsVO> countByRoom();

    /**
     * 统计在线设备数量
     *
     * @return 在线数量
     */
    Integer countOnline();

    /**
     * 统计设备总数
     *
     * @return 设备总数
     */
    Integer countTotal();
}
