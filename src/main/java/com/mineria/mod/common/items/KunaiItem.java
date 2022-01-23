package com.mineria.mod.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.KunaiEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class KunaiItem extends Item
{
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public KunaiItem()
    {
        super(new Properties().stacksTo(1).durability(45).tab(Mineria.MINERIA_GROUP));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.5, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public ItemStack getDefaultInstance()
    {
        return Util.make(new ItemStack(this), stack -> stack.hideTooltipPart(ItemStack.TooltipDisplayFlags.MODIFIERS));
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks)
    {
        if(this.allowdedIn(group))
            stacks.add(getDefaultInstance());
    }

    @Override
    public boolean canAttackBlock(BlockState state, World world, BlockPos pos, PlayerEntity player)
    {
        return !player.isCreative();
    }

    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity living, int duration)
    {
        if (living instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) living;
            int useTime = this.getUseDuration(stack) - duration;
            if (useTime >= 10)
            {
                if (!world.isClientSide)
                {
                    stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(living.getUsedItemHand()));
                    KunaiEntity kunaiEntity = new KunaiEntity(player, world, stack);
                    kunaiEntity.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 2.5F, 1.0F);
                    if (player.abilities.instabuild)
                    {
                        kunaiEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    }

                    world.addFreshEntity(kunaiEntity);
                    world.playSound(null, kunaiEntity, SoundEvents.TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    if (!player.abilities.instabuild)
                    {
                        player.inventory.removeItem(stack);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        tooltip.add(new StringTextComponent(" ").append(
                new TranslationTextComponent("item.mineria.kunai.attack_damage", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(getAttackDamage(getHitCount(stack)) + 1))).withStyle(TextFormatting.AQUA)
        );
        tooltip.add(new StringTextComponent(" ").append(
                new TranslationTextComponent("item.mineria.kunai.attack_speed", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(4.0 + getAttackSpeed(getHitCount(stack))))).withStyle(TextFormatting.AQUA)
        );
        tooltip.add(new StringTextComponent(" ").append(
                new TranslationTextComponent("item.mineria.kunai.ranged_attack_damage", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(getRangedAttackDamage(stack)))).withStyle(TextFormatting.AQUA)
        );
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack kunai = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return ActionResult.consume(kunai);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity living)
    {
        stack.hurtAndBreak(1, living, (holder) -> holder.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity living)
    {
        if (state.getDestroySpeed(world, pos) != 0.0D)
        {
            stack.hurtAndBreak(2, living, (holder) -> holder.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        }
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
    {
        return slot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        List<Enchantment> applicableEnchantments = Lists.newArrayList(Enchantments.VANISHING_CURSE, Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK);
        return applicableEnchantments.contains(enchantment);
    }

    public static void onHitEntity(ItemStack stack)
    {
        CompoundNBT nbt = stack.getOrCreateTag();
        int hitCount = nbt.contains("HitCount") ? nbt.getInt("HitCount") : 0;
        nbt.putInt("HitCount", ++hitCount);
        stack.setTag(nbt);
        if(hitCount % 2 == 0)
            updateAttributeModifiers(stack, hitCount);
    }

    public static float getRangedAttackDamage(ItemStack stack)
    {
        CompoundNBT nbt = stack.getTag();
        int hitCount = nbt != null && nbt.contains("HitCount") ? nbt.getInt("HitCount") : 0;
        return 1.5F + 0.5F * Math.min(20, hitCount / 2);
    }

    private static void updateAttributeModifiers(ItemStack stack, int hitCount)
    {
        CompoundNBT nbt = stack.getTag();
        if(nbt != null)
        {
            AttributeModifier attackDamage = new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool Modifier", getAttackDamage(hitCount), AttributeModifier.Operation.ADDITION);
            AttributeModifier attackSpeed = new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool Modifier", getAttackSpeed(hitCount), AttributeModifier.Operation.ADDITION);

            ListNBT attributeModifiers;
            if(!nbt.contains("AttributeModifiers", 9))
            {
                attributeModifiers = new ListNBT();
                attributeModifiers.add(Util.make(attackDamage.save(), compoundNBT -> compoundNBT.putString("AttributeName", ForgeRegistries.ATTRIBUTES.getKey(Attributes.ATTACK_DAMAGE).toString())));
                attributeModifiers.add(Util.make(attackSpeed.save(), compoundNBT -> compoundNBT.putString("AttributeName", ForgeRegistries.ATTRIBUTES.getKey(Attributes.ATTACK_SPEED).toString())));
            }
            else
            {
                attributeModifiers = nbt.getList("AttributeModifiers", 10);
                attributeModifiers.replaceAll(inbt -> {
                    if(inbt instanceof CompoundNBT)
                    {
                        CompoundNBT compound = (CompoundNBT) inbt;
                        if(compound.contains("AttributeName"))
                        {
                            String attributeName = compound.getString("AttributeName");
                            if(attributeName.equals(ForgeRegistries.ATTRIBUTES.getKey(Attributes.ATTACK_DAMAGE).toString()))
                            {
                                return Util.make(attackDamage.save(), compoundNBT -> compoundNBT.putString("AttributeName", attributeName));
                            }
                            else if(attributeName.equals(ForgeRegistries.ATTRIBUTES.getKey(Attributes.ATTACK_SPEED).toString()))
                            {
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

    private static double getAttackDamage(int hitCount)
    {
        return Math.min(8.0, 0.5 * (hitCount / 2.0));
    }

    private static double getAttackSpeed(int hitCount)
    {
        return -Math.max(1.0, 3.5 - 0.3 * (hitCount / 2.0));
    }

    private static int getHitCount(ItemStack stack)
    {
        CompoundNBT nbt = stack.getTag();
        return nbt != null && nbt.contains("HitCount") ? nbt.getInt("HitCount") : 0;
    }
}
