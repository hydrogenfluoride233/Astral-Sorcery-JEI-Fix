A client-side JEI addon that gives Astral Sorcery's altar crafting page a compact layout.

The stock page is a fixed 148×220 background. Add JEI's title bar and the recipe needs about 250 logical pixels, while a 1920×1080 screen at GUI scale 4 only has 270 — so the page is wedged top to bottom and the second row (extra inputs and fluids) gets clipped. This addon touches no Astral Sorcery class: it registers its own JEI category and hides the stock page.

| | |
|---|---|
| Mod id | `as_jei_fix` |
| Version | 1.0.3 |
| Requires | Minecraft 1.21.1, NeoForge 21.1.x, Astral Sorcery 2.0.3+, JEI 19.x |
| Side | Client |
| License | MIT |

## What it draws

| Area | Content |
|---|---|
| Left | One altar diagram per tier — illuminated, resonating, resonating expanded, luminous and iridescent — laid out with the tome's own slot coordinates: the 3×3 crafting grid plus the inner and outer rings of focus relays. The altar's icon and name sit above it |
| Right, top | Lumen bars using Astral's own JEI lumen ingredient and renderer; the outputs sit on the tome's eight-pointed star |
| Right, bottom | Additional inputs and required fluids, with counts and capacity |
| Tooltips | The ℹ️ icon reuses Astral's own keys: night only, focus constellation, focus shatter chance, focused starlight sources. It only appears when the recipe actually has one of those |
| Outputs | The display-output path is used, so the lines Astral adds to the result — one extra enchantment, +1 enchantment level, one extra gem affix — come along with the item tooltip |

## Notes

- Diagrams and slot coordinates come from the tome's own altar page (`RenderPageAltar`), so items cover their cross markers exactly as in the book; crosses still visible belong to empty slots.
- Only the "Altar Crafting" category is replaced; other Astral Sorcery JEI categories are untouched.
- The stock page is hidden with `hideRecipeCategory` in `onRuntimeAvailable`. No mixins, no access transformers.

## Links

- Source and issues: https://github.com/hydrogenfluoride233/Astral-Sorcery-JEI-Fix
- Upstream issue this works around: https://github.com/HellFirePvP/AstralSorcery/issues/2048

---

**中文**

给星辉魔法（Astral Sorcery）的 JEI「祭坛合成」页换一套更紧凑的版式。

原版页面背景写死 148×220，叠上 JEI 的标题行后约 250 逻辑像素高；1920×1080 在 GUI scale 4 下只有 270 像素可用，页面几乎贴满上下边缘，额外输入与流体的第二行容易被挤到看不见。这个附属不改星辉任何类：注册自己的分类，再把原页面隐藏。

- 模组 ID：`as_jei_fix`
- 版本：1.0.3
- 依赖：Minecraft 1.21.1、NeoForge 21.1.x、Astral Sorcery 2.0.3+、JEI 19.x
- 环境：客户端

**做了什么**

- **左栏**：按祭坛等级换用不同的方阵底图（启明 / 共振 / 共振扩展 / 辉光 / 虹彩），用宝典自己的格位坐标摆 3×3 工作格与内外两圈聚星转继台，上方写祭坛名字与图标
- **右栏上**：流明条（沿用星辉注册的 JEI 流明配料与渲染器）、产物坐在宝典八角星中心
- **右栏下**：额外输入、所需流体（显示数量与流体容量）
- **提示**：ℹ️ 用星辉自己的文案键——仅可在夜间合成、需要哪个星座的定焦物、定焦物碎裂概率、需要来自哪些星座的聚焦星能；只在配方真有额外条件时出现
- **产物**：用「展示用输出」，把星辉挂在产物上的词条说明（增加魔咒词条 / 将魔咒等级提升至少1级 / 增加1个星能力词条）一起带出来

**已知边界**

- 方阵与格位沿用宝典祭坛页自己的坐标，和宝典里的排布一致（物品会压住十字标记，旁边露出的十字属于空着的格位）
- 只替换「祭坛合成」页，星辉其它 JEI 分类不受影响
- 原页面在 `onRuntimeAvailable` 里用 `hideRecipeCategory` 隐藏，没有 mixin
