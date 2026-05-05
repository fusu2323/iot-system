# Requirements: 智能家居娱乐管理系统 v2.0

**Defined:** 2026-05-05
**Core Value:** 增加定时任务和使用统计功能，提升系统自动化能力和数据分析能力

## v1 Requirements

### 定时任务 (Scheduled Tasks)

- [ ] **SCHED-01**: 管理员可创建定时任务，指定关联场景和执行时间计划
- [ ] **SCHED-02**: 支持多种时间类型：每日（每天固定时间）、工作日（周一至周五）、周末（周六日）、自定义cron表达式
- [ ] **SCHED-03**: 系统按cron表达式在指定时间自动触发关联场景
- [ ] **SCHED-04**: 管理员可单独启用/禁用每个定时任务
- [ ] **SCHED-05**: 定时任务触发后记录执行结果（成功/失败）和时间

### 使用统计 (Usage Analytics)

- [ ] **STATS-01**: 系统自动记录每次设备激活（status变为1）作为使用事件
- [ ] **STATS-02**: 系统自动记录每次场景触发（trigger调用）作为使用事件
- [ ] **STATS-03**: 用户可查看自己的设备和场景的使用次数统计（按日/周/月）
- [ ] **STATS-04**: 管理员可查看全局设备/场景使用统计概览（使用次数排行）
- [ ] **STATS-05**: 统计支持查看单条时间线（某设备/场景的历史激活记录）

## v2 Requirements (Deferred)

### 通知系统
- **NOTIF-01**: 场景触发时发送应用内通知
- **NOTIF-02**: 定时任务失败时通知管理员

### 设备分组/区域
- **ZONE-01**: 按房间/区域对设备分组管理
- **ZONE-02**: 场景可按区域批量控制设备

### 场景模板
- **TEMPLATE-01**: 提供预设场景模板（如"影院模式"、"派对模式"）
- **TEMPLATE-02**: 用户可保存自己的场景为模板

## Out of Scope

| Feature | Reason |
|---------|--------|
| 物联网设备真实通信 | 本系统专注软件逻辑，不涉及真实硬件 |
| 微信小程序开发 | 已列入未来工作计划 |
| 实时通知推送 | 下个版本考虑 |
| 数据导出/报表 | 下个版本考虑 |

## Traceability

| Requirement | Phase | Status |
|-------------|-------|--------|
| SCHED-01 | Phase 20 | Pending |
| SCHED-02 | Phase 20 | Pending |
| SCHED-03 | Phase 21 | Pending |
| SCHED-04 | Phase 20 | Pending |
| SCHED-05 | Phase 21 | Pending |
| STATS-01 | Phase 22 | Pending |
| STATS-02 | Phase 22 | Pending |
| STATS-03 | Phase 23 | Pending |
| STATS-04 | Phase 23 | Pending |
| STATS-05 | Phase 23 | Pending |

**Coverage:**
- v1 requirements: 10 total
- Mapped to phases: 10
- Unmapped: 0 ✓

---
*Requirements defined: 2026-05-05*
*Last updated: 2026-05-05 after v2.0 milestone start*