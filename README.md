# Astral Sorcery JEI Fix / 星辉魔法 JEI 修复

给**星辉魔法**（Astral Sorcery）的 JEI「祭坛合成」页换一套更紧凑的版式。

原版页面背景写死 148×220，叠上 JEI 的标题行后约 250 逻辑像素高；1920×1080 在 GUI scale 4 下只有 270 像素可用，页面几乎贴满上下边缘，产物、额外输入和流体的第二行容易被挤到看不见。这个附属不改星辉任何类，只注册自己的分类并把原页面隐藏。

| 项目 | 值 |
|---|---|
| 模组 ID | `astral_jei_fix` |
| 依赖 | Minecraft 1.21.1、NeoForge 21.1.x、Astral Sorcery 2.0.3+、JEI 19.x |
| 环境 | 仅客户端需要（页面在客户端渲染） |
| 许可证 | MIT |

## 做了什么

| 区域 | 内容 |
|---|---|
| 左栏 | 按祭坛等级换用不同的方阵底图（启明 / 共振 / 共振扩展 / 辉光 / 虹彩），用宝典自己的格位坐标摆 3×3 工作格与内外两圈继电器，上方写祭坛名字与图标 |
| 右栏上 | 流明条（沿用星辉注册的 JEI 流明配料与渲染器）、产物坐在宝典八角星中心 |
| 右栏下 | 额外输入、所需流体，各带数量与流体容量 |
| 提示 | ℹ️ 用星辉自己的文案键：仅夜间、需要哪个星座的定焦物、碎裂概率、聚焦星能来源；只在配方真有额外条件时出现 |
| 产物 | 用「展示用输出」，把星辉挂在产物上的词条说明（增加魔咒词条 / 提升魔咒等级 / 增加星能力词条）一起带出来 |

## 构建

```bash
./gradlew build
```

依赖不随仓库分发，编译前需要把两个 jar 放进 `libs/`：

- `astralsorcery-<版本>.jar`
- `jei-1.21.1-neoforge-<版本>.jar`

产物在 `build/libs/`。

## 已知边界

- 继电器格位沿用宝典页的坐标，与宝典自己的排布一致（物品会压住十字标记）。
- 只替换「祭坛合成」页；星辉其它 JEI 分类不受影响。
- 原页面在 `onRuntimeAvailable` 里用 `hideRecipeCategory` 隐藏，未使用 mixin。

## English

A client-side JEI addon that replaces Astral Sorcery's cramped 148×220 altar recipe page with a compact 262×172 layout: per-tier altar diagrams, lumen bars drawn with Astral's own JEI ingredient renderer, the result sitting on the tome's 8-point star, and Astral's own tooltip keys for night / focus constellation / shatter chance / focused starlight. No mixins into Astral Sorcery; the stock page is simply hidden with `hideRecipeCategory`.

## 相关

- 上游问题：HellFirePvP/AstralSorcery#2048
