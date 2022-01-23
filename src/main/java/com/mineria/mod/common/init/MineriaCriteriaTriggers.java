package com.mineria.mod.common.init;

import com.mineria.mod.common.data.triggers.*;

import static net.minecraft.advancements.CriteriaTriggers.register;

public class MineriaCriteriaTriggers
{
    public static final DamageFromSpikeTrigger DAMAGE_FROM_SPIKE = register(new DamageFromSpikeTrigger());
    public static final MinedBlockTrigger MINED_BLOCK = register(new MinedBlockTrigger());
    public static final FluidBarrelFilledTrigger FLUID_BARREL_FILLED = register(new FluidBarrelFilledTrigger());
    public static final ExtractedItemTrigger EXTRACTED_ITEM = register(new ExtractedItemTrigger());
    public static final ObtainedTradeBonusRewardsTrigger OBTAINED_TRADE_BONUS_REWARDS = register(new ObtainedTradeBonusRewardsTrigger());
    public static final UsedAnvilTrigger USED_ANVIL = register(new UsedAnvilTrigger());
    public static final BrewedItemTrigger BREWED_ITEM = register(new BrewedItemTrigger());
    public static final UsedApothecaryTableTrigger USED_APOTHECARY_TABLE = register(new UsedApothecaryTableTrigger());
    public static final ShotBlowgunTrigger SHOT_BLOWGUN = register(new ShotBlowgunTrigger());
    public static final UsedScalpelTrigger USED_SCALPEL = register(new UsedScalpelTrigger());
    public static final ShapedRecipeUsedTrigger SHAPED_RECIPE_USED = register(new ShapedRecipeUsedTrigger());

    // We resolve all the triggers by initializing the class.
    public static void init() {}
}
