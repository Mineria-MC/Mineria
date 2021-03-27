package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.entity.GoldenSilverfishEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, References.MODID);

	public static final RegistryObject<EntityType<GoldenSilverfishEntity>> GOLDEN_SILVERFISH = ENTITY_TYPES.register("golden_silverfish",
			() -> EntityType.Builder.create(GoldenSilverfishEntity::new, EntityClassification.MONSTER).size(0.4F, 0.3F).trackingRange(8)
					.build(new ResourceLocation(References.MODID, "golden_silverfish").toString()));

	public static void registerEntityAttributes()
	{
		GlobalEntityTypeAttributes.put(GOLDEN_SILVERFISH.get(), (MonsterEntity.func_234295_eP_().createMutableAttribute(Attributes.MAX_HEALTH, 11.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.30D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.2D)).create());
	}
}
