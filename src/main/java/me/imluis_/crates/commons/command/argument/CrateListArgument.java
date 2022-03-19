package me.imluis_.crates.commons.command.argument;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.imluis_.crates.commons.menu.CratesMenu;
import me.imluis_.crates.util.ChatUtil;
import me.imluis_.crates.util.command.CommandArgument;

public class CrateListArgument extends CommandArgument {

	public CrateListArgument() {
		super("list", "Open a menu with all crates.", "crate.command.list");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUsage(String label) {
		// TODO Auto-generated method stub
		return "/" + label + ' ' + this.getName();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			new CratesMenu().openMenu(player);
		} else {
			ChatUtil.sendMessage(sender, "&cOnly players can execute this command.");
		}
		return false;
	}

}
