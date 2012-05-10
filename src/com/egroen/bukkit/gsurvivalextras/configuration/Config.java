package com.egroen.bukkit.gsurvivalextras.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Config - A Yaml config
 * 
 * This gives some extra methods to the original YamlConfiguration.
 * @author egroen
 *
 */
public class Config extends YamlConfiguration {
	private JavaPlugin plugin;
	private String file;
	
	public Config(JavaPlugin plugin, String file) {
		this.plugin = plugin;
		this.file = file;
		this.reload();
	}
	
	public void reload() {
		try { load(new File(plugin.getDataFolder(), file)); } catch (Exception e) { }
		try {				// This part will give an error about being unable to read trough the zipstream of the jar, not being able to get around it yet.
			InputStream defConfigStream = plugin.getClass().getClassLoader().getResourceAsStream(file);
			if (defConfigStream != null) {
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				setDefaults(defConfig);
			}
		} catch (Exception e) {}
	}
	
	public boolean save() {
		try { this.save(new File(plugin.getDataFolder(), file)); } catch (IOException e) { return false; }
		return true;
	}
}
