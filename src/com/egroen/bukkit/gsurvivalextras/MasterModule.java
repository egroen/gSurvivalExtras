package com.egroen.bukkit.gsurvivalextras;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public interface MasterModule {
	public FileConfiguration getConfig();
	//public void registerCommand(String cmd);
	public void registerListener(Listener mod);
}
