package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

/**
 * 分组推荐响应对象
 */
@Schema(description = "分组推荐响应对象")
public class GroupedRecommendationResponse {

    @Schema(description = "按类型分组的推荐列表")
    private Map<String, GroupedRecommendationVO> groups;

    @Schema(description = "推荐内容总数")
    private Integer total;

    public Map<String, GroupedRecommendationVO> getGroups() { return groups; }
    public void setGroups(Map<String, GroupedRecommendationVO> groups) { this.groups = groups; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}
