# 小程序图片素材资源清单

> 用于微信小程序客户端的图标和图片资源，包含命名规范、尺寸要求和 AI 生成提示词

---

## 设计规范

### 色彩规范
| 用途 | 色值 | 说明 |
|------|------|------|
| 主色 | `#5A5D43` | 橄榄绿，用于选中状态 |
| 背景色 | `#F5F5F0` | 燕麦色，页面背景 |
| 卡片背景 | `#FFFFFF` | 纯白，卡片容器 |
| 文字主色 | `#333333` | 深灰，主要文字 |
| 文字次要色 | `#666666` | 中灰，次要文字 |
| 文字提示色 | `#999999` | 浅灰，提示文字 |
| 边框色 | `#E8E8E3` | 浅米色，边框分割线 |

### 图标风格
- **风格**: 线性图标 / 面性图标结合
- **圆角**: 统一使用圆角设计
- **线条粗细**: 2px (线性图标)
- **视觉风格**: 简约、温暖、家居感

---

## TabBar 图标资源

### 尺寸规范
- **图标尺寸**: 81px × 81px (推荐)
- **文件大小**: 每图标 < 40KB
- **格式**: PNG (透明背景)
- **数量**: 10 个 (5 个页面 × 2 状态)

### 1. 首页 (home)

#### home.png (未选中)
```
尺寸：81px × 81px
颜色：#999999 (灰色线性)
提示词：
minimalist home icon outline, simple line art,
thin stroke 2px, rounded corners, gray color #999999,
white background, clean modern style, app icon,
UI design, flat vector illustration
```

#### home-active.png (选中)
```
尺寸：81px × 81px
颜色：#5A5D43 (橄榄绿填充)
提示词：
minimalist home icon filled, solid shape,
rounded corners, olive green color #5A5D43,
white background, clean modern style, app icon,
UI design, flat vector illustration, warm tone
```

### 2. 设备页 (device)

#### device.png (未选中)
```
尺寸：81px × 81px
颜色：#999999 (灰色线性)
提示词：
smart device icon outline, IoT device symbol,
simple line art, thin stroke 2px, rounded corners,
gray color #999999, white background, clean modern style,
app icon, UI design, flat vector illustration
```

#### device-active.png (选中)
```
尺寸：81px × 81px
颜色：#5A5D43 (橄榄绿填充)
提示词：
smart device icon filled, IoT device symbol,
solid shape, rounded corners, olive green color #5A5D43,
white background, clean modern style, app icon,
UI design, flat vector illustration, warm tone
```

### 3. 场景页 (scene)

#### scene.png (未选中)
```
尺寸：81px × 81px
颜色：#999999 (灰色线性)
提示词：
scene automation icon outline, magic wand or
lightning bolt, simple line art, thin stroke 2px,
rounded corners, gray color #999999, white background,
clean modern style, app icon, UI design
```

#### scene-active.png (选中)
```
尺寸：81px × 81px
颜色：#5A5D43 (橄榄绿填充)
提示词：
scene automation icon filled, magic wand or
lightning bolt, solid shape, rounded corners,
olive green color #5A5D43, white background,
clean modern style, app icon, UI design, warm tone
```

### 4. 推荐页 (recommend)

#### recommend.png (未选中)
```
尺寸：81px × 81px
颜色：#999999 (灰色线性)
提示词：
recommendation icon outline, heart or star with
sparkle, simple line art, thin stroke 2px,
rounded corners, gray color #999999, white background,
clean modern style, app icon, UI design
```

#### recommend-active.png (选中)
```
尺寸：81px × 81px
颜色：#5A5D43 (橄榄绿填充)
提示词：
recommendation icon filled, heart or star with
sparkle, solid shape, rounded corners,
olive green color #5A5D43, white background,
clean modern style, app icon, UI design, warm tone
```

### 5. 我的 (mine)

#### mine.png (未选中)
```
尺寸：81px × 81px
颜色：#999999 (灰色线性)
提示词：
user profile icon outline, person silhouette,
simple line art, thin stroke 2px, rounded corners,
gray color #999999, white background, clean modern style,
app icon, UI design, flat vector illustration
```

