# Phase 10: 内容数据扩充 - Context

**Gathered:** 2026-04-18
**Status:** Ready for planning

<domain>
## Phase Boundary

实现 CONTENT-01, CONTENT-02, CONTENT-03：补充电影/音乐/游戏示例数据，每个类型增加8条新记录。

**This phase delivers:**
- 电影类型增加8条示例数据（现有4条 + 新增8条 = 12条）
- 音乐类型增加8条示例数据（现有2条 + 新增8条 = 10条）
- 游戏类型增加8条示例数据（现有2条 + 新增8条 = 10条）
- 通过 Flyway migration (V1.5__expand_content_data.sql) 实现

</domain>

<decisions>
## Implementation Decisions

### Content Authenticity
- **D-01:** 使用真实标题（actual movies/music/games）
  - 电影：使用真实知名电影名称（如 阿凡达2, 泰坦尼克号等）
  - 音乐：使用真实歌曲/专辑名称（如 泰勒丝专辑、周杰伦歌曲等）
  - 游戏：使用真实游戏名称（如 塞尔达、使命召唤等）
  - 更真实的演示效果，sample data 常见做法

### Quantity Per Type
- **D-02:** 每个类型增加8条新记录
  - 电影：现有4条 → 新增8条 → 总计12条
  - 音乐：现有2条 → 新增8条 → 总计10条
  - 游戏：现有2条 → 新增8条 → 总计10条
  - 平衡选择，不过多也不过少

### Idempotency Pattern
- **D-03:** 使用 `INSERT ... WHERE NOT EXISTS` 模式
  - 遵循 V1.0__create_scene_content_tables.sql 的现有模式
  - 可以安全地重复运行，不会产生重复数据
  - 每次 INSERT 前检查 title 是否已存在

### Migration File
- **D-04:** 新建 `V1.5__expand_content_data.sql` migration 文件
  - 参考 V1.0 的 INSERT ... WHERE NOT EXISTS 语法
  - 每个 content 插入独立 INSERT 语句
  - 使用 `NOW()` 作为 create_time 和 update_time

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Core Project Files
- `.planning/PROJECT.md` — 项目概述、技术栈
- `.planning/REQUIREMENTS.md` — Phase 10 需求：CONTENT-01, CONTENT-02, CONTENT-03
- `.planning/ROADMAP.md` — Phase 10 目标：内容数据扩充

### Prior Phase Context
- `.planning/phases/09-scene-device/09-CONTEXT.md` — Phase 9 场景设备联动完整上下文
- `.planning/phases/08-scene-mutex/08-CONTEXT.md` — Phase 8 场景互斥机制完整上下文

### Existing Code
- `backend/src/main/java/com/example/iot/entity/Content.java` — Content 实体定义（id, title, type, cover, description, rating）
- `backend/src/main/resources/db/init.sql` — 现有8条 content 数据（4 movies, 2 music, 2 games）
- `backend/src/main/resources/db/migration/V1.0__create_scene_content_tables.sql` — WHERE NOT EXISTS idempotency pattern 示例
- `backend/src/main/resources/db/migration/V1.4__add_scene_mutex_group.sql` — 最新 migration 文件参考

</canonical_refs>

<codebase_context>
## Existing Code Insights

### Content Entity Structure
- id: BIGINT AUTO_INCREMENT
- title: VARCHAR(100) — 内容标题
- type: VARCHAR(20) — MOVIE/MUSIC/GAME
- cover: VARCHAR(255) — 封面 URL
- description: TEXT — 内容描述
- rating: DECIMAL(2,1) — 0.0-5.0 评分
- deleted, create_time, update_time — 标准审计字段

### Established Patterns (from V1.0)
- Idempotent INSERT: `INSERT INTO content (...) SELECT 'title', ... WHERE NOT EXISTS (SELECT 1 FROM content WHERE title = 'title')`
- 使用 `NOW()` 函数填充时间戳
- 每条记录独立 INSERT 语句

### Integration Points
- Migration file: `backend/src/main/resources/db/migration/V1.5__expand_content_data.sql`
- 无需修改 Java 代码，纯数据迁移

</codebase_context>

<specifics>
## Specific Ideas

**Real title examples (for reference):**
- Movies: 阿凡达2, 泰坦尼克号, 复仇者联盟, 盗梦空间, 星际穿越, 速度与激情, 变形金刚, 侏罗纪世界
- Music: 泰勒丝 - Anti-Hero, 碧昂丝 - Crazy in Love, 周杰伦 - 夜曲, 林俊杰 - 可惜没如果,五月天 - 倔强
- Games: 使命召唤, 守望先锋, 我的世界, GTA5, 巫师3, 黑暗之魂, 艾尔登法环, 超级马里奥奥德赛

</specifics>

<deferred>
## Deferred Ideas

None — discussion stayed within phase scope.

</deferred>

---
*Phase: 10-content-data*
*Context gathered: 2026-04-18*
