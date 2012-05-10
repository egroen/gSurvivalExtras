package com.egroen.bukkit.gsurvivalextras.subplugins;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.world.StructureGrowEvent;

import com.egroen.bukkit.gsurvivalextras.configuration.*;
import com.egroen.bukkit.gsurvivalextras.MasterModule;
import com.egroen.bukkit.gsurvivalextras.SubModule;

/*
 * Not finished, currently only reading data.
 */
public class NoGrow extends SubModule implements Listener {
	private Config grow = ConfigManager.getInstance().loadConfig("nogrowGrowth.yml");

	public NoGrow(MasterModule plugin) {
		super(plugin);
	}

	@Override
	public void start() {
		plugin.registerListener(this);
	}

	@Override
	public void stop() {
	}
	
	private enum type { SPREAD, GROWTH, STRUCTURE };
	
	@EventHandler
	public void onBlockSpread(BlockSpreadEvent e) {
		registerGrow(type.SPREAD, e.getNewState().getType().toString());
	}
	
	@EventHandler
    public void onBlockGrow(BlockGrowEvent e) {
		 //if (e.isCancelled()) return;
		registerGrow(type.GROWTH, e.getNewState().getType().toString());
		
		 //List<String> grows = plugin.getConfig().getStringList("nogrowmaterials");
		 //if (grows.contains(e.getBlock().getType().toString())) return;
		 //grows.add(e.getBlock().getType().toString());
		 //plugin.saveConfig();
    }
	
	@EventHandler
	public void onStructureGrow(StructureGrowEvent e) {
		registerGrow(type.STRUCTURE, e.getSpecies().name());
	}
	
	private void registerGrow(type t, String i) {
		List<String> l = grow.getStringList(t.name());
		if (l == null) l = new ArrayList<String>();
		if (l.contains(i)) return;
		l.add(i);
		grow.set(t.name(), l);
		grow.save();
	}

}
