package me.imluis_.crates.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ChatUtil {

	public static String MENU_BAR;
    public static String CHAT_BAR;
    public static String SB_BAR;
    public static String TAB_BAR;
    public static String COLOR_CHAR;
	
	public static String translate(String value) {
		return ChatColor.translateAlternateColorCodes('&', value);
	}
	
	public static List<String> translate(Iterable<? extends String> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).filter(Objects::nonNull).map(ChatUtil::translate).collect(Collectors.toList());
    }

    public static List<String> translate(List<String> list) {
        List<String> buffered = new ArrayList<String>();
        
        list.forEach(string -> buffered.add(translate(string)));
        return buffered;
    }

    public static String[] translate(String... strings) {
    	return translate(Arrays.asList(strings)).stream().toArray(String[]::new);
    }
	
	public static void sendMessage(CommandSender sender, String value) {
		sender.sendMessage(translate(value));
	}
	
	public static void sendMessage(CommandSender sender, List<String> values) {
		for(String value : values) {
			sender.sendMessage(translate(value));
		}
	}
	
	public static void sendMessage(CommandSender sender, String... values) {
		for(String value : values) {
			sender.sendMessage(translate(value));
		}
	}
	
	public static void sendMessage(Player player, String value) {
		player.sendMessage(translate(value));
	}
	
	public static void sendMessage(Player player, List<String> values) {
		for(String value : values) {
			player.sendMessage(translate(value));
		}
	}
	
	public static void sendMessage(Player player, String... values) {
		for(String value : values) {
			player.sendMessage(translate(value));
		}
	}
}
