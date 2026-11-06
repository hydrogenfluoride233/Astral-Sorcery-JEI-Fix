package com.hydrogen.asjeifix.client;

import com.hydrogen.asjeifix.AsJeiFix;
import hellfirepvp.astralsorcery.client.ClientProxy;
import hellfirepvp.astralsorcery.common.constellation.BaseConstellation;
import hellfirepvp.astralsorcery.common.ingredient.IngredientBridge;
import hellfirepvp.astralsorcery.common.integration.jei.ingredient.LumenIngredientRenderer;
import hellfirepvp.astralsorcery.common.integration.jei.ingredient.LumenIngredientType;
import hellfirepvp.astralsorcery.common.lumen.LumenStack;
import hellfirepvp.astralsorcery.common.recipe.altar.AltarRecipe;
import hellfirepvp.astralsorcery.common.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.research.ResearchManager;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 祭坛合成页：底用 JEI 自己的面板，左栏按祭坛等级铺各方阵，右栏放流明条、产物、额外输入与流体。
 */
public class AltarFixCategory implements IRecipeCategory<AltarRecipe> {
    private static final int WIDTH = 262;
    private static final int HEIGHT = 172;

    private static final String TEX = "textures/gui/";
    private static final ResourceLocation INFO =
            ResourceLocation.fromNamespaceAndPath(AsJeiFix.MODID, TEX + "info.png");
    private static final ResourceLocation STAR =
            ResourceLocation.fromNamespaceAndPath(AsJeiFix.MODID, TEX + "star.png");
    private static final ResourceLocation BLANK =
            ResourceLocation.fromNamespaceAndPath(AsJeiFix.MODID, TEX + "blank.png");

    // 左栏：方阵中心与各等级的贴图/格位（格位是相对贴图左上角的坐标）
    private static final int PLATE_CENTER_X = 76;
    private static final int PLATE_CENTER_Y = 88;
    private static final int GRID_PITCH = 18;
    private static final int CORE_SPAN = 54;
    private static final int RELAY_CENTER = 12;
    private static final int NAME_Y = 4;

    // 格位一律用宝典祭坛页自己的坐标（RenderPageAltar）：核心 61/118、内圈 42/99、外圈 25/…，
    // 减去本贴图裁切原点 (24,81) 后就是下面这些值
    private static final int[] RELAY_X_3 = {18, 55, 92};         // 共振小方阵：内圈 3 列
    private static final int[] RELAY_Y_3 = {18, 55, 92};
    private static final int[] RELAY_X_5 = {1, 18, 55, 92, 109};
    private static final int[] RELAY_Y_5 = {1, 18, 55, 92, 109};

    // 右栏
    private static final int LUMEN_X = 152;
    private static final int LUMEN_Y = 18;
    private static final int LUMEN_STEP = 10;
    private static final int LUMEN_ROWS = 5;
    private static final int STAR_X = 196;
    private static final int STAR_Y = 12;
    private static final int STAR_W = 57;
    private static final int STAR_H = 53;
    // 产物槽位：让 16×16 的物品正好落在星形中心 (224.5, 38.5)
    private static final int OUTPUT_X = 216;
    private static final int OUTPUT_Y = 30;
    private static final int INFO_X = 219;
    private static final int INFO_Y = 68;
    private static final int INFO_SIZE = 12;
    private static final int COLUMN_X = 152;
    private static final int COLUMN_WIDTH = 5;
    private static final int ADDITIONAL_LABEL_Y = 86;
    private static final int ADDITIONAL_Y = 96;
    private static final int FLUID_LABEL_Y = 138;
    private static final int FLUID_Y = 150;

    private static final int TEXT = 0xFF2B2B2B;
    private static final int LABEL = 0xFF4A4A4A;
    private static final int OUTPUT_SLOTS = 3;

    private final IDrawable icon;
    private final IDrawable blank;

