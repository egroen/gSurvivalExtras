package com.egroen.bukkit.gsurvivalextras;

/**
 * SubModule interface
 * This is used to identify/start the submodules of the plugin.
 * 
 * @author egroen
 *
 */
public abstract class SubModule {
	protected MasterModule plugin;
	public SubModule(MasterModule plugin) {
		this.plugin = plugin;
	}
	
	public abstract void start();
	public abstract void stop();
}
