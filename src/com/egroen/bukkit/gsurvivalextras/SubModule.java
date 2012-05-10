package com.egroen.bukkit.gsurvivalextras;

public abstract class SubModule {
	protected MasterModule plugin;
	public SubModule(MasterModule plugin) {
		this.plugin = plugin;
	}
	
	public abstract void start();
	public abstract void stop();
}
