package me.imluis_.crates.commons.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.imluis_.crates.Crates;
import me.imluis_.crates.commons.Crate;
import me.imluis_.crates.util.ItemBuilder;
import me.imluis_.crates.util.menu.Button;
import me.imluis_.crates.util.menu.Menu;

public class CratesMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		// TODO Auto-generated method stub
		return "&6Crates";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<Integer, Button>();
		
		int slot = 0;
		
		for(Crate crate : Crates.getInstance().getCrateManager().getCrates().values()) {
			buttons.put(slot, new CratesButton(crate));
			slot++;
		}
		
		return buttons;
	}

	private static class CratesButton extends Button {

		private Crate crate;
		
		public CratesButton(Crate crate) {
			this.crate = crate;
		}
		
		@Override
		public ItemStack getButtonItem(Player player) {
			
			ItemBuilder itemBuilder = new ItemBuilder(Material.CHEST);
			
			itemBuilder.setName(crate.getColor() + crate.getName());
			itemBuilder.setLore(
					"",
					"&7Left click to view loot.",
					"&7Right click to edit loot.",
					""
					);
			
			return itemBuilder.build();
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
			if(clickType.isLeftClick()) {
				player.closeInventory();
				new CrateLootMenu(this.crate).openMenu(player);
			}
			
			if(player.hasPermission("crates.edit")) {
				if(clickType.isRightClick()) {
					player.closeInventory();
					new CrateEditMenu(player, this.crate);
				}
			}
		}
		
	}
}
