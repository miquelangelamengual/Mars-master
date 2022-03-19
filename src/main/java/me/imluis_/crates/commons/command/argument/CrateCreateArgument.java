package me.imluis_.crates.commons.command.argument;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.imluis_.crates.Crates;
import me.imluis_.crates.commons.Crate;
import me.imluis_.crates.util.ChatUtil;
import me.imluis_.crates.util.command.CommandArgument;

public class CrateCreateArgument extends CommandArgument {

	public CrateCreateArgument() {
		super("create", "Crates a new crate.", "crate.command.create");
	}

	@Override
	public String getUsage(String label) {
		// TODO Auto-generated method stub
		return "/" + label + ' ' + this.getName() + " <name>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			ChatUtil.sendMessage(sender,  "&cIncorrect usage, please use: " + this.getUsage(label));
			return true;
		}
		
		Crate crate = Crates.getInstance().getCrateManager().getCrate(args[0]);
		
		if(crate != null) {
			ChatUtil.sendMessage(sender, "&cCrate already exists.");
			return true;
		}
		
		Crates.getInstance().getCrateManager().create(args[0]);
		ChatUtil.sendMessage(sender, "&6[Crates] &fYou have created the '&7" + args[0] + "&f' crate.");
		return false;
	}
}
