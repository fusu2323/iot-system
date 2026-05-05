package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * 使用排行 VO
 */
@Schema(description = "使用排行响应")
public class UsageRankingVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "排行列表")
    private java.util.List<RankItem> rankings;

    public java.util.List<RankItem> getRankings() { return rankings; }
    public void setRankings(java.util.List<RankItem> rankings) { this.rankings = rankings; }

    @Schema(description = "排行条目")
    public static class RankItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "目标 ID")
        private Long targetId;

        @Schema(description = "目标名称")
        private String targetName;

        @Schema(description = "目标类型：DEVICE/SCENE")
        private String targetType;

        @Schema(description = "使用次数")
        private Long count;

        public RankItem() {}
        public RankItem(Long targetId, String targetName, String targetType, Long count) {
            this.targetId = targetId;
            this.targetName = targetName;
            this.targetType = targetType;
            this.count = count;
        }

        public Long getTargetId() { return targetId; }
        public void setTargetId(Long targetId) { this.targetId = targetId; }
        public String getTargetName() { return targetName; }
        public void setTargetName(String targetName) { this.targetName = targetName; }
        public String getTargetType() { return targetType; }
        public void setTargetType(String targetType) { this.targetType = targetType; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }
}
