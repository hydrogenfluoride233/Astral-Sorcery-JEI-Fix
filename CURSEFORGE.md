This mod reworks the JEI "Altar Crafting" page of **Astral Sorcery** into a more compact layout, so the whole recipe stays visible at GUI scale 4.

<img width="1920" height="1080" alt="screenshot" src="https://github.com/user-attachments/assets/fc47692f-0fb3-4846-96b4-dd330c3d626d" />

**What you get**

- One altar diagram per tier — illuminated, resonating, resonating expanded, luminous and iridescent — with the altar's icon and name above it
- Lumen bars drawn with Astral's own JEI lumen ingredient and renderer, and the output sitting on the tome's eight-pointed star
- Additional inputs and required fluids, with counts and capacity
- A ℹ️ icon that reuses Astral's own tooltip keys: night only, focus constellation, focus shatter chance, focused starlight sources
- Outputs shown through the display-output path, so the lines Astral adds to the result (extra enchantment / +1 enchantment level / extra gem affix) come along

**Requirements**

- Minecraft 1.21.1, NeoForge 21.1.x
- Astral Sorcery 2.0.3+, Just Enough Items 19.x
- Client-side only

**Notes**

- Slot coordinates are taken from the tome's own altar page, so items cover their cross markers exactly as in the book; crosses you can still see belong to empty slots
- Only the "Altar Crafting" category is replaced; the stock page is hidden with `hideRecipeCategory`, no mixins, no access transformers

**Links**

- Source and issues: https://github.com/hydrogenfluoride233/Astral-Sorcery-JEI-Fix
- Upstream issue this works around: https://github.com/HellFirePvP/AstralSorcery/issues/2048

---

本模组优化了**星辉魔法（Astral Sorcery）** 的 JEI「祭坛合成」页，为其重写了一套更紧凑的版式，使其在 GUI scale 4 下依然能完整显示。

**你会看到**

- 左栏按祭坛等级换用不同的方阵底图（启明 / 共振 / 共振扩展 / 辉光 / 虹彩），上方写祭坛名字与图标
- 流明条沿用星辉自己注册的 JEI 流明配料与渲染器，产物坐在宝典八角星中心
- 额外输入与所需流体，带数量与流体容量
- ℹ️ 沿用星辉自己的提示键：仅可在夜间合成、需要哪个星座的定焦物、定焦物碎裂概率、聚焦星能来源
- 产物走「展示用输出」，把星辉挂在产物上的词条说明（增加魔咒词条 / 将魔咒等级提升至少1级 / 增加1个星能力词条）一起带出来

**需要**

- Minecraft 1.21.1、NeoForge 21.1.x
- 星辉魔法 2.0.3+、JEI 19.x
- 仅客户端

**说明**

- 方阵与格位沿用宝典祭坛页自己的坐标，和宝典里的排布一致（物品会压住十字标记，旁边露出的十字属于空着的格位）
- 只替换「祭坛合成」页，星辉其它 JEI 分类不受影响；原页面用 `hideRecipeCategory` 隐藏，没有 mixin

**相关**

- 仓库与问题反馈：https://github.com/hydrogenfluoride233/Astral-Sorcery-JEI-Fix
- 所绕过的上游问题：https://github.com/HellFirePvP/AstralSorcery/issues/2048
