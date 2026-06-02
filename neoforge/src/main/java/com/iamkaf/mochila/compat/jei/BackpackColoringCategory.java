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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public class BackpackColoringCategory implements IRecipeCategory<BackpackJeiRecipes.ColoringDisplay> {
    public static final IRecipeType<BackpackJeiRecipes.ColoringDisplay> RECIPE_TYPE =
            IRecipeType.create(Constants.resource("backpack_coloring"), BackpackJeiRecipes.ColoringDisplay.class);

    private final IDrawable icon;
    private final IDrawable arrow;

    public BackpackColoringCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemLike(Items.LIGHT_BLUE_LEATHER_BACKPACK.get());
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public IRecipeType<BackpackJeiRecipes.ColoringDisplay> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.mochila.category.backpack_coloring");
    }

    @Override
    public int getWidth() {
        return 116;
    }

    @Override
    public int getHeight() {
        return 38;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            BackpackJeiRecipes.ColoringDisplay recipe,
            IFocusGroup focuses
    ) {
        builder.addInputSlot(18, 11)
                .setStandardSlotBackground()
                .addItemStacks(recipe.backpackInputs());
        builder.addInputSlot(40, 11)
                .setStandardSlotBackground()
                .add(recipe.dyeInput());
        builder.addOutputSlot(82, 11)
                .setOutputSlotBackground()
                .add(recipe.output());
        builder.setShapeless(98, 0);
    }

    @Override
    public void draw(
            BackpackJeiRecipes.ColoringDisplay recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphicsExtractor guiGraphics,
            double mouseX,
            double mouseY
    ) {
        arrow.draw(guiGraphics, 56, 11);
    }
}
