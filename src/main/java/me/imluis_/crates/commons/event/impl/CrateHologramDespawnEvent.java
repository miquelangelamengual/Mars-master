package me.imluis_.crates.commons.event.impl;

import org.bukkit.Location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.imluis_.crates.commons.Crate;
import me.imluis_.crates.commons.event.CrateEvent;

@Getter @AllArgsConstructor
public class CrateHologramDespawnEvent extends CrateEvent {

	private Crate crate;
	private Location location;
}
