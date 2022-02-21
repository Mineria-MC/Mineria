package com.mineria.mod;

import net.minecraft.util.registry.Bootstrap;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import org.junit.jupiter.api.BeforeAll;

import static org.mockito.Mockito.mock;

public class McTest
{
    @BeforeAll
    static void setupRegistries()
    {
        Bootstrap.bootStrap();
        ModContainer modContainer = mock(ModContainer.class);
        ModLoadingContext.get().setActiveContainer(modContainer, null);
    }
}
