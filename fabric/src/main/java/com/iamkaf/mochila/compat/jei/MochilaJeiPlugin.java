package com.iamkaf.mochila.compat.jei;

import com.iamkaf.mochila.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

@JeiPlugin
public class MochilaJeiPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return Constants.resource("jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new BackpackColoringCategory(guiHelper),
                new BackpackUpgradingCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(BackpackColoringCategory.RECIPE_TYPE, BackpackJeiRecipes.coloringDisplays());
        registration.addRecipes(BackpackUpgradingCategory.RECIPE_TYPE, BackpackJeiRecipes.upgradeDisplays());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(BackpackColoringCategory.RECIPE_TYPE, Items.CRAFTING_TABLE);
        registration.addCraftingStation(BackpackUpgradingCategory.RECIPE_TYPE, Items.CRAFTING_TABLE);
    }
}
