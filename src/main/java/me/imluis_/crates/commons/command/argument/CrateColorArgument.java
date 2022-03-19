package me.imluis_.crates.commons.command.argument;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.imluis_.crates.Crates;
import me.imluis_.crates.commons.Crate;
import me.imluis_.crates.commons.command.argument.completer.CratesCompleter;
import me.imluis_.crates.commons.menu.CrateColorMenu;
import me.imluis_.crates.util.ChatUtil;
import me.imluis_.crates.util.command.CommandArgument;

public class CrateColorArgument extends CommandArgument {

	public CrateColorArgument() {
		super("color", "Set crate color.", "crate.command.color", new String[] { "setcolor" });
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
		if(!(sender instanceof Player)) {
			ChatUtil.sendMessage(sender, "&cOnly players can execute this command.");
			return true;
		}
		
		new CrateColorMenu(crate).openMenu((Player) sender);
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return new CratesCompleter().onTabComplete(sender, command, label, args);
	}
}
