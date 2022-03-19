package me.imluis_.crates.commons;

import org.bukkit.Material;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum CrateType {

	CHEST(Material.CHEST),
	ENDERCHEST(Material.ENDER_CHEST),
	ENCHANTMENT_TABBLE(Material.ENCHANTMENT_TABLE),
	BEACON(Material.BEACON);
	
	private Material material;
}
