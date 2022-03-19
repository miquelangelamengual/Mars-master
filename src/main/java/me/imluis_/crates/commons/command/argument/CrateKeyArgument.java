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

public class CrateKeyArgument extends CommandArgument {

	public CrateKeyArgument() {
		super("key", "Set crate key.", "crate.command.key", new String[] { "setkey" });
		this.isPlayerOnly = true;
	}

	@Override
	public String getUsage(String label) {
		// TODO Auto-generated method stub
		return "/" + label + ' ' + this.getName();
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
		
		Player player = (Player) sender;
		if(player.getItemInHand() == null) {
			ChatUtil.sendMessage(sender, "&cItem in hand cannot be null.");
			return true;
		}
		
		crate.setItemKey(player.getItemInHand());
		
		ChatUtil.sendMessage(sender, "&6[Crates] &fYou have seted key type to the '&7" + args[0] + "&f' crate.");
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return new CratesCompleter().onTabComplete(sender, command, label, args);
	}
}
