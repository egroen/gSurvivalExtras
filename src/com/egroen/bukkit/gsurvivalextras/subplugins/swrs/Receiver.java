package com.egroen.bukkit.gsurvivalextras.subplugins.swrs;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

/**
 * Receiver for SWRS
 * 
 * Just passes the power trough.
 * 
 * @author egroen
 */
public class Receiver implements Listener {

	@EventHandler
	public void onBlockRedstone(BlockRedstoneEvent event) {
		if (event.getBlock().getType() != Material.SIGN_POST
				&& event.getBlock().getType() != Material.WALL_SIGN) return;
		
		Sign sign = (Sign)event.getBlock().getState();
		if (!sign.getLine(0).equals("[receiver]")) return;
		if (!sign.getLine(1).matches("^-?[0-9]+$")) return;
		if (!sign.getLine(2).matches("^-?[0-9]+$")) return;
		if (!sign.getLine(3).matches("^-?[0-9]+$")) return;

		Block transmitter = event.getBlock().getWorld().getBlockAt(
					Integer.parseInt(sign.getLine(1)),
					Integer.parseInt(sign.getLine(2)),
					Integer.parseInt(sign.getLine(3))
				);
		
		Transmitter.setPowered(transmitter, event.getNewCurrent() > 0);
	}
}
