package me.imluis_.crates.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocationUtil {
	
    public static String getString(Location loc) {
        StringBuilder builder = new StringBuilder();

        if (loc == null) return "unset";

        builder.append(loc.getX()).append("|");
        builder.append(loc.getY()).append("|");
        builder.append(loc.getZ()).append("|");
        builder.append(loc.getWorld().getName()).append("|");
        builder.append(loc.getYaw()).append("|");
        builder.append(loc.getPitch());

        return builder.toString();
    }

    public static Location getLocation(String s) {
        if (s == null || s.equals("unset") || s.equals("")) return null;

        String[] data = s.split("\\|");
        double x = Double.parseDouble(data[0]);
        double y = Double.parseDouble(data[1]);
        double z = Double.parseDouble(data[2]);
        World world = Bukkit.getWorld(data[3]);
        Float yaw = Float.parseFloat(data[4]);
        Float pitch = Float.parseFloat(data[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String getFormatted(Location location, boolean world) {
    	return (world ? location.getWorld().getName() + ", " : "") + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }
    
    public static boolean isSameLocation(Location loc1, Location loc2) {
        return loc1 != null && loc2 != null && loc1.equals(loc2);
    }
    
    public static void multiplyVelocity(Player player, Vector vector, double multiply, double addY) {
        vector.normalize();
        vector.multiply(multiply);
        vector.setY(vector.getY() + addY);
        player.setFallDistance(0.0F);

        player.setVelocity(vector);
    }
}
