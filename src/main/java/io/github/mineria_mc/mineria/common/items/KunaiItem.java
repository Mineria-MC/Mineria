package io.github.mineria_mc.mineria.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import io.github.mineria_mc.mineria.common.entity.KunaiEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class KunaiItem extends Item {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public KunaiItem() {
        super(new Properties().stacksTo(1).durability(45));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.5, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Nonnull
    @Override
    public ItemStack getDefaultInstance() {
        return Util.make(new ItemStack(this), stack -> stack.hideTooltipPart(ItemStack.TooltipPart.MODIFIERS));
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity living, int duration) {
        if (living instanceof Player player) {
            int useTime = this.getUseDuration(stack) - duration;
            if (useTime >= 10) {
                if (!world.isClientSide) {
                    stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(living.getUsedItemHand()));
                    KunaiEntity kunaiEntity = new KunaiEntity(player, world, stack);
                    kunaiEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                    if (player.getAbilities().instabuild) {
                        kunaiEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    world.addFreshEntity(kunaiEntity);
                    world.playSound(null, kunaiEntity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        player.getInventory().removeItem(stack);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal(" ").append(
                Component.translatable("item.mineria.kunai.attack_damage", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(getAttackDamage(getHitCount(stack)) + 1))).withStyle(ChatFormatting.AQUA)
        );
        tooltip.add(Component.literal(" ").append(
                Component.translatable("item.mineria.kunai.attack_speed", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(4.0 + getAttackSpeed(getHitCount(stack))))).withStyle(ChatFormatting.AQUA)
        );
        tooltip.add(Component.literal(" ").append(
                Component.translatable("item.mineria.kunai.ranged_attack_damage", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(getRangedAttackDamage(stack)))).withStyle(ChatFormatting.AQUA)
        );
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack kunai = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(kunai);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity living) {
        stack.hurtAndBreak(1, living, (holder) -> holder.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity living) {
        if (state.getDestroySpeed(world, pos) != 0.0D) {
            stack.hurtAndBreak(2, living, (holder) -> holder.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        List<Enchantment> applicableEnchantments = Lists.newArrayList(Enchantments.VANISHING_CURSE, Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK);
        return applicableEnchantments.contains(enchantment);
    }

    public static void onHitEntity(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        int hitCount = nbt.contains("HitCount") ? nbt.getInt("HitCount") : 0;
        nbt.putInt("HitCount", ++hitCount);
        stack.setTag(nbt);
        if (hitCount % 2 == 0)
            updateAttributeModifiers(stack, hitCount);
    }

    public static float getRangedAttackDamage(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        int hitCount = nbt != null && nbt.contains("HitCount") ? nbt.getInt("HitCount") : 0;
        return 1.5F + 0.5F * Math.min(20, hitCount / 2);
    }

    private static void updateAttributeModifiers(ItemStack stack, int hitCount) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null) {
            AttributeModifier attackDamage = new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool Modifier", getAttackDamage(hitCount), AttributeModifier.Operation.ADDITION);
            AttributeModifier attackSpeed = new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool Modifier", getAttackSpeed(hitCount), AttributeModifier.Operation.ADDITION);

            ListTag attributeModifiers;
            if (!nbt.contains("AttributeModifiers", 9)) {
                attributeModifiers = new ListTag();
                attributeModifiers.add(Util.make(attackDamage.save(), compoundNBT -> compoundNBT.putString("AttributeName", ForgeRegistries.ATTRIBUTES.getKey(Attributes.ATTACK_DAMAGE).toString())));
                attributeModifiers.add(Util.make(attackSpeed.save(), compoundNBT -> compoundNBT.putString("AttributeName", ForgeRegistries.ATTRIBUTES.getKey(Attributes.ATTACK_SPEED).toString())));
            } else {
                attributeModifiers = nbt.getList("AttributeModifiers", 10);
                attributeModifiers.replaceAll(inbt -> {
                    if (inbt instanceof CompoundTag) {
                        CompoundTag compound = (CompoundTag) inbt;
                        if (compound.contains("AttributeName")) {
                            String attributeName = compound.getString("AttributeName");
                            if (attributeName.equals(ForgeRegistries.ATTRIBUTES.getKey(Attributes.ATTACK_DAMAGE).toString())) {
                                return Util.make(attackDamage.save(), compoundNBT -> compoundNBT.putString("AttributeName", attributeName));
                            } else if (attributeName.equals(ForgeRegistries.ATTRIBUTES.getKey(Attributes.ATTACK_SPEED).toString())) {
                                return Util.make(attackSpeed.save(), compoundNBT -> compoundNBT.putString("AttributeName", attributeName));
                            }
                        }
                    }
                    return inbt;
                });
            }
            nbt.put("AttributeModifiers", attributeModifiers);
        }
        stack.setTag(nbt);
    }

    private static double getAttackDamage(int hitCount) {
        return Math.min(8.0, 0.5 * (hitCount / 2.0));
    }

    private static double getAttackSpeed(int hitCount) {
        return -Math.max(1.0, 3.5 - 0.3 * (hitCount / 2.0));
    }

    private static int getHitCount(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.contains("HitCount") ? nbt.getInt("HitCount") : 0;
    }
}
