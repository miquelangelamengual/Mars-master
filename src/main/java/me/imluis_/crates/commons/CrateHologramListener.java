package me.imluis_.crates.commons;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.imluis_.crates.Crates;
import me.imluis_.crates.commons.event.impl.CrateHologramDespawnEvent;
import me.imluis_.crates.commons.event.impl.CrateHologramSpawnEvent;
import me.imluis_.crates.util.ChatUtil;

public class CrateHologramListener implements Listener {

	private Hologram getHologram(Location location) {
		
		for(Hologram hologram : HologramsAPI.getHolograms(Crates.getInstance())) {
			if(hologram.getLocation().equals(location)) {
				return hologram;
			}
		}
		
		return null;
	}
	
	@EventHandler
	public void onHologramSpawn(CrateHologramSpawnEvent event) {
		Hologram hologram = HologramsAPI.createHologram(Crates.getInstance(), event.getLocation());
		hologram.getVisibilityManager().setVisibleByDefault(true);
		hologram.appendItemLine(event.getCrate().getItemHologram());
		hologram.appendTextLine(ChatUtil.translate("&7&m----------------"));
		hologram.appendTextLine(ChatUtil.translate(event.getCrate().getColor().toString() + event.getCrate().getName() + "&7 Crate"));
		hologram.appendTextLine(ChatUtil.translate("&fRight click to open."));
		hologram.appendTextLine(ChatUtil.translate("&7&m----------------"));
	}
	
	@EventHandler
	public void onHologramDespawn(CrateHologramDespawnEvent event) {
		Hologram hologram = getHologram(event.getLocation());
		if(hologram != null) {
			hologram.delete();
		}
	}
}
