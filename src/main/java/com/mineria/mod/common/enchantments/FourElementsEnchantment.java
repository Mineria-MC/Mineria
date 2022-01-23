package com.mineria.mod.common.enchantments;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.entity.ElementalOrbEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FourElementsEnchantment extends Enchantment
{
    private final ElementType elementType;

    public FourElementsEnchantment(ElementType elementType)
    {
        super(Rarity.VERY_RARE, EnchantmentType.ARMOR_CHEST, new EquipmentSlotType[] { EquipmentSlotType.CHEST });
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
    public ITextComponent getFullname(int level)
    {
        return new TranslationTextComponent(this.getDescriptionId()).withStyle(style -> style.withColor(Color.fromRgb(this.elementType.getColor())));
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
        PlayerEntity player = event.player;
        World world = player.getCommandSenderWorld();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(player.inventory.armor.get(2));

        if(!world.isClientSide())
        {
            if(enchantments.keySet().stream().anyMatch(FourElementsEnchantment.class::isInstance))
            {
                FourElementsEnchantment enchantment = (FourElementsEnchantment) enchantments.keySet().stream().filter(FourElementsEnchantment.class::isInstance).findFirst().get();
                player.getCapability(CapabilityRegistry.ATTACHED_ENTITY).ifPresent(cap -> {
                    cap.updateAttachedEntities(world);

                    cap.removeAttachedEntities(entity -> entity instanceof ElementalOrbEntity && !((ElementalOrbEntity) entity).getElementType().equals(enchantment.getElementType())).forEach(Entity::remove);

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
                player.getCapability(CapabilityRegistry.ATTACHED_ENTITY).ifPresent(cap -> cap.removeAttachedEntities(entity -> entity instanceof ElementalOrbEntity).forEach(Entity::remove));
            }
        }
    }
}
