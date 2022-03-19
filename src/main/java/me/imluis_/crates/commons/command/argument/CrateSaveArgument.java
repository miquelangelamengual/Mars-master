package me.imluis_.crates.commons.command.argument;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.imluis_.crates.Crates;
import me.imluis_.crates.util.ChatUtil;
import me.imluis_.crates.util.command.CommandArgument;

public class CrateSaveArgument extends CommandArgument {

	public CrateSaveArgument() {
		super("save", "Saves all crates to yaml.", "crate.command.save");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUsage(String label) {
		// TODO Auto-generated method stub
		return "/" + label + ' ' + this.getName();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ChatUtil.sendMessage(sender, "&6[Crates] &aAll crates has been saved to the yml storage.");
		Crates.getInstance().getCrateManager().save();
		return false;
	}

}
