package com.iamkaf.mochila.compat.jei;
//? if !=1.21.6 {
import com.iamkaf.mochila.registry.Items;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;

public class BackpackColoringCategory extends MochilaCategory<BackpackJeiRecipes.ColoringDisplay> {
    public BackpackColoringCategory(IGuiHelper guiHelper) {
        super(guiHelper, "backpack_coloring", BackpackJeiRecipes.ColoringDisplay.class,
                Items.LIGHT_BLUE_LEATHER_BACKPACK.get(), 116, 38, 56, 11);
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            BackpackJeiRecipes.ColoringDisplay recipe,
            IFocusGroup focuses
    ) {
        inputSlot(builder, 18, 11, recipe.backpackInputs());
        inputSlot(builder, 40, 11, recipe.dyeInput());
        outputSlot(builder, 82, 11, recipe.output());
        builder.setShapeless(98, 0);
    }
}
//?}
