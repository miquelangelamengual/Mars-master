package me.imluis_.crates.commons.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

public class CrateEvent extends Event {

	@Getter
	private static HandlerList handlerList = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public void call() {
		Bukkit.getServer().getPluginManager().callEvent(this);
	}
}
