# Astral Sorcery JEI Fix

A client-side JEI addon that replaces the Astral Sorcery altar crafting page with a more compact layout.

The stock page is a fixed 148×220 background. With JEI's title bar the recipe needs about 250 logical pixels, and a 1920×1080 screen at GUI scale 4 only has 270, so the page is wedged and the second row — extra inputs and fluids — gets clipped. This addon touches no Astral Sorcery class: it registers its own JEI category and hides the stock page.

| Field | Value |
|---|---|
| Mod id | `as_jei_fix` |
| Version | 1.0.2 |
| Requires | Minecraft 1.21.1, NeoForge 21.1.x, Astral Sorcery 2.0.3+, JEI 19.x |
| Side | Client |
| License | MIT |

## What it draws

| Area | Content |
|---|---|
| Left | One altar diagram per tier (illuminated / resonating / resonating expanded / luminous / iridescent), laid out with the tome's own slot coordinates: the 3×3 crafting grid plus the inner and outer rings of focus relays. The altar's icon and name sit above it |
| Right, top | Lumen bars using Astral's own JEI lumen ingredient and renderer; the outputs sit on the tome's eight-pointed star |
| Right, bottom | Additional inputs and required fluids, with counts and capacity |
| Tooltips | The ℹ️ icon reuses Astral's own keys: night only, focus constellation, focus shatter chance, focused starlight sources. It only appears when the recipe actually has one of those |
| Outputs | The "display output" path is used, so the lines Astral adds to the result — one extra enchantment line, +1 enchantment level, one extra gem affix — come along with the item tooltip |

## Building

```bash
./gradlew build
```

The dependencies are not distributed here; drop both jars into `libs/` first:

- `astralsorcery-<version>.jar`
- `jei-1.21.1-neoforge-<version>.jar`

The jar ends up in `build/libs/`.

## Notes

- Diagrams and slot coordinates are taken from the tome's own altar page (`RenderPageAltar`), so items cover their cross markers exactly as in the book; the crosses you can still see belong to empty slots.
- Only the "Altar Crafting" category is replaced; other Astral Sorcery JEI categories are untouched.
- The stock page is hidden with `hideRecipeCategory` in `onRuntimeAvailable`. No mixins, no access transformers.

## Links

- Upstream issue: HellFirePvP/AstralSorcery#2048
- 中文说明：[README.md](README.md)
