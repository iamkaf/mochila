package com.iamkaf.mochila.compat.jei;
//? if !=1.21.6 {
import com.iamkaf.mochila.Constants;
import com.iamkaf.mochila.compat.Texts;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
//? if >=1.21.4 {
import mezz.jei.api.recipe.types.IRecipeType;
//?} else {
/*import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
*///?}
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else if >=1.20 {
/*import net.minecraft.client.gui.GuiGraphics;
*///?} else {
/*import com.mojang.blaze3d.vertex.PoseStack;
*///?}
//? if <1.19.4
/*import net.minecraft.resources.Identifier;*/
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

/**
 * A recipe category for one of Mochila's crafting recipes. JEI 20 (Minecraft 1.21.4) replaced its slot and recipe type
 * API, and Minecraft 1.18.2's JEI identifies a category by a uid and recipe class.
 */
abstract class MochilaCategory<T> implements IRecipeCategory<T> {
    private final Component title;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final int width;
    private final int height;
    private final int arrowX;
    private final int arrowY;
    //? if >=1.21.4 {
    private final IRecipeType<T> type;
    //?} else {
    /*private final RecipeType<T> type;
    private final IDrawable slot;
    private final IDrawable background;
    *///?}

    MochilaCategory(IGuiHelper guiHelper, String id, Class<T> displayClass, Item iconItem, int width, int height,
            int arrowX, int arrowY) {
        this.title = Texts.translatable("jei.mochila.category." + id);
        this.width = width;
        this.height = height;
        this.arrowX = arrowX;
        this.arrowY = arrowY;
        //? if >=1.21.4 {
        this.type = IRecipeType.create(Constants.resource(id), displayClass);
        this.icon = guiHelper.createDrawableItemLike(iconItem);
        this.arrow = guiHelper.getRecipeArrow();
        //?} else {
        /*this.type = RecipeType.create(Constants.MOD_ID, id, displayClass);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(iconItem));
        // JEI before 20 has no recipe arrow helper. This is the arrow it draws in its own vanilla categories.
        this.arrow = guiHelper.createDrawable(Constants.resource("jei", "textures/jei/gui/gui_vanilla.png"), 82, 128, 24, 17);
        this.slot = guiHelper.getSlotDrawable();
        this.background = guiHelper.createBlankDrawable(width, height);
        *///?}
    }

    public void addRecipes(IRecipeRegistration registration, List<T> recipes) {
        registration.addRecipes(type, recipes);
    }

    public void addCraftingTable(IRecipeCatalystRegistration registration) {
        //? if >=1.21.4 {
        registration.addCraftingStation(type, Items.CRAFTING_TABLE);
        //?} else {
        /*registration.addRecipeCatalyst(new ItemStack(Items.CRAFTING_TABLE), type);
        *///?}
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    // JEI made these two overridable after Minecraft 1.18.2.
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    //? if >=1.21.4 {
    @Override
    public IRecipeType<T> getRecipeType() {
        return type;
    }
    //?} else {
    /*@Override
    public RecipeType<T> getRecipeType() {
        return type;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }
    *///?}

    //? if <1.19.4 {
    /*@Override
    public Identifier getUid() {
        return type.getUid();
    }

    @Override
    public Class<? extends T> getRecipeClass() {
        return type.getRecipeClass();
    }
    *///?}

    //? if >=1.21.4 {
    protected final void inputSlot(IRecipeLayoutBuilder builder, int x, int y, ItemStack stack) {
        builder.addInputSlot(x, y).setStandardSlotBackground().add(stack);
    }

    protected final void inputSlot(IRecipeLayoutBuilder builder, int x, int y, List<ItemStack> stacks) {
        builder.addInputSlot(x, y).setStandardSlotBackground().addItemStacks(stacks);
    }

    protected final void outputSlot(IRecipeLayoutBuilder builder, int x, int y, ItemStack stack) {
        builder.addOutputSlot(x, y).setOutputSlotBackground().add(stack);
    }
    //?} else {
    /*protected final void inputSlot(IRecipeLayoutBuilder builder, int x, int y, ItemStack stack) {
        builder.addSlot(RecipeIngredientRole.INPUT, x, y).setBackground(slot, -1, -1).addItemStack(stack);
    }

    protected final void inputSlot(IRecipeLayoutBuilder builder, int x, int y, List<ItemStack> stacks) {
        builder.addSlot(RecipeIngredientRole.INPUT, x, y).setBackground(slot, -1, -1).addItemStacks(stacks);
    }

    protected final void outputSlot(IRecipeLayoutBuilder builder, int x, int y, ItemStack stack) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).setBackground(slot, -1, -1).addItemStack(stack);
    }
    *///?}

    //? if >=26.1 {
    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX,
            double mouseY) {
        arrow.draw(guiGraphics, arrowX, arrowY);
    }
    //?} else if >=1.20 {
    /*@Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
            double mouseY) {
        arrow.draw(guiGraphics, arrowX, arrowY);
    }
    *///?} else {
    /*@Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, PoseStack guiGraphics, double mouseX,
            double mouseY) {
        arrow.draw(guiGraphics, arrowX, arrowY);
    }
    *///?}
}
//?}
