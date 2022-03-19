package me.imluis_.crates.commons.command.argument;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.imluis_.crates.Crates;
import me.imluis_.crates.commons.Crate;
import me.imluis_.crates.commons.command.argument.completer.CratesCompleter;
import me.imluis_.crates.util.ChatUtil;
import me.imluis_.crates.util.command.CommandArgument;

public class CrateLootChestArgument extends CommandArgument {

	public CrateLootChestArgument() {
		super("lootchest", "Get the loot chest.", "crate.command.lootchest");
	}

	@Override
	public String getUsage(String label) {
		// TODO Auto-generated method stub
		return "/" + label + ' ' + this.getName() + " <crate name>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			ChatUtil.sendMessage(sender,  "&cIncorrect usage, please use: " + this.getUsage(label));
			return true;
		}
		
		Crate crate = Crates.getInstance().getCrateManager().getCrate(args[0]);
		
		if(crate == null) {
			ChatUtil.sendMessage(sender, "&cCrate '&r" + args[0] + "&c' not found.");
			return true;
		}
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			ChatUtil.sendMessage(sender, "&6[Crates] &fAdded the loot chest of '&7" + crate.getName() + "&f' crate.");
			Crates.getInstance().getCrateManager().giveCrate(player, crate);
		} else {
			ChatUtil.sendMessage(sender, "&cOnly players can get the crate lootchest.");
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return new CratesCompleter().onTabComplete(sender, command, label, args);
	}
}
