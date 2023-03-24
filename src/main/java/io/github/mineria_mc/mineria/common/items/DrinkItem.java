package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import com.mojang.datafixers.util.Either;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.tags.ITagManager;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DrinkItem extends Item {
    private final DrinkItem.Properties drinkProperties;

    public DrinkItem(Item.Properties properties, DrinkItem.Properties drinkProperties) {
        super(properties);
        this.drinkProperties = drinkProperties;
    }

    public DrinkItem(Item.Properties properties) {
        this(properties, new DrinkItem.Properties());
    }

    @Nonnull
    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.DRINK;
    }

    private static Method ADD_EAT_EFFECT;

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level worldIn, @Nonnull LivingEntity entityLiving) {
        if (entityLiving instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!worldIn.isClientSide && this.drinkProperties.immediateCureEffects) {
            entityLiving.curePotionEffects(stack);
        }
        if (!worldIn.isClientSide) {
            this.drinkProperties.foodEatenCallback.onFoodEaten(stack, worldIn, entityLiving);
        }

        try {
            if (ADD_EAT_EFFECT == null) {
                ADD_EAT_EFFECT = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "m_21063_", ItemStack.class, Level.class, LivingEntity.class);
            }
            ADD_EAT_EFFECT.invoke(entityLiving, stack, worldIn, entityLiving);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Mineria.LOGGER.error("Couldn't apply effect for DrinkItem '{}'", ForgeRegistries.ITEMS.getKey(this));
            e.printStackTrace();
        }

        boolean survivalPlayer = entityLiving instanceof Player && !((Player) entityLiving).getAbilities().instabuild;

        if (survivalPlayer) {
            stack.shrink(1);
        }

        ItemStack containerStack = this.drinkProperties.container.mapLeft(RegistryObject::get).map(ItemStack::new, ItemStack::new);

        if (stack.isEmpty()) {
            return containerStack;
        } else {
            if (survivalPlayer) {
                Player player = (Player) entityLiving;
                if (!player.getInventory().add(containerStack)) {
                    player.drop(containerStack, false);
                }
            }
            return stack;
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level worldIn, @Nonnull Player playerIn, @Nonnull InteractionHand handIn) {
        return ItemUtils.startUsingInstantly(worldIn, playerIn, handIn);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return this.drinkProperties.container.mapLeft(RegistryObject::get).map(ItemStack::new, ItemStack::new);
    }

    public static void lockLaxativeDrinks(LivingEntity living) {
        if (living instanceof Player player) {
            ITagManager<Item> itemTags = ForgeRegistries.ITEMS.tags();
            if(itemTags != null) {
                itemTags.getTag(MineriaItems.Tags.LAXATIVE_DRINKS).stream().forEach(item -> player.getCooldowns().addCooldown(item, 20 * 60 * 10));
            }
        }
    }

    public static void unlockLaxativeDrinks(LivingEntity living) {
        if (living instanceof Player player) {
            ITagManager<Item> itemTags = ForgeRegistries.ITEMS.tags();
            if(itemTags != null) {
                itemTags.getTag(MineriaItems.Tags.LAXATIVE_DRINKS).stream().forEach(player.getCooldowns()::removeCooldown);
            }
        }
    }

    public static class Properties {
        private Either<RegistryObject<Item>, Item> container = Either.left(MineriaItems.CUP);
        private CallbackFoodItem.FoodEatenCallback foodEatenCallback = (stack, world, living) -> {};
        private boolean immediateCureEffects = true;

        public Properties container(RegistryObject<Item> item) {
            this.container = Either.left(item);
            return this;
        }

        public Properties container(Item item) {
            this.container = Either.right(item);
            return this;
        }

        public Properties onFoodEaten(CallbackFoodItem.FoodEatenCallback onFoodEaten) {
            this.foodEatenCallback = onFoodEaten;
            return this;
        }

        public Properties doNotImmediateCureEffects() {
            this.immediateCureEffects = false;
            return this;
        }
    }
}