#### mine-active.png (选中)
```
尺寸：81px × 81px
颜色：#5A5D43 (橄榄绿填充)
提示词：
user profile icon filled, person silhouette,
solid shape, rounded corners, olive green color #5A5D43,
white background, clean modern style, app icon,
UI design, flat vector illustration, warm tone
```

---

## 功能图标资源

### 场景图标 (Scene Icons)

用于场景卡片展示，增强视觉识别

#### cinema-icon.png (观影模式)
```
尺寸：128px × 128px
颜色：线性渐变 #2D3436 → #4A5568
提示词：
movie theater icon, film reel or cinema clapperboard,
gradient dark gray to slate, rounded soft shape,
warm cozy feeling, home entertainment,
flat illustration, minimalist design
```

#### home-icon.png (回家模式)
```
尺寸：128px × 128px
颜色：线性渐变 #C8E6D0 → #A8D5BA
提示词：
home house icon, simple house shape with door,
gradient mint green to soft green, rounded soft shape,
welcoming warm feeling, flat illustration,
minimalist design, cozy home vibe
```

#### sleep-icon.png (睡眠模式)
```
尺寸：128px × 128px
颜色：线性渐变 #667EEA → #764BA2
提示词：
sleep night icon, crescent moon with stars,
gradient purple to deep purple, rounded soft shape,
calm peaceful feeling, flat illustration,
minimalist design, bedtime theme
```

#### leave-icon.png (离家模式)
```
尺寸：128px × 128px
颜色：线性渐变 #F093FB → #F5576C
提示词：
leave home icon, door with exit arrow or key,
gradient pink to coral red, rounded soft shape,
secure safe feeling, flat illustration,
minimalist design, departure theme
```

#### relax-icon.png (放松模式)
```
尺寸：128px × 128px
颜色：线性渐变 #FFB6C1 → #FF69B4
提示词：
relax meditation icon, lotus flower or zen stone,
gradient light pink to hot pink, rounded soft shape,
calm relaxing feeling, flat illustration,
minimalist design, wellness theme
```

---

## 设备类型图标

用于设备列表展示

#### device-tv.png (电视)
```
尺寸：80px × 80px
颜色：#5A5D43 或根据主题
提示词：
TV television icon, flat screen TV with stand,
simple line art or filled, rounded corners,
olive green or neutral gray, minimalist design,
home entertainment, vector illustration
```

#### device-speaker.png (音响)
```
尺寸：80px × 80px
颜色：#5A5D43 或根据主题
提示词：
speaker audio icon, wireless speaker with sound waves,
simple line art or filled, rounded corners,
olive green or neutral gray, minimalist design,
home audio, vector illustration
```

#### device-light.png (灯光)
```
尺寸：80px × 80px
颜色：#5A5D43 或根据主题
提示词：
smart light bulb icon, LED bulb with glow effect,
simple line art or filled, rounded corners,
olive green or neutral gray, minimalist design,
smart home lighting, vector illustration
```

#### device-ac.png (空调)
```
尺寸：80px × 80px
颜色：#5A5D43 或根据主题
提示词：
air conditioner icon, wall mounted AC unit,
simple line art or filled, rounded corners,
olive green or neutral gray, minimalist design,
home climate control, vector illustration
```

#### device-camera.png (摄像头)
```
尺寸：80px × 80px
颜色：#5A5D43 或根据主题
提示词：
security camera icon, dome or bullet camera,
simple line art or filled, rounded corners,
olive green or neutral gray, minimalist design,
home security, vector illustration
```

#### device-curtain.png (窗帘)
```
尺寸：80px × 80px
颜色：#5A5D43 或根据主题
提示词：
curtain icon, open or closed curtain,
simple line art or filled, rounded corners,
olive green or neutral gray, minimalist design,
smart home automation, vector illustration
```

---

## 内容类型图标

用于推荐内容展示

#### content-movie.png (电影)
```
尺寸：120px × 120px
颜色：渐变蓝紫色系
提示词：
movie film icon, film strip or movie reel,
gradient deep blue to purple, cinematic feeling,
rounded soft shape, flat illustration,
entertainment content, vector design
```

#### content-music.png (音乐)
```
尺寸：120px × 120px
颜色：渐变紫粉色系
提示词：
music note icon, musical note with sparkle,
gradient purple to pink, melodic feeling,
rounded soft shape, flat illustration,
entertainment content, vector design
```

