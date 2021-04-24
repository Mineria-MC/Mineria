package com.mineria.mod.util;

import com.mineria.mod.blocks.barrel.copper.ContainerCopperWaterBarrel;
import com.mineria.mod.blocks.barrel.copper.GuiCopperWaterBarrel;
import com.mineria.mod.blocks.barrel.copper.TileEntityCopperWaterBarrel;
import com.mineria.mod.blocks.barrel.golden.ContainerGoldenWaterBarrel;
import com.mineria.mod.blocks.barrel.golden.GuiGoldenWaterBarrel;
import com.mineria.mod.blocks.barrel.golden.TileEntityGoldenWaterBarrel;
import com.mineria.mod.blocks.extractor.ContainerExtractor;
import com.mineria.mod.blocks.extractor.GuiExtractor;
import com.mineria.mod.blocks.extractor.TileEntityExtractor;
import com.mineria.mod.blocks.infuser.ContainerInfuser;
import com.mineria.mod.blocks.infuser.GuiInfuser;
import com.mineria.mod.blocks.infuser.TileEntityInfuser;
import com.mineria.mod.blocks.titane_extractor.ContainerTitaneExtractor;
import com.mineria.mod.blocks.titane_extractor.GuiTitaneExtractor;
import com.mineria.mod.blocks.titane_extractor.TileEntityTitaneExtractor;
import com.mineria.mod.blocks.xp_block.ContainerXpBlock;
import com.mineria.mod.blocks.xp_block.GuiXpBlock;
import com.mineria.mod.blocks.xp_block.TileEntityXpBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	public static final int GUI_TITANE_EXTRACTOR = 1;
	public static final int GUI_EXTRACTOR = 2;
	public static final int GUI_XP_BLOCK = 3;
	public static final int GUI_INFUSER = 4;
	public static final int GUI_COPPER_BARREL = 5;
	public static final int GUI_GOLDEN_BARREL = 6;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if(tile != null)
		{
			switch (ID)
			{
				case GUI_TITANE_EXTRACTOR:
					return new ContainerTitaneExtractor(player.inventory, (TileEntityTitaneExtractor) tile);
				case GUI_EXTRACTOR:
					return new ContainerExtractor(player.inventory, (TileEntityExtractor) tile);
				case GUI_XP_BLOCK:
					return new ContainerXpBlock(player.inventory, (TileEntityXpBlock) tile);
				case GUI_INFUSER:
					return new ContainerInfuser(player.inventory, (TileEntityInfuser) tile);
				case GUI_COPPER_BARREL:
					return new ContainerCopperWaterBarrel(player.inventory, (TileEntityCopperWaterBarrel) tile);
				case GUI_GOLDEN_BARREL:
					return new ContainerGoldenWaterBarrel(player.inventory, (TileEntityGoldenWaterBarrel) tile);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if(tile != null)
		{
			switch (ID)
			{
				case GUI_TITANE_EXTRACTOR:
					return new GuiTitaneExtractor(player.inventory, (TileEntityTitaneExtractor) tile);
				case GUI_EXTRACTOR:
					return new GuiExtractor(player.inventory, (TileEntityExtractor) tile);
				case GUI_XP_BLOCK:
					return new GuiXpBlock(player.inventory, (TileEntityXpBlock) tile);
				case GUI_INFUSER:
					return new GuiInfuser(player.inventory, (TileEntityInfuser) tile);
				case GUI_COPPER_BARREL:
					return new GuiCopperWaterBarrel(player.inventory, (TileEntityCopperWaterBarrel) tile);
				case GUI_GOLDEN_BARREL:
					return new GuiGoldenWaterBarrel(player.inventory, (TileEntityGoldenWaterBarrel) tile);
			}
		}
		return null;
	}
}
