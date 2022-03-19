package me.imluis_.crates.commons.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.imluis_.crates.commons.Crate;
import me.imluis_.crates.util.menu.Button;
import me.imluis_.crates.util.menu.Menu;

public class CrateLootMenu extends Menu {

	private Crate crate;
	
	public CrateLootMenu(Crate crate) {
		this.crate = crate;
	}
	
	@Override
	public String getTitle(Player player) {
		// TODO Auto-generated method stub
		return crate.getColor() + crate.getName() + "&7 Loot";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<Integer, Button>();
		
		for(Integer pos : crate.getLoot().keySet()) {
			buttons.put(pos, new LootButton(crate.getLoot().get(pos)));
		}
		
		return buttons;
	}

	private static class LootButton extends Button {

		private ItemStack crateItem;
		
		public LootButton(ItemStack crateItem) {
			this.crateItem = crateItem;
		}
		
		@Override
		public ItemStack getButtonItem(Player player) {
			return crateItem;
		}
	}
}
