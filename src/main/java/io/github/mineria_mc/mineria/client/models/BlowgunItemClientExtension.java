package io.github.mineria_mc.mineria.client.models;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class BlowgunItemClientExtension implements IClientItemExtensions {
    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return entityLiving.isUsingItem() ? MineriaArmPoses.BLOWGUN : null;
    }
}
