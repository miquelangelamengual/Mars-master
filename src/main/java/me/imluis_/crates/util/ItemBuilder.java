package me.imluis_.crates.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import lombok.Setter;

public class ItemBuilder implements Cloneable {
	
    @Setter private Material material;
    private int data, amount;
    private String name;
    private boolean unbreakable;
    
    private List<String> lore = new ArrayList<String>();
    private Color color;
    
    private HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
    
    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, 0);
    }

    public ItemBuilder(Material material, int amount, int data) {
        this.material = material;
        this.amount = amount;
        this.data = data;
        this.unbreakable = false;
    }
    
    public ItemBuilder(ItemStack itemStack) {
    	Validate.notNull(itemStack, "ItemStack cannot be null");
    	
        this.lore = new ArrayList<String>();
        this.enchantments = new HashMap<Enchantment, Integer>();
        
        this.material = itemStack.getType();
        this.data = itemStack.getDurability();
        this.amount = itemStack.getAmount();
        
        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasDisplayName()) {
            	this.name = itemStack.getItemMeta().getDisplayName();
            }
            
            if (itemStack.getItemMeta().hasLore()) {
            	this.lore = (List<String>)itemStack.getItemMeta().getLore();
            }
        }
        if (itemStack.getEnchantments() != null) {
        	this.enchantments.putAll(itemStack.getEnchantments());
        }
        
        if (itemStack.getType().toString().toLowerCase().contains("leather") && itemStack.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)itemStack.getItemMeta();
            this.color = leatherArmorMeta.getColor();
        }
    }

    public ItemBuilder setData(int data) {
        this.data = data;
        return this;
    }
 
    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }
    
    public ItemBuilder setName(String string) {
        this.name = ChatUtil.translate(string);
        return this;
    }
    
    public ItemBuilder setLore(String... strings) {
        this.lore = ChatUtil.translate(Arrays.asList(strings));
        return this;
    }

    public ItemBuilder setUnrepairable() {
    	
    	if(this.lore != null) {
    		lore.add(ChatColor.BLUE + "Unrepairable");
    	}
    	
    	return this;
    }
    
    public ItemBuilder setUndropable() {
    	
    	if(this.lore != null) {
    		lore.add(ChatColor.BLUE + "Undropable");
    	}
    	
    	return this;
    }
    
    public ItemBuilder setUnbreakable() {
    	
    	this.unbreakable = true;
    	
    	return this;
    }
    
    public ItemBuilder setLore(List<String> list) {
        this.lore = ChatUtil.translate(list);
        return this;
    }
    
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }
    
    public ItemBuilder setColor(Color color) {
        if (!this.material.toString().toLowerCase().contains("leather")) {
        	throw new RuntimeException("Cannot set color of non-leather items.");
        }
        
        this.color = color;
        return this;
    }
    
    public ItemStack build() {
        Validate.noNullElements(new Object[] { this.material, this.data, this.amount });
        
        ItemStack item = new ItemStack(this.material, this.amount, (short)this.data);
        ItemMeta meta = item.getItemMeta();
        if (this.name != null && this.name != "") {
        	meta.setDisplayName(this.name);
        }
        
        if (this.lore != null && !this.lore.isEmpty()) {
        	meta.setLore(this.lore);
        }
        
        if (this.color != null && this.material.toString().toLowerCase().contains("leather")) {
        	((LeatherArmorMeta)meta).setColor(this.color);
        }
        
        item.setItemMeta(meta);
        if (this.enchantments != null && !this.enchantments.isEmpty()) {
        	item.addUnsafeEnchantments(this.enchantments);
        }
        
        item.getItemMeta().spigot().setUnbreakable(unbreakable);
        
        return item;
    }
}
