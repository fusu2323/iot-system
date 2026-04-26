package com.example.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.dto.ContentCreateDTO;
import com.example.iot.vo.ContentVO;

/**
 * 内容服务接口
 */
public interface ContentService {

    /**
     * 分页查询内容列表
     *
     * @param page 页码
     * @param size 每页大小
     * @param keyword 搜索关键词
     * @param type 内容类型
     * @return 内容列表
     */
    IPage<ContentVO> list(Integer page, Integer size, String keyword, String type);

    /**
     * 根据 ID 查询内容详情
     *
     * @param id 内容 ID
     * @return 内容信息
     */
    ContentVO getById(Long id);

    /**
     * 创建内容
     *
     * @param dto 创建请求
     * @return 内容 ID
     */
    Long create(ContentCreateDTO dto);

    /**
     * 更新内容信息
     *
     * @param id 内容 ID
     * @param dto 更新请求
     * @return 内容信息
     */
    ContentVO update(Long id, ContentCreateDTO dto);

    /**
     * 删除内容
     *
     * @param id 内容 ID
     */
    void delete(Long id);
}