#### content-game.png (游戏)
```
尺寸：120px × 120px
颜色：渐变红色系
提示词：
game controller icon, gamepad or joystick,
gradient red to dark red, gaming excitement,
rounded soft shape, flat illustration,
entertainment content, vector design
```

#### content-book.png (书籍)
```
尺寸：120px × 120px
颜色：渐变绿色系
提示词：
book icon, open book with pages,
gradient green to teal, knowledge feeling,
rounded soft shape, flat illustration,
entertainment content, vector design
```

---

## 用户头像占位图

#### avatar-placeholder.png
```
尺寸：200px × 200px
颜色：渐变橄榄绿色系
提示词：
user avatar placeholder, simple person silhouette
inside circle, gradient olive green tones,
soft warm feeling, rounded shape,
profile default image, vector illustration
```

---

## 空状态插图

#### empty-devices.png (设备为空)
```
尺寸：300px × 300px
风格：温暖插画风
提示词：
empty state illustration, no devices scene,
minimalist room with empty shelf, soft warm colors,
cozy home atmosphere, flat vector illustration,
calm gentle mood, olive green accents
```

#### empty-scenes.png (场景为空)
```
尺寸：300px × 300px
风格：温暖插画风
提示词：
empty state illustration, automation scene concept,
simple magical sparkles with empty frame,
soft warm colors, flat vector illustration,
calm gentle mood, olive green accents
```

#### empty-recommend.png (推荐为空)
```
尺寸：300px × 300px
风格：温暖插画风
提示词：
empty state illustration, content discovery concept,
person browsing with empty screen, soft warm colors,
flat vector illustration, calm gentle mood,
olive green accents
```

---

## 启动页资源

#### launch-image.png
```
尺寸：750px × 1334px (iPhone 标准)
颜色：#F5F5F0 背景 + #5A5D43 Logo
提示词：
app launch screen, minimalist smart home logo
centered on oatmeal beige background, clean elegant,
warm cozy feeling, simple sophisticated design,
no text, brand identity
```

---

## 资源目录结构

```
miniprogram/
├── images/
│   ├── tabBar/
│   │   ├── home.png
│   │   ├── home-active.png
│   │   ├── device.png
│   │   ├── device-active.png
│   │   ├── scene.png
│   │   ├── scene-active.png
│   │   ├── recommend.png
│   │   ├── recommend-active.png
│   │   ├── mine.png
│   │   └── mine-active.png
│   ├── icons/
│   │   ├── scene-cinema.png
│   │   ├── scene-home.png
│   │   ├── scene-sleep.png
│   │   ├── scene-leave.png
│   │   ├── scene-relax.png
│   │   ├── device-tv.png
│   │   ├── device-speaker.png
│   │   ├── device-light.png
│   │   ├── device-ac.png
│   │   ├── device-camera.png
│   │   └── device-curtain.png
│   ├── content/
│   │   ├── content-movie.png
│   │   ├── content-music.png
│   │   ├── content-game.png
│   │   └── content-book.png
│   ├── avatar/
│   │   └── placeholder.png
│   ├── empty/
│   │   ├── empty-devices.png
│   │   ├── empty-scenes.png
│   │   └── empty-recommend.png
│   └── launch/
│       └── launch-image.png
└── ...
```

---

## AI 绘图工具推荐

| 工具 | 适用场景 | 链接 |
|------|----------|------|
| Midjourney | 高质量图标和插图 | midjourney.com |
| DALL-E 3 | 精确控制元素 | openai.com/dall-e |
| Stable Diffusion | 免费本地部署 | stability.ai |
| Adobe Firefly | 商业友好 | firefly.adobe.com |

---

## 使用建议

1. **批量生成**: 使用相同的 seed 和风格参数确保图标一致性
2. **后期处理**: 使用 Figma/Sketch 进行精细调整和统一尺寸
3. **压缩优化**: 使用 TinyPNG 压缩图片，减小小程序包体积
4. **多倍图**: 如需适配高清屏，生成 @2x @3x 版本
5. **命名规范**: 严格遵循命名规范，便于代码引用

---

## 修订记录

| 日期 | 操作 | 说明 |
|------|------|------|
| 2026-03-12 | 创建 | 初始素材清单，包含 TabBar、功能图标、内容图标等 |
