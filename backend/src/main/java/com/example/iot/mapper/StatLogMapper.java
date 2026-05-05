package com.example.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.entity.StatLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 统计日志 Mapper 接口
 */
@Mapper
public interface StatLogMapper extends BaseMapper<StatLog> {

    /**
     * 按用户和时间维度查询使用统计
     * @param dimension DAY / WEEK / MONTH
     */
    @Select("<script>"
            + "SELECT "
            + "<if test='dimension == \"DAY\"'>DATE(create_time) as stat_key,</if>"
            + "<if test='dimension == \"WEEK\"'>YEARWEEK(create_time, 1) as stat_key,</if>"
            + "<if test='dimension == \"MONTH\"'>DATE_FORMAT(create_time, '%Y-%m') as stat_key,</if>"
            + " COUNT(*) as count "
            + "FROM stat_log "
            + "WHERE user_id = #{userId} "
            + "<if test='targetType != null'>AND target_type = #{targetType}</if>"
            + "<if test='startDate != null'>AND stat_date &gt;= #{startDate}</if>"
            + "<if test='endDate != null'>AND stat_date &lt;= #{endDate}</if>"
            + "GROUP BY stat_key "
            + "ORDER BY stat_key DESC"
            + "</script>")
    List<Map<String, Object>> selectStatsByUserAndDimension(
            @Param("userId") Long userId,
            @Param("dimension") String dimension,
            @Param("targetType") String targetType,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    /**
     * 全局使用排行（管理员）
     * @param targetType DEVICE / SCENE / null（全部）
     * @param limit 返回条数
     */
    @Select("<script>"
            + "SELECT target_id, target_type, COUNT(*) as count "
            + "FROM stat_log "
            + "<if test='targetType != null'>WHERE target_type = #{targetType}</if>"
            + "GROUP BY target_id, target_type "
            + "ORDER BY count DESC "
            + "LIMIT #{limit}"
            + "</script>")
    List<Map<String, Object>> selectGlobalRanking(
            @Param("targetType") String targetType,
            @Param("limit") Integer limit);

    /**
     * 单条设备/场景的历史激活时间线
     */
    @Select("SELECT * FROM stat_log "
            + "WHERE target_type = #{targetType} AND target_id = #{targetId} "
            + "ORDER BY create_time DESC")
    IPage<StatLog> selectTimeline(Page<StatLog> page,
                                   @Param("targetType") String targetType,
                                   @Param("targetId") Long targetId);
}
