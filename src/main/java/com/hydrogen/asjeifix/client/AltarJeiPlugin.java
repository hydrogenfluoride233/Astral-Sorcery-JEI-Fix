package com.hydrogen.asjeifix.client;

import com.hydrogen.asjeifix.AsJeiFix;
import hellfirepvp.astralsorcery.common.integration.jei.category.AltarRecipeCategory;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.recipe.altar.AltarRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

/**
 * 注册更紧凑的祭坛页，并把星辉魔法自带的那页藏起来。
 */
@JeiPlugin
public class AltarJeiPlugin implements IModPlugin {
    public static final RecipeType<AltarRecipe> ALTAR_TYPE =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(AsJeiFix.MODID, "altar"), AltarRecipe.class);

    private static final String[] CATALYSTS = {
            "altar_illumination", "altar_resonance", "altar_luminance", "altar_radiance"
    };

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(AsJeiFix.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AltarFixCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        List<AltarRecipe> recipes = mc.level.getRecipeManager()
                .getAllRecipesFor(RecipeTypesAS.ALTAR_CRAFTING_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(ALTAR_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (String id : CATALYSTS) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("astralsorcery", id));
            if (item != null) {
                registration.addRecipeCatalyst(new ItemStack(item), ALTAR_TYPE);
            }
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        runtime.getRecipeManager().hideRecipeCategory(AltarRecipeCategory.RECIPE_TYPE);
    }
}
