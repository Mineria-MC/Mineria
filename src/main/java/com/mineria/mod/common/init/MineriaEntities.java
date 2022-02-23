package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.*;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Mineria.MODID);

	public static final RegistryObject<EntityType<GoldenSilverfishEntity>> GOLDEN_SILVERFISH = register("golden_silverfish", EntityType.Builder.of(GoldenSilverfishEntity::new, MobCategory.MONSTER).sized(0.4F, 0.3F).clientTrackingRange(8));
	public static final RegistryObject<EntityType<WizardEntity>> WIZARD = register("wizard", EntityType.Builder.of(WizardEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
	public static final RegistryObject<EntityType<KunaiEntity>> KUNAI = register("kunai", EntityType.Builder.<KunaiEntity>of(KunaiEntity::new, MobCategory.MISC).sized(0.2F, 0.2F).clientTrackingRange(4).updateInterval(20));
	public static final RegistryObject<EntityType<MineriaPotionEntity>> MINERIA_POTION = register("mineria_potion", EntityType.Builder.<MineriaPotionEntity>of(MineriaPotionEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
	public static final RegistryObject<EntityType<MineriaAreaEffectCloudEntity>> MINERIA_AREA_EFFECT_CLOUD = register("mineria_area_effect_cloud", EntityType.Builder.<MineriaAreaEffectCloudEntity>of(MineriaAreaEffectCloudEntity::new, MobCategory.MISC).fireImmune().sized(6.0F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE));
	public static final RegistryObject<EntityType<DruidEntity>> DRUID = register("druid", EntityType.Builder.of(DruidEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
	public static final RegistryObject<EntityType<OvateEntity>> OVATE = register("ovate", EntityType.Builder.of(OvateEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
	public static final RegistryObject<EntityType<BardEntity>> BARD = register("bard", EntityType.Builder.of(BardEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
	public static final RegistryObject<EntityType<ElementalOrbEntity>> ELEMENTAL_ORB = register("elemental_orb", EntityType.Builder.<ElementalOrbEntity>of(ElementalOrbEntity::new, MobCategory.MISC).fireImmune().sized(0.75F, 0.75F).clientTrackingRange(8));
	public static final RegistryObject<EntityType<FireGolemEntity>> FIRE_GOLEM = register("fire_golem", EntityType.Builder.of(FireGolemEntity::new, MobCategory.MONSTER).sized(1.4F, 2.7F).fireImmune().clientTrackingRange(10));
	public static final RegistryObject<EntityType<DirtGolemEntity>> DIRT_GOLEM = register("dirt_golem", EntityType.Builder.of(DirtGolemEntity::new, MobCategory.MONSTER).sized(1.4F, 2.7F).clientTrackingRange(10));
	public static final RegistryObject<EntityType<AirSpiritEntity>> AIR_SPIRIT = register("air_spirit", EntityType.Builder.of(AirSpiritEntity::new, MobCategory.MONSTER).sized(0.8F, 2F).clientTrackingRange(8));
	public static final RegistryObject<EntityType<WaterSpiritEntity>> WATER_SPIRIT = register("water_spirit", EntityType.Builder.of(WaterSpiritEntity::new, MobCategory.MONSTER).sized(0.8F, 1.8F).fireImmune().clientTrackingRange(10));
	public static final RegistryObject<EntityType<BlowgunRefillEntity>> DART = register("dart", EntityType.Builder.<BlowgunRefillEntity>of(BlowgunRefillEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
	public static final RegistryObject<EntityType<JarEntity>> JAR = register("jar", EntityType.Builder.<JarEntity>of(JarEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
	public static final RegistryObject<EntityType<DruidicWolfEntity>> DRUIDIC_WOLF = register("druidic_wolf", EntityType.Builder.of(DruidicWolfEntity::new, MobCategory.MONSTER).sized(0.6F, 0.85F).clientTrackingRange(10));
	public static final RegistryObject<EntityType<BrownBearEntity>> BROWN_BEAR = register("brown_bear", EntityType.Builder.of(BrownBearEntity::new, MobCategory.MONSTER).sized(1.4F, 1.4F).clientTrackingRange(10));
	public static final RegistryObject<EntityType<GreatDruidOfGaulsEntity>> GREAT_DRUID_OF_GAULS = register("great_druid_of_gauls", EntityType.Builder.of(GreatDruidOfGaulsEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).noSummon());
	public static final RegistryObject<EntityType<MineriaLightningBoltEntity>> MINERIA_LIGHTNING_BOLT = register("mineria_lighning_bolt", EntityType.Builder.<MineriaLightningBoltEntity>of(MineriaLightningBoltEntity::new, MobCategory.MISC).noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE));
	public static final RegistryObject<EntityType<BuddhistEntity>> BUDDHIST = register("buddhist", EntityType.Builder.of(BuddhistEntity::new, MobCategory.CREATURE).sized(0.6F, 1.95F).clientTrackingRange(10));
	public static final RegistryObject<EntityType<AsiaticHerbalistEntity>> ASIATIC_HERBALIST = register("asiatic_herbalist", EntityType.Builder.of(AsiaticHerbalistEntity::new, MobCategory.CREATURE).sized(0.6F, 1.95F).clientTrackingRange(10));
	public static final RegistryObject<EntityType<TemporaryItemFrameEntity>> TEMPORARY_ITEM_FRAME = register("temporary_item_frame", EntityType.Builder.<TemporaryItemFrameEntity>of(TemporaryItemFrameEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder)
	{
		return ENTITY_TYPES.register(name, () -> builder.build(Mineria.MODID.concat(":").concat(name)));
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event)
	{
		event.put(GOLDEN_SILVERFISH.get(), (Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 11.0D).add(Attributes.MOVEMENT_SPEED, 0.30D).add(Attributes.ATTACK_DAMAGE, 1.2D)).build());
		event.put(WIZARD.get(), Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 26.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).build());
		event.put(DRUID.get(), AbstractDruidEntity.createAttributes().build());
		event.put(OVATE.get(), AbstractDruidEntity.createAttributes().build());
		event.put(BARD.get(), AbstractDruidEntity.createAttributes().build());
		event.put(FIRE_GOLEM.get(), FireGolemEntity.createAttributes().build());
		event.put(DIRT_GOLEM.get(), DirtGolemEntity.createAttributes().build());
		event.put(AIR_SPIRIT.get(), AirSpiritEntity.createAttributes().build());
		event.put(WATER_SPIRIT.get(), WaterSpiritEntity.createAttributes().build());
		event.put(DRUIDIC_WOLF.get(), DruidicWolfEntity.createAttributes().build());
		event.put(BROWN_BEAR.get(), BrownBearEntity.createAttributes().build());
		event.put(GREAT_DRUID_OF_GAULS.get(), GreatDruidOfGaulsEntity.createAttributes().build());
		event.put(BUDDHIST.get(), Mob.createMobAttributes().build());
		event.put(ASIATIC_HERBALIST.get(), Mob.createMobAttributes().build());
	}

	public static void registerSpawnPlacements()
	{
		SpawnPlacements.register(WIZARD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(BROWN_BEAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(DRUIDIC_WOLF.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(DRUID.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(OVATE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(BARD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(FIRE_GOLEM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(DIRT_GOLEM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(AIR_SPIRIT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(WATER_SPIRIT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(BUDDHIST.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(ASIATIC_HERBALIST.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
	}

	public static final class Tags
	{
		public static final Tag.Named<EntityType<?>> DRUIDS = EntityTypeTags.bind("mineria:druids");
	}
}
