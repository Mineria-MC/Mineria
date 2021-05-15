package com.mineria.mod.util;

import com.mineria.mod.Mineria;
import com.mineria.mod.References;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class MineriaConfiguration
{
    private Configuration configuration;

    public int guiTitaneExtractor = 1;
    public int guiExtractor = 2;
    public int guiXpBlock = 3;
    public int guiInfuser = 4;
    public int guiCopperBarrel = 5;
    public int guiGoldenBarrel = 6;
    public int guiDiamondBarrel = 7;

    public int entityGoldenSilverfish = 354;

    public void setupConfig()
    {
        configuration = new Configuration(new File(Mineria.configDirectory, References.MODID.concat(".cfg")));
        configuration.load();

        Mineria.LOGGER.info("Setting up Mineria Config");

        setupIDS();

        configuration.save();
    }

    private void setupIDS()
    {
        int minValue;
        int maxValue;
        String guiIDCategory = "GUI IDs";
        minValue = 1;
        maxValue = 512;
        configuration.addCustomCategoryComment(guiIDCategory, "IDs of the GUIs in Mineria.");
        guiTitaneExtractor = configuration.getInt("guiTitaneExtractor", guiIDCategory, 1, minValue, maxValue, "The GUI ID of the Titane Extractor.");
        guiExtractor = configuration.getInt("guiExtractor", guiIDCategory, 2, minValue, maxValue, "The GUI ID of the Extractor.");
        guiXpBlock = configuration.getInt("guiXpBlock", guiIDCategory, 3, minValue, maxValue, "The GUI ID of the XP Block.");
        guiInfuser = configuration.getInt("guiInfuser", guiIDCategory, 4, minValue, maxValue, "The GUI ID of the Infuser.");
        guiCopperBarrel = configuration.getInt("guiCopperBarrel", guiIDCategory, 5, minValue, maxValue, "The GUI ID of the Copper Water Barrel.");
        guiGoldenBarrel = configuration.getInt("guiGoldenBarrel", guiIDCategory, 6, minValue, maxValue, "The GUI ID of the Golden Water Barrel.");
        //guiDiamondBarrel = configuration.getInt("guiDiamondBarrel", guiIDCategory, 7, minValue, maxValue, "The GUI ID for the Diamond Water Barrel");
        String entityIDCategory = "Entity IDs";
        minValue = 120;
        maxValue = 2048;
        configuration.addCustomCategoryComment(entityIDCategory, "IDs of the Entities in Mineria.");
        entityGoldenSilverfish = configuration.getInt("entityGoldenFish", entityIDCategory, 354, minValue, maxValue, "The Entity ID of the Golden Silverfish.");
    }
}
