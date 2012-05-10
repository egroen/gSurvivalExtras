package com.egroen.bukkit.gsurvivalextras.subplugins.swrs;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * Transmitter for SWRS
 * 
 * The transmitter is a block that only knows his state and position.
 * It does not now what triggers him, so multiple sources can give trouble (2 on, 1 goes down, transmitter goes down)
 * @author egroen
 *
 */
public class Transmitter {
	
	private Transmitter() { }
	
	public static boolean isPowered(Block block) {
		return block.getType() == Material.REDSTONE_TORCH_ON || block.getType() == Material.REDSTONE_TORCH_OFF;	// Off is 'hardware' disabled, but we are transmitting.
	}
	
	public static void setPowered(Block block, boolean powered) {

		if (block.getType() != Material.SIGN_POST
				&& block.getType() != Material.REDSTONE_TORCH_ON
				&& block.getType() != Material.REDSTONE_TORCH_OFF) return;

		if (powered == isPowered(block)) return;
		
		Block tmp = block.getRelative(BlockFace.DOWN);
		if (!tmp.getType().isBlock() || tmp.isLiquid()) return; // not a valid underground?
		
		if (powered) {
			block.setTypeIdAndData(Material.REDSTONE_TORCH_ON.getId(), (byte) 0x5, true);
		} else {
			block.setTypeIdAndData(Material.SIGN_POST.getId(), (byte)0x8, true);
			org.bukkit.block.Sign sign = (org.bukkit.block.Sign) block.getState();
			sign.setLine(0,  "[transmitter]");
			sign.update(true);
		}
	}
}
