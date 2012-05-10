package com.egroen.bukkit.gsurvivalextras.subplugins.swrs;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Sensor implements Listener {
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		
		// Check if we stayed on the same block (if so, do nothing)
		if (event.getFrom().getBlockX() == event.getTo().getBlockX()
				&& event.getFrom().getBlockY() == event.getTo().getBlockY()
				&& event.getFrom().getBlockZ() == event.getTo().getBlockZ()
				&& event.getFrom().getWorld() == event.getTo().getWorld()) return;
		
		// Get blocks of possible sensor position
		Block bFrom = event.getFrom().getBlock().getRelative(0, -2, 0);
		Block bTo = event.getTo().getBlock().getRelative(0, -2, 0);
		
		// Check for signs
		Sign sFrom = null, sTo=null;
		if (bFrom.getState() instanceof Sign) {
			sFrom = (Sign)bFrom.getState();
			if (!sFrom.getLine(0).equals("[sensor]")) sFrom = null;		// Not a sensor sign
		}
		if (bTo.getState() instanceof Sign) {
			sTo = (Sign)bTo.getState();
			if (!sTo.getLine(0).equals("[sensor]")) sTo = null;		// Not a sensor sign
		}
		
		// If both are signs AND point to the same transmitter, stop, should already be up then.
		if (sFrom != null && sTo != null
				&& sFrom.getLine(1).equals(sTo.getLine(1))
				&& sFrom.getLine(2).equals(sTo.getLine(2))
				&& sFrom.getLine(3).equals(sTo.getLine(3))
				&& bFrom.getWorld() == bTo.getWorld()) return;		// Went to other sensor on same target, ignore


		// If we came here, we did not move to the same sensor or something like that.
		if (sFrom != null) {			// Check if we left a sensor
			Block transmitter = bFrom.getWorld().getBlockAt(
					Integer.parseInt(sFrom.getLine(1)),
					Integer.parseInt(sFrom.getLine(2)),
					Integer.parseInt(sFrom.getLine(3))
				);
			Transmitter.setPowered(transmitter, false);
		}
		if (sTo != null) {			// Check if we entered a sensor
			Block transmitter = bTo.getWorld().getBlockAt(
					Integer.parseInt(sTo.getLine(1)),
					Integer.parseInt(sTo.getLine(2)),
					Integer.parseInt(sTo.getLine(3))
				);
			Transmitter.setPowered(transmitter, true);
		}
	}
}
