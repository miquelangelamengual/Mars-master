package me.imluis_.crates.commons;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.imluis_.crates.Crates;
import me.imluis_.crates.commons.event.impl.CrateHologramSpawnEvent;
import me.imluis_.crates.util.ChatUtil;
import me.imluis_.crates.util.InventoryUtil;
import me.imluis_.crates.util.ItemBuilder;
import me.imluis_.crates.util.config.Config;
import me.imluis_.crates.util.serializer.impl.LocationSerializer;

public class CrateManager {
	
	private Map<String, Crate> crates = new HashMap<String, Crate>();
    private Config config;
    
    public static String META = "񗉲�3";
    
    public CrateManager() {
        this.reload();
        
        if(Crates.getInstance().canCreateHolograms()) {
        	for(Crate crate : this.crates.values()) {
        		for(String serializedLocation : crate.getCrateLocations()) {
        			Location location = new LocationSerializer().deserialize(serializedLocation).clone().add(0, 3, 0);
        			new CrateHologramSpawnEvent(crate, location).call();
        		}
        	}
        }
    }
    
    public Map<String, Crate> getCrates() {
        return crates;
    }

    public Crate getCrate(String name) {
    	
    	if(crates.keySet().contains(name.toLowerCase())) {
    		return crates.get(name.toLowerCase());
    	}
    	
        return null;
    }

    public Crate getCrate(Location location) {
    	
    	for(String key : crates.keySet()) {
    		Crate crate = crates.get(key);
    		
    		for(String serializedLocation : crate.getCrateLocations()) {
    			if(serializedLocation.equals(new LocationSerializer().serialize(location))) {
    				return crate;
    			}
    		}
    	}
    	
    	return null;
    }
    
    public void create(String name) {
    	crates.put(name.toLowerCase(), new Crate(name));
    }
    
    public void delete(Crate crate) {
    	if(crate != null) {
    		crates.remove(crate.getName().toLowerCase());
    	}
    }
    
    public void giveCrate(Player player, Crate crate) {
    	
    	ItemBuilder builder = new ItemBuilder(crate.getType().getMaterial());
    	builder.setName(crate.getColor().toString() + crate.getName() + "&7 Loot Chest" + META);
    	
    	player.getInventory().addItem(builder.build());
    }
    
    public void giveKey(Player player, Crate crate, int amount) {
    	ItemBuilder builder = new ItemBuilder(crate.getItemKey());
    	builder.setName(crate.getColor().toString() + crate.getName() + "&7 Key" + META);
    	builder.setAmount(amount);
    	
    	ItemStack item = builder.build();
    	
    	if(InventoryUtil.isFull(player)) {
    		player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
    	}else{
    		player.getInventory().addItem(item);
    	}
    	
    	ChatUtil.sendMessage(player, "&6[Crates] &fAdded x" + amount + " " + crate.getColor().toString() + crate.getName() + "&f key to your inventory.");
    }
    
    public boolean isKey(Crate crate, ItemStack item) {
    	

		if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			if(item.getItemMeta().getDisplayName().contains(CrateManager.META)) {
				String displayName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
				String name = displayName.toLowerCase().replace(" key", "").replace(CrateManager.META, "");
				
				Crate crate2 = Crates.getInstance().getCrateManager().getCrate(name);
				
				if(crate2 != null) {
					if(crate.getName().equalsIgnoreCase(crate2.getName())) {
						return true;
					}
				}
			}
		}
    	
    	return false;
    }
    
    /**
     * Loads the Crate data from storage.
     */
    public void reload() {
        this.config = new Config(Crates.getInstance(), "crates");

        Object object = config.get("crates");
        if (object instanceof MemorySection) {
            MemorySection section = (MemorySection) object;
            Collection<String> keys = section.getKeys(false);
            for (String id : keys) {
                crates.put(id, (Crate) config.get(section.getCurrentPath() + '.' + id));
            }
        }
    }

    /**
     * Saves the Crate data to storage.
     */
    public void save() {
        Set<Map.Entry<String, Crate>> entrySet = crates.entrySet();
        Map<String, Crate> saveMap = new LinkedHashMap<String, Crate>(entrySet.size());
        for (Map.Entry<String, Crate> entry : entrySet) {
            saveMap.put(entry.getKey().toString(), entry.getValue());
        }

        config.set("crates", saveMap);
        config.save();
    }
}
