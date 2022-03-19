package me.imluis_.crates.commons;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.Setter;
import me.imluis_.crates.util.ChatUtil;
import me.imluis_.crates.util.GenericUtil;
import me.imluis_.crates.util.InventoryUtil;
import me.imluis_.crates.util.ItemBuilder;
import me.imluis_.crates.util.serializer.impl.LocationSerializer;

@Getter
public class Crate implements ConfigurationSerializable {

	private String name;
	private UUID id;
	@Setter private ChatColor color;
	@Setter private CrateType type;
	@Setter private ItemStack itemKey;
	@Setter private ItemStack itemHologram;
	private List<String> crateLocations;
	private Map<Integer, ItemStack> loot;
	
	public Crate(String name) {
		this.name = name;
		this.id = UUID.randomUUID();
		this.color = ChatColor.WHITE;
		this.type = CrateType.CHEST;
		this.itemKey = new ItemStack(Material.TRIPWIRE_HOOK);
		this.itemHologram = new ItemStack(Material.PAPER);
		
		this.crateLocations = Lists.newArrayList();
		this.loot = Maps.newHashMap();
		
		this.loot.put(0, new ItemBuilder(Material.IRON_SWORD).setName("&6Exmaple Sword").build());
	}
	
	public Crate(Map<String, Object> map) {
		this.name = (String) map.get("name");
		this.id = UUID.fromString((String) map.get("id"));
		this.color = ChatColor.valueOf((String) map.get("color"));
		this.type = CrateType.valueOf((String) map.get("type"));
		this.itemKey = (ItemStack) map.get("item-key");
		this.itemHologram = (ItemStack) map.get("item-hologram");
		
		this.crateLocations = GenericUtil.createList(map.get("crate-locations"), String.class);
		
		this.loot = Maps.newHashMap();
		
		for(Map.Entry<Integer, ItemStack> lootEntry : GenericUtil.castMap(map.get("loot"), Integer.class, ItemStack.class).entrySet()) {
			this.loot.put(lootEntry.getKey(), lootEntry.getValue());
		}
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = Maps.newHashMap();
		
		map.put("name", name);
		map.put("id", id.toString());
		map.put("color", color.name());
		map.put("type", type.name());
		map.put("crate-locations", crateLocations);
		map.put("loot", loot);
		map.put("item-key", itemKey);
		map.put("item-hologram", itemHologram);
		
		return map;
	}

	public boolean giveLoot(Player player) {
		if(InventoryUtil.isFull(player)) {
			ChatUtil.sendMessage(player, "&6[Crates] &cYour inventory is full.");
			return false;
		}
		
		List<ItemStack> results = Lists.newArrayList();
		
		for(ItemStack item : this.loot.values()) {
			if(item.getType().equals(Material.STAINED_GLASS_PANE)) {
				continue;
			}
			
			results.add(item);
		}
		
		Random random = new Random();
		
		int select = random.nextInt(results.size());
		
		ItemStack item = results.get(select);
		
		player.getInventory().addItem(item);
		return true;
	}
	
	public void addLoot(Integer position, ItemStack item) {
		loot.put(position, item);
	}
	
	public List<Location> getLocations() {
		List<Location> locations = Lists.newArrayList();
		
		for(String crateLocation : this.crateLocations) {
			LocationSerializer locationSerializer = new LocationSerializer();
			Location location = locationSerializer.deserialize(crateLocation);
			
			if(location != null) {
				locations.add(location);
			}
		}
		
		return locations;
	}
}
