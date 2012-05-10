package com.egroen.bukkit.gsurvivalextras.subplugins;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.egroen.bukkit.gsurvivalextras.MasterModule;
import com.egroen.bukkit.gsurvivalextras.SubModule;

public class RSNetherrack extends SubModule implements Listener {

	public RSNetherrack(MasterModule plugin) {
		super(plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start() {
        plugin.registerListener(this);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}
	
    /**
     * Handles redstone change on blocks
     * @param event 
     */
    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
    	try {
    	//if (!event.getBlock().getLocation().getWorld().getName().equals("Hunger")) return;
	    	if (event.getBlock().getType() == Material.NETHERRACK) {
				Block b = event.getBlock();
				Block a = b.getRelative(BlockFace.UP);
	    		if (event.getNewCurrent() > 0) {
	    			if (a.getType() != Material.AIR) return;
	    			a.setType(Material.FIRE);
	    		} else {
	    			if (a.getType() != Material.FIRE) return;
	    			a.setType(Material.AIR);
	    		}
	    	}
    	} catch (Exception e) { } // When netherrack is pushed, power goes down and fire goes out, so fast that the plugin gives error
    }

}
