package com.iamkaf.mochila.compat.emi;

import com.iamkaf.mochila.item.BackpackItem;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.minecraft.world.item.DyeColor;

@EmiEntrypoint
public class EmiCompat implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        for (BackpackItem.Tier tier : BackpackItem.Tier.values()) {
            for (DyeColor color : DyeColor.values()) {
                registry.addRecipe(new BackpackColoringEmiRecipe(tier, color));

                if (tier.equals(BackpackItem.Tier.NETHERITE)) continue;
                registry.addRecipe(new BackpackUpgradingEmiRecipe(tier, color));
            }
        }
    }
}
