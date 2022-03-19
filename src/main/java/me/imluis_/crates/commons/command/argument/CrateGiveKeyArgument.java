package me.imluis_.crates.commons.command.argument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.imluis_.crates.Crates;
import me.imluis_.crates.commons.Crate;
import me.imluis_.crates.util.BukkitUtil;
import me.imluis_.crates.util.ChatUtil;
import me.imluis_.crates.util.JavaUtil;
import me.imluis_.crates.util.command.CommandArgument;

public class CrateGiveKeyArgument extends CommandArgument {

	public CrateGiveKeyArgument() {
		super("givekey", "Give key to a player.", "crate.command.givekey");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUsage(String label) {
		// TODO Auto-generated method stub
		return "/" + label + ' ' + this.getName() + " <target|all> <crate name> <amount>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length < 3) {
			ChatUtil.sendMessage(sender, "&cInvalid usage, please use: /crate givekey <player|all> <crate name> <amount>");
			return true;
		}
		
		String player = args[0];
		String crateName = args[1];
		String amount = args[2];
		
		if(Crates.getInstance().getCrateManager().getCrate(crateName) == null) {
			ChatUtil.sendMessage(sender, "&cCrate '&f" + crateName + "&c' not found.");
			return true;
		}
		
		Crate crate = Crates.getInstance().getCrateManager().getCrate(crateName);
		
		Integer am = JavaUtil.tryParseInt(amount);
		
		if(am == null) {
			ChatUtil.sendMessage(sender, "&cThe amount is invalid");
			return true;
		}
		
		if(player.equalsIgnoreCase("all")) {
			for(Player target : BukkitUtil.getOnlinePlayers()) {
				if(target != null) {
					Crates.getInstance().getCrateManager().giveKey(target, crate, am);
				
				}
			}
			ChatUtil.sendMessage(sender, "&6[Crates] &fAdded x" + am + "&f " + crate.getColor() + crate.getName() + " &fcrate keys to &7" + BukkitUtil.getOnlinePlayers().size() + " &fplayers.");
		}else {
			Player target = Bukkit.getPlayer(player);
			
			if(target == null) {
				ChatUtil.sendMessage(sender, "&cPlayer '&f" + player + "&c' not found.");
				return true;
			}
			
			Crates.getInstance().getCrateManager().giveKey(target, crate, am);
			ChatUtil.sendMessage(sender, "&6[Crates] &fAdded &7x" + am + "&f " + crate.getColor() + crate.getName() + " &fcrate keys to &7" + target.getName());
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length == 1) {
			List<String> results = BukkitUtil.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
			results.add("ALL");
			return results;
		}
		
		if(args.length == 2) {
			return Crates.getInstance().getCrateManager().getCrates().values().stream().map(Crate::getName).collect(Collectors.toList());
		}
		
		return new ArrayList<String>();
	}
}
