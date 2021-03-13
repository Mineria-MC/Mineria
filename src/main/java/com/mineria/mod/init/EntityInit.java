package com.mineria.mod.init;

import com.mineria.mod.References;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, References.MODID);



	/*
	public static void registerEntities()
	{
		registerEntity("golden_fish", EntityGoldenFish.class, References.ENTITY_GOLDEN_FISH, 10, 12888340, 12852517);
		addSpawns();
	}
	
	private static void addSpawns()
	{
		
	}
	
	private static Biome[] getBiomes(final BiomeDictionary.Type type)
	{
		return BiomeDictionary.getBiomes(type).toArray(new Biome[0]);
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2)
	{
		EntityRegistry.registerModEntity(new ResourceLocation (References.MODID + ":" + name), entity, name, id, Mineria.instance, range, 1, true, color1, color2);
	}
	
	private static void copySpawns(final Class<? extends EntityLiving> classToAdd, final EnumCreatureType creatureTypeToAdd, final Class<? extends EntityLiving> classToCopy, final EnumCreatureType creatureTypeToCopy)
	{
		for (final Biome biome : ForgeRegistries.BIOMES)
		{
			biome.getSpawnableList(creatureTypeToCopy).stream()
			.filter(entry -> entry.entityClass == classToCopy)
			.findFirst()
			.ifPresent(spawnListEntry ->
			biome.getSpawnableList(creatureTypeToAdd).add(new Biome.SpawnListEntry(classToAdd, spawnListEntry.itemWeight, spawnListEntry.minGroupCount, spawnListEntry.maxGroupCount))
			);
		}
	}*/
}
