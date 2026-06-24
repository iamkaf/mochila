package com.iamkaf.mochila.compat.jei;

import com.iamkaf.mochila.Constants;
import com.iamkaf.mochila.registry.Items;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
//? if <26.1
/*import net.minecraft.client.gui.GuiGraphics;*/
//? if >=26.1
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public class BackpackUpgradingCategory implements IRecipeCategory<BackpackJeiRecipes.UpgradeDisplay> {
    public static final IRecipeType<BackpackJeiRecipes.UpgradeDisplay> RECIPE_TYPE =
            IRecipeType.create(Constants.resource("backpack_upgrading"), BackpackJeiRecipes.UpgradeDisplay.class);

    private final IDrawable icon;
    private final IDrawable arrow;

    public BackpackUpgradingCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemLike(Items.IRON_BACKPACK.get());
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public IRecipeType<BackpackJeiRecipes.UpgradeDisplay> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.mochila.category.backpack_upgrading");
    }

    @Override
    public int getWidth() {
        return 142;
    }

    @Override
    public int getHeight() {
        return 58;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            BackpackJeiRecipes.UpgradeDisplay recipe,
            IFocusGroup focuses
    ) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                int x = 0 + column * 18;
                int y = 0 + row * 18;
                if (row == 1 && column == 1) {
                    builder.addInputSlot(x, y)
                            .setStandardSlotBackground()
                            .add(recipe.backpackInput());
                } else {
                    builder.addInputSlot(x, y)
                            .setStandardSlotBackground()
                            .add(recipe.materialInput());
                }
            }
        }
        builder.addOutputSlot(106, 20)
                .setOutputSlotBackground()
                .add(recipe.output());
    }

    @Override
    public void draw(
            BackpackJeiRecipes.UpgradeDisplay recipe,
            IRecipeSlotsView recipeSlotsView,
            //? if >=26.1
            GuiGraphicsExtractor guiGraphics,
            //? if <26.1
            /*GuiGraphics guiGraphics,*/
            double mouseX,
            double mouseY
    ) {
        arrow.draw(guiGraphics, 70, 20);
    }
}
