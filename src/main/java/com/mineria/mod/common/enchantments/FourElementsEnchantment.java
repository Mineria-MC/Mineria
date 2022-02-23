package com.mineria.mod.common.enchantments;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.entity.ElementalOrbEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FourElementsEnchantment extends Enchantment
{
    private final ElementType elementType;

    public FourElementsEnchantment(ElementType elementType)
    {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[] { EquipmentSlot.CHEST });
        this.elementType = elementType;
    }

    @Override
    public boolean isTreasureOnly()
    {
        return true;
    }

    @Override
    public boolean isTradeable()
    {
        return false;
    }

    public ElementType getElementType()
    {
        return this.elementType;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return super.canApplyAtEnchantingTable(stack) && !hasEnchantment(stack);
    }

    private static boolean hasEnchantment(ItemStack stack)
    {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        Optional<Enchantment> enchantment = enchantments.keySet().stream().filter(FourElementsEnchantment.class::isInstance).findFirst();
        return enchantment.isPresent() && enchantments.get(enchantment.get()) > 0;
    }

    @Override
    public Component getFullname(int level)
    {
        return new TranslatableComponent(this.getDescriptionId()).withStyle(style -> style.withColor(TextColor.fromRgb(this.elementType.getColor())));
    }

    public enum ElementType
    {
        NONE(0, 11184810),
        FIRE(1, 14759181),
        WATER(2, 1784051),
        AIR(3, 7526298),
        GROUND(4, 7752762);

        private static final ElementType[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(ElementType::getId)).toArray(ElementType[]::new);

        final int id;
        final int color;

        ElementType(int id, int color)
        {
            this.id = id;
            this.color = color;
        }

        public int getColor()
        {
            return color;
        }

        public int getId()
        {
            return id;
        }

        public static ElementType byId(int id)
        {
            if (id < 0 || id >= BY_ID.length)
            {
                id = 0;
            }

            return BY_ID[id];
        }
    }

    @SubscribeEvent
    public static void onEnchantmentApplied(TickEvent.PlayerTickEvent event)
    {
        Player player = event.player;
        Level world = player.getCommandSenderWorld();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(player.getInventory().armor.get(2));

        if(!world.isClientSide())
        {
            if(enchantments.keySet().stream().anyMatch(FourElementsEnchantment.class::isInstance))
            {
                FourElementsEnchantment enchantment = (FourElementsEnchantment) enchantments.keySet().stream().filter(FourElementsEnchantment.class::isInstance).findFirst().get();
                player.getCapability(CapabilityRegistry.ATTACHED_ENTITY).ifPresent(cap -> {
                    cap.updateAttachedEntities(world);

                    cap.removeAttachedEntities(entity -> entity instanceof ElementalOrbEntity && !((ElementalOrbEntity) entity).getElementType().equals(enchantment.getElementType())).forEach(entity -> entity.remove(Entity.RemovalReason.KILLED));

                    for(int i = cap.getAttachedEntities().size(); i < 3; i++)
                    {
                        ElementalOrbEntity orb = new ElementalOrbEntity(world, player, enchantment.getElementType(), i * 8);
                        orb.setPos(player.getX(), player.getY(), player.getZ());
                        world.addFreshEntity(orb);
                        cap.attachEntity(orb);
                    }
                });
            } else
            {
                player.getCapability(CapabilityRegistry.ATTACHED_ENTITY).ifPresent(cap -> cap.removeAttachedEntities(entity -> entity instanceof ElementalOrbEntity).forEach(entity -> entity.remove(Entity.RemovalReason.KILLED)));
            }
        }
    }
}
