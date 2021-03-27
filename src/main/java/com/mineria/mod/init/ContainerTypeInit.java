package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.blocks.infuser.InfuserContainer;
import com.mineria.mod.blocks.titane_extractor.TitaneExtractorContainer;
import com.mineria.mod.blocks.xp_block.XpBlockContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypeInit
{
    //Deferred Register
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.MODID);

    public static final RegistryObject<ContainerType<TitaneExtractorContainer>> TITANE_EXTRACTOR = CONTAINER_TYPES.register("titane_extractor", () -> IForgeContainerType.create(TitaneExtractorContainer::create));
    public static final RegistryObject<ContainerType<InfuserContainer>> INFUSER = CONTAINER_TYPES.register("infuser", () -> IForgeContainerType.create(InfuserContainer::create));
    public static final RegistryObject<ContainerType<XpBlockContainer>> XP_BLOCK = CONTAINER_TYPES.register("xp_block", () -> IForgeContainerType.create(XpBlockContainer::create));
}
