package com.iamkaf.mochila.compat.jei;
//? if !=1.21.6 {
import com.iamkaf.mochila.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class MochilaJeiPlugin implements IModPlugin {
    private BackpackColoringCategory coloring;
    private BackpackUpgradingCategory upgrading;

    @Override
    public Identifier getPluginUid() {
        return Constants.resource("jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        coloring = new BackpackColoringCategory(guiHelper);
        upgrading = new BackpackUpgradingCategory(guiHelper);
        registration.addRecipeCategories(coloring, upgrading);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        coloring.addRecipes(registration, BackpackJeiRecipes.coloringDisplays());
        upgrading.addRecipes(registration, BackpackJeiRecipes.upgradeDisplays());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        coloring.addCraftingTable(registration);
        upgrading.addCraftingTable(registration);
    }
}
//?}
