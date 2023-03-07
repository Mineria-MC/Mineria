package io.github.mineria_mc.mineria.common.init;

import com.google.common.collect.ImmutableSet;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaProfessions {
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, Mineria.MODID);

    public static final RegistryObject<VillagerProfession> APOTHECARY = PROFESSIONS.register("apothecary", () -> new VillagerProfession(Mineria.MODID + ":" +  "apothecary", holder -> holder.is(MineriaPOITypes.APOTHECARY.getId()), holder -> holder.is(MineriaPOITypes.APOTHECARY.getId()), ImmutableSet.of(), ImmutableSet.of(), null));
}
