package com.mineria.mod.commands;

import java.util.List;

import com.google.common.collect.Lists;

import com.mineria.mod.References;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandHeal extends CommandBase
{
	private final List<String> aliases = Lists.newArrayList(References.MODID + "heal", "hl");
	
	@Override
	public String getName()
	{
		return "heal";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "command.heal.usage";
	}
	
	@Override
	public List<String> getAliases()
	{
		return aliases;
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length > 1)
        {
            throw new WrongUsageException("commands.heal.usage", new Object[0]);
        }
		
		if(sender instanceof EntityPlayer)
		{
			((EntityPlayer) sender).setHealth(20.0F);
		}
	}
}