    /** 一个祭坛等级的方阵布局。 */
    private record Plate(ResourceLocation texture, int size, int[] relayX, int[] relayY, int coreStart) {
        int offsetX() {
            return PLATE_CENTER_X - size / 2;
        }

        int offsetY() {
            return PLATE_CENTER_Y - size / 2;
        }
    }

    private static final Plate PLATE_T1 = new Plate(loc("plate_t1"), 128, new int[0], new int[0], 37);
    private static final Plate PLATE_T2 = new Plate(loc("plate_t2"), 128, RELAY_X_3, RELAY_Y_3, 37);
    private static final Plate PLATE_T2E = new Plate(loc("plate_t2e"), 128, RELAY_X_5, RELAY_Y_5, 37);
    private static final Plate PLATE_T3 = new Plate(loc("plate_t3"), 128, RELAY_X_5, RELAY_Y_5, 37);
    private static final Plate PLATE_T4 = new Plate(loc("plate_t4"), 128, RELAY_X_5, RELAY_Y_5, 37);

    public AltarFixCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableItemStack(new ItemStack(altarItem("altar_luminance")));
        this.blank = helper.createDrawable(BLANK, 0, 0, 1, 1);
    }

    private static ResourceLocation loc(String file) {
        return ResourceLocation.fromNamespaceAndPath(AsJeiFix.MODID, TEX + file + ".png");
    }

    private static Item altarItem(String id) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("astralsorcery", id));
    }

    private static Plate plate(AltarRecipe recipe) {
        TileAltar.AltarType type = recipe.getRequiredType();
        return switch (type) {
            case ILLUMINATION -> PLATE_T1;
            // 共振：摆了外圈就换扩展形态那张（照宝典与 appliedas 的做法）
            case RESONANCE -> hasOuterRelays(recipe) ? PLATE_T2E : PLATE_T2;
            case LUMINANCE -> PLATE_T3;
            case RADIANCE -> PLATE_T4;
        };
    }

    private static boolean hasOuterRelays(AltarRecipe recipe) {
        List<IngredientBridge> relays = recipe.getGrid().getRelayInputs();
        for (int index = 0; index < 25 && index < relays.size(); index++) {
            if (index == RELAY_CENTER || relays.get(index).isEmpty()) {
                continue;
            }
            int col = index % 5;
            int row = index / 5;
            boolean inner = col >= 1 && col <= 3 && row >= 1 && row <= 3;
            if (!inner) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RecipeType<AltarRecipe> getRecipeType() {
        return AltarJeiPlugin.ALTAR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.as_jei_fix.altar");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    /** 底交给 JEI 自己的面板，这里只垫一张透明图。 */
    @Override
    public IDrawable getBackground() {
        return blank;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AltarRecipe recipe, IFocusGroup focuses) {
        Plate plate = plate(recipe);
        AltarRecipeGrid grid = recipe.getGrid();

        // 3×3 工作格
        List<IngredientBridge> inputs = grid.getInputs();
        for (int i = 0; i < 9 && i < inputs.size(); i++) {
            IngredientBridge bridge = inputs.get(i);
            if (bridge.isEmpty()) {
                continue;
            }
            builder.addSlot(RecipeIngredientRole.INPUT,
                            plate.offsetX() + plate.coreStart() + (i % 3) * GRID_PITCH,
                            plate.offsetY() + plate.coreStart() + (i / 3) * GRID_PITCH)
                    .addItemStacks(stacksOf(bridge));
        }

        // 聚星转继台：5×5 行主序，内圈 8 个、外圈 16 个；只有 3×3 格位的等级把内圈减一格看
        List<IngredientBridge> relays = grid.getRelayInputs();
        int lattice = plate.relayX().length;
        for (int index = 0; index < 25 && index < relays.size(); index++) {
            IngredientBridge bridge = relays.get(index);
            if (index == RELAY_CENTER || bridge.isEmpty() || lattice == 0) {
                continue;
            }
            int col = index % 5;
            int row = index / 5;
            if (lattice == 3) {
                boolean inner = (col >= 1 && col <= 3) && (row >= 1 && row <= 3);
                if (!inner) {
                    continue;
                }
                col -= 1;
                row -= 1;
            }
            builder.addSlot(RecipeIngredientRole.INPUT,
                            plate.offsetX() + plate.relayX()[col],
                            plate.offsetY() + plate.relayY()[row])
                    .addItemStacks(stacksOf(bridge));
        }

        // 流明条：用星辉自己注册的 JEI 流明配料与其渲染器
        List<LumenStack> lumen = recipe.getRequiredLumen();
        for (int i = 0; i < lumen.size() && i < LUMEN_ROWS; i++) {
            LumenStack stack = lumen.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            builder.addSlot(RecipeIngredientRole.INPUT, LUMEN_X, LUMEN_Y + i * LUMEN_STEP)
                    .setCustomRenderer(LumenIngredientType.INSTANCE, new LumenIngredientRenderer(
                            LumenIngredientRenderer.Display.SHORT_BAR,
                            LumenIngredientRenderer.TooltipMode.SHOW_AMOUNT,
                            stack.getAmount()))
                    .addIngredient(LumenIngredientType.INSTANCE, stack);
        }

        // 产物：用「展示用输出」，星辉挂在输出上的词条说明（增加魔咒词条等）会跟着物品一起显示
        List<ItemStack> outputs = displayOutputs(recipe);
        for (int i = 0; i < outputs.size() && i < OUTPUT_SLOTS; i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X + i * GRID_PITCH, OUTPUT_Y)
                    .addItemStack(outputs.get(i));
        }

        // 额外输入（地面上投掷的物品）
        List<SizedIngredient> additional = recipe.getRequiredAdditionalInputs();
        for (int i = 0; i < additional.size(); i++) {
            SizedIngredient sized = additional.get(i);
            if (sized.ingredient().isEmpty()) {
                continue;
            }
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack stack : sized.ingredient().getItems()) {
                stacks.add(stack.copyWithCount(sized.count()));
            }
            builder.addSlot(RecipeIngredientRole.INPUT,
                            columnX(i), ADDITIONAL_Y + (i / COLUMN_WIDTH) * GRID_PITCH)
                    .addItemStacks(stacks)
                    .setStandardSlotBackground();
        }

        // 圣杯流体
        List<FluidStack> fluids = recipe.getRequiredFluid();
        for (int i = 0; i < fluids.size(); i++) {
            FluidStack fluid = fluids.get(i);
            if (fluid.isEmpty()) {
                continue;
            }
            builder.addSlot(RecipeIngredientRole.INPUT,
                            columnX(i), FLUID_Y + (i / COLUMN_WIDTH) * GRID_PITCH)
                    .addFluidStack(fluid.getFluid(), fluid.getAmount())
                    .setFluidRenderer(fluid.getAmount(), false, 16, 16)
                    .setStandardSlotBackground();
        }
    }

    private static int columnX(int index) {
        return COLUMN_X + (index % COLUMN_WIDTH) * GRID_PITCH;
    }

    /** 宝典与 JEI 都靠这套「展示用输出」把词条说明加进物品 lore（出问题就退回原产物）。 */
    private static List<ItemStack> displayOutputs(AltarRecipe recipe) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return recipe.getOutputs();
        }
        try {
            long tick = ClientProxy.getClientTick();
            return recipe.getOutputsForDisplay(recipe.createInputForDisplay(tick), mc.level.registryAccess(), tick);
        } catch (RuntimeException e) {
            return recipe.getOutputs();
        }
    }

    private static List<ItemStack> stacksOf(IngredientBridge bridge) {
        if (bridge.getIngredient() != null && !bridge.getIngredient().isEmpty()) {
            Ingredient ingredient = bridge.getIngredient();
            return List.of(ingredient.getItems());
        }
        return bridge.getItems().toList();
    }

    @Override
    public void draw(AltarRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        Plate plate = plate(recipe);
        graphics.blit(plate.texture(), plate.offsetX(), plate.offsetY(),
                0, 0, plate.size(), plate.size(), plate.size(), plate.size());
        graphics.blit(STAR, STAR_X, STAR_Y, 0, 0, STAR_W, STAR_H, STAR_W, STAR_H);

        // 祭坛名字：名字和图一起看，就知道要用哪一级祭坛
        Font font = Minecraft.getInstance().font;
        ItemStack altar = recipe.getRequiredType().getAltarItem();
        Component name = altar.getHoverName();
        int total = 16 + 3 + font.width(name);
        int x = PLATE_CENTER_X - total / 2;
        graphics.renderItem(altar, x, NAME_Y);
        graphics.drawString(font, name, x + 19, NAME_Y + 4, TEXT, false);

        // 有额外条件时才出信息图标
        if (hasHints(recipe)) {
            graphics.blit(INFO, INFO_X, INFO_Y, 0, 0, INFO_SIZE, INFO_SIZE, INFO_SIZE, INFO_SIZE);
        }

        if (!recipe.getRequiredAdditionalInputs().isEmpty()) {
            graphics.drawString(font, Component.translatable("jei.as_jei_fix.additional"),
                    COLUMN_X, ADDITIONAL_LABEL_Y, LABEL, false);
        }
        if (!recipe.getRequiredFluid().isEmpty()) {
            graphics.drawString(font, Component.translatable("jei.as_jei_fix.fluid"),
                    COLUMN_X, FLUID_LABEL_Y, LABEL, false);
        }
    }

    private static boolean hasHints(AltarRecipe recipe) {
        return recipe.isOnlyNight() || recipe.getFocusConstellation().isPresent() || !recipe.getRequiredStarlight().isEmpty();
    }

    /** 提示文案全部沿用星辉自己的键，和原版 JEI 提示一字不差。 */
    @Override
    public void getTooltip(ITooltipBuilder tooltip, AltarRecipe recipe, IRecipeSlotsView slots,
                           double mouseX, double mouseY) {
        if (mouseX < INFO_X || mouseX >= INFO_X + INFO_SIZE || mouseY < INFO_Y || mouseY >= INFO_Y + INFO_SIZE) {
            return;
        }
        tooltip.add(Component.translatable("jei.as_jei_fix.duration", recipe.getDuration() / 20));
        if (recipe.isOnlyNight()) {
            tooltip.add(Component.translatable("jei.astralsorcery.info.altar.recipe_night"));
        }
        recipe.getFocusConstellation().ifPresent(constellation -> {
            PlayerProgress progress = ResearchManager.getClientProgress();
            if (progress == null || progress.hasDiscoveredConstellation(constellation)) {
                tooltip.add(Component.translatable("jei.astralsorcery.info.altar.recipe_constellation",
                        constellation.getColoredName()));
            } else {
                tooltip.add(Component.translatable("jei.astralsorcery.info.altar.recipe_constellation.unknown"));
            }
            float chance = recipe.getBaseFocusShatterChance();
            if (chance >= 1.0F) {
                tooltip.add(Component.translatable("jei.astralsorcery.info.altar.focus_shatter.guaranteed"));
            } else if (Math.round(chance * 100.0F) < 1) {
                tooltip.add(Component.translatable("jei.astralsorcery.info.altar.focus_shatter.small"));
            } else {
                tooltip.add(Component.translatable("jei.astralsorcery.info.altar.focus_shatter",
                        Math.round(chance * 100.0F)));
            }
        });
        Set<BaseConstellation> starlight = recipe.getRequiredStarlight();
        if (!starlight.isEmpty()) {
            tooltip.add(Component.translatable("jei.astralsorcery.info.altar.focused_starlight"));
            for (BaseConstellation constellation : starlight) {
                tooltip.add(Component.literal("- ").append(constellation.getColoredName()));
            }
        }
    }
}
