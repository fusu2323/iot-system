package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 按类型分组的推荐内容视图对象
 */
@Schema(description = "按类型分组的推荐内容视图对象")
public class GroupedRecommendationVO {

    @Schema(description = "内容类型", example = "MOVIE")
    private String type;

    @Schema(description = "该类型的推荐列表")
    private List<RecommendationVO> items;

    @Schema(description = "该类型的总数")
    private Integer total;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<RecommendationVO> getItems() { return items; }
    public void setItems(List<RecommendationVO> items) { this.items = items; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}
