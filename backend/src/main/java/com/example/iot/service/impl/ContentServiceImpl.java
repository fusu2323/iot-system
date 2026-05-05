package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.common.exception.BusinessException;
import com.example.iot.common.result.ResultCode;
import com.example.iot.dto.ContentCreateDTO;
import com.example.iot.entity.Content;
import com.example.iot.mapper.ContentMapper;
import com.example.iot.service.ContentService;
import com.example.iot.service.OperationLogService;
import com.example.iot.vo.ContentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 内容服务实现类
 */
@Service
public class ContentServiceImpl implements ContentService {

    private static final Logger log = LoggerFactory.getLogger(ContentServiceImpl.class);

    private final ContentMapper contentMapper;
    private final OperationLogService operationLogService;

    public ContentServiceImpl(ContentMapper contentMapper, OperationLogService operationLogService) {
        this.contentMapper = contentMapper;
        this.operationLogService = operationLogService;
    }

    @Override
    public IPage<ContentVO> list(Integer page, Integer size, String keyword, String type) {
        Page<Content> contentPage = new Page<>(page, size);
        LambdaQueryWrapper<Content> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Content::getDeleted, 0);

        if (StringUtils.hasText(keyword)) {
            wrapper.like(Content::getTitle, keyword);
        }
        if (StringUtils.hasText(type)) {
            wrapper.eq(Content::getType, type);
        }
        wrapper.orderByDesc(Content::getCreateTime);

        IPage<Content> resultPage = contentMapper.selectPage(contentPage, wrapper);
        return resultPage.convert(this::convertToVO);
    }

    @Override
    public ContentVO getById(Long id) {
        Content content = contentMapper.selectById(id);
        if (content == null || content.getDeleted() == 1) {
            throw new BusinessException(ResultCode.CONTENT_NOT_FOUND);
        }
        return convertToVO(content);
    }

    @Override
    @Transactional
    public Long create(ContentCreateDTO dto) {
        Content content = new Content();
        content.setTitle(dto.getTitle());
        content.setType(dto.getType());
        content.setGenre(dto.getGenre());
        content.setCover(dto.getCover());
        content.setDescription(dto.getDescription());
        content.setRating(dto.getRating());

        contentMapper.insert(content);

        log.info("内容创建成功：{}", dto.getTitle());

        // 记录操作日志
        operationLogService.log(null, "CREATE", "CONTENT", content.getId(), null);

        return content.getId();
    }

    @Override
    @Transactional
    public ContentVO update(Long id, ContentCreateDTO dto) {
        Content content = contentMapper.selectById(id);
        if (content == null || content.getDeleted() == 1) {
            throw new BusinessException(ResultCode.CONTENT_NOT_FOUND);
        }

        if (StringUtils.hasText(dto.getTitle())) {
            content.setTitle(dto.getTitle());
        }
        if (StringUtils.hasText(dto.getType())) {
            content.setType(dto.getType());
        }
        if (dto.getGenre() != null) {
            content.setGenre(dto.getGenre());
        }
        if (dto.getCover() != null) {
            content.setCover(dto.getCover());
        }
        if (dto.getDescription() != null) {
            content.setDescription(dto.getDescription());
        }
        if (dto.getRating() != null) {
            content.setRating(dto.getRating());
        }

        contentMapper.updateById(content);

        log.info("内容信息更新成功：{}", id);

        return convertToVO(content);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Content content = contentMapper.selectById(id);
        if (content == null || content.getDeleted() == 1) {
            throw new BusinessException(ResultCode.CONTENT_NOT_FOUND);
        }

        contentMapper.deleteById(id);

        log.info("内容删除成功：{}", id);

        // 记录操作日志
        operationLogService.log(null, "DELETE", "CONTENT", id, null);
    }

    /**
     * 实体转 VO
     */
    private ContentVO convertToVO(Content content) {
        ContentVO vo = new ContentVO();
        vo.setId(content.getId());
        vo.setTitle(content.getTitle());
        vo.setType(content.getType());
        vo.setGenre(content.getGenre());
        vo.setCover(content.getCover());
        vo.setDescription(content.getDescription());
        vo.setRating(content.getRating());
        vo.setCreateTime(content.getCreateTime());
        vo.setUpdateTime(content.getUpdateTime());
        return vo;
    }
}
