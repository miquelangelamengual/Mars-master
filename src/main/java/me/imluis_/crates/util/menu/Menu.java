package me.imluis_.crates.util.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.imluis_.crates.util.ChatUtil;

public abstract class Menu {
	
    public static Map<String, Menu> currentlyOpenedMenus;
    private Map<Integer, Button> buttons;
    private boolean updateAfterClick;
    private boolean closedByMenu;
    private boolean placeholder;
    private Button placeholderButton;
    
    public Menu() {
        this.buttons = new HashMap<Integer, Button>();
        this.updateAfterClick = true;
        this.closedByMenu = false;
        this.placeholder = false;
        this.placeholderButton = Button.placeholder(Material.STAINED_GLASS_PANE, (byte)15, " ");
    }
    
    private ItemStack createItemStack(Player player, Button button) {
        ItemStack item = button.getButtonItem(player);
        if (item.getType() != Material.SKULL_ITEM) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                meta.setDisplayName(meta.getDisplayName());
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    
    public void openMenu(Player player) {
        this.buttons = this.getButtons(player);
        Menu previousMenu = Menu.currentlyOpenedMenus.get(player.getName());
        Inventory inventory = null;
        int size = (this.getSize() == -1) ? this.size(this.buttons) : this.getSize();
        boolean update = false;
        String title = ChatUtil.translate(this.getTitle(player));
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }
        if (player.getOpenInventory() != null) {
            if (previousMenu == null) {
                player.closeInventory();
            }
            else {
                int previousSize = player.getOpenInventory().getTopInventory().getSize();
                if (previousSize == size && player.getOpenInventory().getTopInventory().getTitle().equals(title)) {
                    inventory = player.getOpenInventory().getTopInventory();
                    update = true;
                }
                else {
                    previousMenu.setClosedByMenu(true);
                    player.closeInventory();
                }
            }
        }
        if (inventory == null) {
            inventory = Bukkit.createInventory((InventoryHolder)player, size, title);
        }
        inventory.setContents(new ItemStack[inventory.getSize()]);
        Menu.currentlyOpenedMenus.put(player.getName(), this);
        for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet()) {
            inventory.setItem((int)buttonEntry.getKey(), this.createItemStack(player, buttonEntry.getValue()));
        }
        if (this.isPlaceholder()) {
            for (int index = 0; index < size; ++index) {
                if (this.buttons.get(index) == null) {
                    this.buttons.put(index, this.placeholderButton);
                    inventory.setItem(index, this.placeholderButton.getButtonItem(player));
                }
            }
        }
        if (update) {
            player.updateInventory();
        }
        else {
            player.openInventory(inventory);
        }
        this.onOpen(player);
        this.setClosedByMenu(false);
    }
    
    public int size(Map<Integer, Button> buttons) {
        int highest = 0;
        for (int buttonValue : buttons.keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }
        return (int)(Math.ceil((highest + 1) / 9.0) * 9.0);
    }
    
    public int getSize() {
        return -1;
    }
    
    public int getSlot(int x, int y) {
        return 9 * y + x;
    }
    
    public abstract String getTitle(Player player);
    
    public abstract Map<Integer, Button> getButtons(Player player);
    
    public void onOpen(Player player) {
    }
    
    public void onClose(Player player) {
    }
    
    public Map<Integer, Button> getButtons() {
        return this.buttons;
    }
    
    public boolean isUpdateAfterClick() {
        return this.updateAfterClick;
    }
    
    public boolean isClosedByMenu() {
        return this.closedByMenu;
    }
    
    public boolean isPlaceholder() {
        return this.placeholder;
    }
    
    public Button getPlaceholderButton() {
        return this.placeholderButton;
    }
    
    public void setButtons(Map<Integer, Button> buttons) {
        this.buttons = buttons;
    }
    
    public void setUpdateAfterClick(boolean updateAfterClick) {
        this.updateAfterClick = updateAfterClick;
    }
    
    public void setClosedByMenu(boolean closedByMenu) {
        this.closedByMenu = closedByMenu;
    }
    
    public void setPlaceholder(boolean placeholder) {
        this.placeholder = placeholder;
    }
    
    public void setPlaceholderButton(Button placeholderButton) {
        this.placeholderButton = placeholderButton;
    }
    
    static {
        Menu.currentlyOpenedMenus = new HashMap<String, Menu>();
    }
}
