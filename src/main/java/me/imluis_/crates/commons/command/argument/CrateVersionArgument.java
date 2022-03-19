package me.imluis_.crates.commons.command.argument;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.imluis_.crates.util.ChatUtil;
import me.imluis_.crates.util.command.CommandArgument;

public class CrateVersionArgument extends CommandArgument {

	public CrateVersionArgument() {
		super("version", "Get plugin version and developers.");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUsage(String label) {
		// TODO Auto-generated method stub
		return "/" + label + ' ' + this.getName();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		ChatUtil.sendMessage(sender, "&cThis plugin was created by &fimLu1s_ &cand purchased by &fAstro Development &cwith license.", "&cOriginal Developer contact: &rimLu1s_#7950");
		
		return false;
	}

}
