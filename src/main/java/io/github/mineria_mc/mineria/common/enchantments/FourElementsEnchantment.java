package io.github.mineria_mc.mineria.common.enchantments;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.capabilities.IElementCapability;
import io.github.mineria_mc.mineria.common.capabilities.MineriaCapabilities;
import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaEffects;
import io.github.mineria_mc.mineria.util.DamageSourceUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FourElementsEnchantment extends Enchantment {
    private final ElementType elementType;

    public FourElementsEnchantment(ElementType elementType) {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.CHEST});
        this.elementType = elementType;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    public ElementType getElementType() {
        return this.elementType;
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
        return super.canApplyAtEnchantingTable(stack) && !hasEnchantment(stack);
    }

    private static boolean hasEnchantment(ItemStack stack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        Optional<Enchantment> enchantment = enchantments.keySet().stream().filter(FourElementsEnchantment.class::isInstance).findFirst();
        return enchantment.isPresent() && enchantments.get(enchantment.get()) > 0;
    }

    @Nonnull
    @Override
    public Component getFullname(int level) {
        return Component.translatable(this.getDescriptionId()).withStyle(style -> style.withColor(TextColor.fromRgb(this.elementType.getColor())));
    }

    public static Optional<FourElementsEnchantment> getFromEntity(LivingEntity entity) {
        ItemStack chestItem = entity.getItemBySlot(EquipmentSlot.CHEST);
        if(chestItem.isEmpty()) {
            return Optional.empty();
        }
        return EnchantmentHelper.getEnchantments(chestItem).keySet().stream().filter(FourElementsEnchantment.class::isInstance).map(FourElementsEnchantment.class::cast).findFirst();
    }

    @SubscribeEvent
    public static void onEnchantmentApplied(LivingEvent.LivingTickEvent event) {
        LivingEntity owner = event.getEntity();
        Level world = owner.getLevel();
        if(world.isClientSide() || !owner.isAlive() || owner.isSpectator()) {
            return;
        }
        Optional<FourElementsEnchantment> opt = getFromEntity(owner);

        if (opt.isPresent()) {
            FourElementsEnchantment enchantment = opt.get();
            world.getEntities(owner, owner.getBoundingBox().inflate(1.25), entity -> entity instanceof LivingEntity && entity.isAlive() && !entity.isSpectator() && !entity.isAlliedTo(owner)).forEach(entity -> {
                if(!(entity instanceof LivingEntity living)) {
                    return;
                }
                if(living.invulnerableTime > 0) {
                    return;
                }
                Optional<IElementCapability> optCap = entity.getCapability(MineriaCapabilities.ELEMENT_EXPOSURE).resolve();
                if(optCap.isEmpty()) {
                    return;
                }
                IElementCapability cap = optCap.get();

                cap.applyElement(enchantment.getElementType());
                switch (enchantment.getElementType()) {
                    case FIRE -> {
                        if (!living.fireImmune()) {
                            living.setSecondsOnFire(10);
                            living.hurt(DamageSourceUtil.elementalOrb(owner), 2);
                        }
                    }
                    case WATER -> {
                        if (cap.applicationCount(ElementType.WATER) >= 5) {
                            cap.removeElement(ElementType.WATER);
                            living.setAirSupply(-650);
                            living.addEffect(new MobEffectInstance(MineriaEffects.DROWNING.get(), 32770));
                        } else {
                            living.setAirSupply(living.getAirSupply() - 60);
                            living.hurt(DamageSource.DROWN, 1);
                        }
                    }
                    case AIR -> {
                        living.hurt(DamageSourceUtil.elementalOrb(owner), 1);
                        living.setDeltaMovement(living.getDeltaMovement().add(0, 1.25, 0));
                    }
                    case GROUND -> {
                        if (!PoisonMobEffectInstance.isEntityAffected(living)) {
                            PoisonMobEffectInstance.applyPoisonEffect(living, 1, 24000, 0, PoisonSource.UNKNOWN);
                        }
                        living.hurt(DamageSourceUtil.elementalOrb(owner), 1);
                    }
                }
            });
        }
    }
}
