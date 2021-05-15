package com.mineria.mod.util;

import com.mineria.mod.Mineria;
import com.mineria.mod.blocks.barrel.copper.ContainerCopperWaterBarrel;
import com.mineria.mod.blocks.barrel.copper.GuiCopperWaterBarrel;
import com.mineria.mod.blocks.barrel.copper.TileEntityCopperWaterBarrel;
import com.mineria.mod.blocks.barrel.diamond.ContainerDiamondFluidBarrel;
import com.mineria.mod.blocks.barrel.diamond.GuiDiamondFluidBarrel;
import com.mineria.mod.blocks.barrel.diamond.TileEntityDiamondFluidBarrel;
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
	public static final int GUI_TITANE_EXTRACTOR = Mineria.CONFIG.guiTitaneExtractor;
	public static final int GUI_EXTRACTOR = Mineria.CONFIG.guiExtractor;
	public static final int GUI_XP_BLOCK = Mineria.CONFIG.guiXpBlock;
	public static final int GUI_INFUSER = Mineria.CONFIG.guiInfuser;
	public static final int GUI_COPPER_BARREL = Mineria.CONFIG.guiCopperBarrel;
	public static final int GUI_GOLDEN_BARREL = Mineria.CONFIG.guiGoldenBarrel;
	public static final int GUI_DIAMOND_BARREL = Mineria.CONFIG.guiDiamondBarrel;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if(tile != null)
		{
			if(ID == GUI_TITANE_EXTRACTOR)
				return new ContainerTitaneExtractor(player.inventory, (TileEntityTitaneExtractor) tile);
			else if(ID == GUI_EXTRACTOR)
				return new ContainerExtractor(player.inventory, (TileEntityExtractor) tile);
			else if(ID == GUI_XP_BLOCK)
				return new ContainerXpBlock(player.inventory, (TileEntityXpBlock) tile);
			else if(ID == GUI_INFUSER)
				return new ContainerInfuser(player.inventory, (TileEntityInfuser) tile);
			else if(ID == GUI_COPPER_BARREL)
				return new ContainerCopperWaterBarrel(player.inventory, (TileEntityCopperWaterBarrel) tile);
			else if(ID == GUI_GOLDEN_BARREL)
				return new ContainerGoldenWaterBarrel(player.inventory, (TileEntityGoldenWaterBarrel) tile);
			else if(ID == GUI_DIAMOND_BARREL)
				return new ContainerDiamondFluidBarrel(player.inventory, (TileEntityDiamondFluidBarrel) tile);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if(tile != null)
		{
			if(ID == GUI_TITANE_EXTRACTOR)
				return new GuiTitaneExtractor(player.inventory, (TileEntityTitaneExtractor) tile);
			else if(ID == GUI_EXTRACTOR)
				return new GuiExtractor(player.inventory, (TileEntityExtractor) tile);
			else if(ID == GUI_XP_BLOCK)
				return new GuiXpBlock(player.inventory, (TileEntityXpBlock) tile);
			else if(ID == GUI_INFUSER)
				return new GuiInfuser(player.inventory, (TileEntityInfuser) tile);
			else if(ID == GUI_COPPER_BARREL)
				return new GuiCopperWaterBarrel(player.inventory, (TileEntityCopperWaterBarrel) tile);
			else if(ID == GUI_GOLDEN_BARREL)
				return new GuiGoldenWaterBarrel(player.inventory, (TileEntityGoldenWaterBarrel) tile);
			else if(ID == GUI_DIAMOND_BARREL)
				return new GuiDiamondFluidBarrel(player.inventory, (TileEntityDiamondFluidBarrel) tile);
		}
		return null;
	}
}
