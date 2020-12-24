package com.mineria.mod.util.handler;

import com.mineria.mod.References;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == References.GUI_EXTRACTOR) return new ContainerExtractor(player.inventory, (TileEntityExtractor)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == References.GUI_TITANE_EXTRACTOR) return new ContainerTitaneExtractor(player.inventory, (TileEntityTitaneExtractor)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == References.GUI_XP_BLOCK) return new ContainerXpBlock(player.inventory, (TileEntityXpBlock)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == References.GUI_INFUSER) return new ContainerInfuser(player.inventory, (TileEntityInfuser) world.getTileEntity(new BlockPos(x,y,z)));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == References.GUI_EXTRACTOR) return new GuiExtractor(player.inventory, (TileEntityExtractor)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == References.GUI_TITANE_EXTRACTOR) return new GuiTitaneExtractor(player.inventory, (TileEntityTitaneExtractor)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == References.GUI_XP_BLOCK) return new GuiXpBlock(player.inventory, (TileEntityXpBlock)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == References.GUI_INFUSER) return new GuiInfuser(player.inventory, (TileEntityInfuser) world.getTileEntity(new BlockPos(x,y,z)));
		return null;
	}
}
