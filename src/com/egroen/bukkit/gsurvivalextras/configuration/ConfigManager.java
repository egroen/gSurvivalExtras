package com.egroen.bukkit.gsurvivalextras.configuration;

import java.util.HashMap;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ConfigManager - Keeps the configs together
 * Yalm type configuration manager
 * Just caches configs so the remain for re-request.
 * 
 * @author egroen
 *
 */
public class ConfigManager {
	private static ConfigManager _singleton = null;
	public static void install(JavaPlugin plugin) {
		_singleton = new ConfigManager(plugin);
    }
	
	public static ConfigManager getInstance() { return _singleton; }
	
	/**
	 * Holds the directory where the configs are stored
	 */
	private JavaPlugin plugin;
	
	/**
	 * Holds already loaded configs
	 */
	private HashMap<String, Config> _loaded = new HashMap<String, Config>();

	private ConfigManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public Config loadConfig(String config) {
		if (!_loaded.containsKey(config)) {
			Config c = new Config(plugin, config);
			c.options().copyDefaults(true);
			_loaded.put(config, c);
		}
		
		return _loaded.get(config);
	}
}
