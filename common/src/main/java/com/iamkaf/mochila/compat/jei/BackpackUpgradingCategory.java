package com.iamkaf.mochila.compat.jei;
//? if !=1.21.6 {
import com.iamkaf.mochila.registry.Items;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;

public class BackpackUpgradingCategory extends MochilaCategory<BackpackJeiRecipes.UpgradeDisplay> {
    public BackpackUpgradingCategory(IGuiHelper guiHelper) {
        super(guiHelper, "backpack_upgrading", BackpackJeiRecipes.UpgradeDisplay.class,
                Items.IRON_BACKPACK.get(), 142, 58, 70, 20);
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            BackpackJeiRecipes.UpgradeDisplay recipe,
            IFocusGroup focuses
    ) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                inputSlot(
                        builder,
                        column * 18,
                        row * 18,
                        row == 1 && column == 1 ? recipe.backpackInput() : recipe.materialInput()
                );
            }
        }
        outputSlot(builder, 106, 20, recipe.output());
    }
}
//?}
