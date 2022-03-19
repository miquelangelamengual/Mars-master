package me.imluis_.crates;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.imluis_.crates.commons.Crate;
import me.imluis_.crates.commons.CrateHologramListener;
import me.imluis_.crates.commons.CrateListener;
import me.imluis_.crates.commons.CrateManager;
import me.imluis_.crates.commons.command.CrateCommand;
import me.imluis_.crates.commons.menu.CrateEditMenu;
import me.imluis_.crates.util.config.Configuration;
import me.imluis_.crates.util.menu.MenuListener;

@Getter
public class Crates extends JavaPlugin {

	@Getter private static Crates instance;
	
	private Configuration configuration;
	private CrateManager crateManager;
	
	@Override
	public void onEnable() {
		instance = this;
				
		ConfigurationSerialization.registerClass(Crate.class);
		
		this.configuration = new Configuration(this, "crates.yml");
		this.crateManager = new CrateManager();
		
		this.getCommand("crates").setExecutor(new CrateCommand());
		
		PluginManager manager = Bukkit.getServer().getPluginManager();
		Arrays.asList(
				new CrateListener(),
				new CrateHologramListener(),
				new CrateEditMenu(),
				new MenuListener()
				).stream().forEach(listener -> manager.registerEvents(listener, this));
	}
	
	@Override
	public void onDisable() {
		this.crateManager.save();
	}
	
	public boolean canCreateHolograms() {
		
		if(Crates.getInstance().getServer().getPluginManager().isPluginEnabled("HolographicDisplays")) { 
			return true;
		}
		
		return false;
	}
}
