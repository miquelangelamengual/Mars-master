package me.imluis_.crates.commons;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.imluis_.crates.Crates;
import me.imluis_.crates.commons.event.impl.CrateHologramDespawnEvent;
import me.imluis_.crates.commons.event.impl.CrateHologramSpawnEvent;
import me.imluis_.crates.commons.menu.CrateEditMenu;
import me.imluis_.crates.commons.menu.CrateLootMenu;
import me.imluis_.crates.util.ChatUtil;
import me.imluis_.crates.util.LocationUtil;
import me.imluis_.crates.util.serializer.impl.LocationSerializer;

public class CrateListener implements Listener {
	
	private LocationSerializer locationSerializer = new LocationSerializer();
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		
		if(player.getItemInHand() != null) {
			ItemStack hand = player.getItemInHand();
			
			if(hand.hasItemMeta() && hand.getItemMeta().hasDisplayName()) {
				if(hand.getItemMeta().getDisplayName().contains(CrateManager.META)) {
					if(!player.getGameMode().equals(GameMode.CREATIVE)) {
						event.setCancelled(true);
						return;
					}
					
					String displayName = ChatColor.stripColor(hand.getItemMeta().getDisplayName());
					String name = displayName.toLowerCase().replace(" loot chest", "").replace(CrateManager.META, "");
					
					Crate crate = Crates.getInstance().getCrateManager().getCrate(name);
					
					if(crate != null) {
						
						String serializedLocation = locationSerializer.serialize(event.getBlockPlaced().getLocation().clone().add(0.5, 0, 0.5));
						
						if(Crates.getInstance().canCreateHolograms()) {
							new CrateHologramSpawnEvent(crate, event.getBlockPlaced().getLocation().clone().add(0.5, 3, 0.5)).call();
						}
						
						crate.getCrateLocations().add(serializedLocation);
						
						ChatUtil.sendMessage(player, "&6[Crates] &fYou have placed the '&7" + crate.getName() + "&f' at '&7" + LocationUtil.getFormatted(event.getBlockPlaced().getLocation(), true) + "&f' location.");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation().clone().add(0.5, 0, 0.5);
		Crate crate = Crates.getInstance().getCrateManager().getCrate(location);
		
		if(crate != null) {
			if(player.isSneaking() && player.getGameMode().equals(GameMode.CREATIVE)) {
				String serialziedLocation = locationSerializer.serialize(location);
				
				if(Crates.getInstance().canCreateHolograms()) {
					new CrateHologramDespawnEvent(crate, location.clone().add(0, 3, 0)).call();
				}
				
				crate.getCrateLocations().remove(serialziedLocation);
				ChatUtil.sendMessage(player, "&6[Crates] &fYou have removed the '&7" + crate.getName() + "&f' from '&7" + LocationUtil.getFormatted(event.getBlock().getLocation(), true) + "&f' location.");
				return;
			}
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();	
		Action action =  event.getAction();
		
		if(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) { 
			Block block = event.getClickedBlock();
			
			Location blockLocation = block.getLocation();
			ItemStack hand = player.getItemInHand();
			
			blockLocation.setX(blockLocation.getBlockX());
			blockLocation.setZ(blockLocation.getBlockZ());
			
			Crate crate = Crates.getInstance().getCrateManager().getCrate(blockLocation.add(0.5, 0, 0.5));
			
			if(crate != null) {
				
				if(action == Action.RIGHT_CLICK_BLOCK) {
					if(player.isSneaking()) {
						if(player.getGameMode().equals(GameMode.CREATIVE) && player.hasPermission("crates.edit")) {
							new CrateEditMenu(player, crate);
						}
					}else {
						if(Crates.getInstance().getCrateManager().isKey(crate, hand)) {
							
							if(crate.giveLoot(player)) {
								if(hand.getAmount() == 1) {
									player.setItemInHand(new ItemStack(Material.AIR, 1));
								}else{
									hand.setAmount(hand.getAmount() - 1);
								}
								
								player.updateInventory();
								player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.5F, 0.5F);
							}
							
						}else {
							ChatUtil.sendMessage(player, "&6[Crates] &fYou need " + crate.getColor() + crate.getName() + "&f key.");
						}
					}
					
					event.setCancelled(true);
				}
				
				if(action == Action.LEFT_CLICK_BLOCK) {
					if(player.isSneaking()) {
						if(!player.getGameMode().equals(GameMode.CREATIVE)) {
							event.setCancelled(true);
						}
					}else {
						if(!crate.getLoot().values().isEmpty()) {
							new CrateLootMenu(crate).openMenu(player);
						}
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
