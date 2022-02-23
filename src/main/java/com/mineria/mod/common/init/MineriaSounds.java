package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// TODO
public class MineriaSounds
{
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Mineria.MODID);

    public static final RegistryObject<SoundEvent> BOWEL_SOUNDS = register("entity.bowel_sounds");
    public static final RegistryObject<SoundEvent> MUSIC_PIPPIN_THE_HUNCHBACK = register("music_disc.pippin_the_hunchback");
    public static final RegistryObject<SoundEvent> DRUID_AMBIENT = register("entity.druid.ambient");
    public static final RegistryObject<SoundEvent> DRUID_HURT = register("entity.druid.hurt");
    public static final RegistryObject<SoundEvent> DRUID_DEATH = register("entity.druid.death");
    public static final RegistryObject<SoundEvent> DRUID_YES = register("entity.druid.yes");
    public static final RegistryObject<SoundEvent> DRUID_NO = register("entity.druid.no");
    public static final RegistryObject<SoundEvent> DIRT_GOLEM_HURT = register("entity.dirt_golem.hurt");
    public static final RegistryObject<SoundEvent> DIRT_GOLEM_DEATH = register("entity.dirt_golem.death");
    public static final RegistryObject<SoundEvent> FIRE_GOLEM_HURT = register("entity.fire_golem.hurt");
    public static final RegistryObject<SoundEvent> FIRE_GOLEM_DEATH = register("entity.fire_golem.death");
    public static final RegistryObject<SoundEvent> WIZARD_AMBIENT = register("entity.wizard.ambient"); //
    public static final RegistryObject<SoundEvent> WIZARD_HURT = register("entity.wizard.hurt"); //
    public static final RegistryObject<SoundEvent> WIZARD_DEATH = register("entity.wizard.death"); //
    public static final RegistryObject<SoundEvent> AIR_SPIRIT_AMBIENT = register("entity.air_spirit.ambient");
    public static final RegistryObject<SoundEvent> AIR_SPIRIT_HURT = register("entity.air_spirit.hurt");
    public static final RegistryObject<SoundEvent> AIR_SPIRIT_DEATH = register("entity.air_spirit.death");
    public static final RegistryObject<SoundEvent> WATER_SPIRIT_HURT = register("entity.water_spirit.hurt"); //
    public static final RegistryObject<SoundEvent> WATER_SPIRIT_DEATH = register("entity.water_spirit.death"); //
    public static final RegistryObject<SoundEvent> DRUIDIC_WOLF_HURT = register("entity.druidic_wolf.hurt"); //
    public static final RegistryObject<SoundEvent> DRUIDIC_WOLF_DEATH = register("entity.druidic_wolf.death"); //
    public static final RegistryObject<SoundEvent> APOTHECARIUM_CUSTOM_PAGE_TURN = register("item.apothecarium.custom_page_turn");

    private static RegistryObject<SoundEvent> register(String id)
    {
        return SOUNDS.register(id, () -> new SoundEvent(new ResourceLocation(Mineria.MODID, id)));
    }
}
