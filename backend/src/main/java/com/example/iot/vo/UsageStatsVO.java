package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;

/**
 * 使用统计 VO
 */
@Schema(description = "使用统计响应")
public class UsageStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "目标类型：DEVICE/SCENE")
    private String targetType;

    @Schema(description = "统计维度：DAY/WEEK/MONTH")
    private String dimension;

    @Schema(description = "统计列表")
    private List<StatItem> statistics;

    @Schema(description = "统计条目")
    public static class StatItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "统计时间键（日期/周/年周/月）")
        private String statKey;

        @Schema(description = "使用次数")
        private Long count;

        public StatItem() {}
        public StatItem(String statKey, Long count) {
            this.statKey = statKey;
            this.count = count;
        }

        public String getStatKey() { return statKey; }
        public void setStatKey(String statKey) { this.statKey = statKey; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public String getDimension() { return dimension; }
    public void setDimension(String dimension) { this.dimension = dimension; }
    public List<StatItem> getStatistics() { return statistics; }
    public void setStatistics(List<StatItem> statistics) { this.statistics = statistics; }
}
