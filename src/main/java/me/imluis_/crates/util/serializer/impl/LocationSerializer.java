package me.imluis_.crates.util.serializer.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.imluis_.crates.util.serializer.ObjectSerializer;

public class LocationSerializer extends ObjectSerializer<Location> {

	@Override
	public String serialize(Location location) {
		
		return location.getWorld().getName() + "-" + location.getX() + "-" + location.getY() + "-" + location.getZ();
	}

	@Override
	public Location deserialize(String source) {
		
		String[] spliter = source.split("-");
		
		try {
			Location location = new Location(Bukkit.getWorld(spliter[0]), Integer.parseInt(spliter[1]), Integer.parseInt(spliter[2]), Integer.parseInt(spliter[3]));
			return location;
		} catch (Exception e) { }
		
		return null;
	}
}
