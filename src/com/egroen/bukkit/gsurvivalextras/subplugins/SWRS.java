package com.egroen.bukkit.gsurvivalextras.subplugins;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.egroen.bukkit.gsurvivalextras.MasterModule;
import com.egroen.bukkit.gsurvivalextras.SubModule;
import com.egroen.bukkit.gsurvivalextras.subplugins.swrs.*;
/**
 * SWRS - Simple Wireless RedStone
 * 
 * This makes it possible to use wireless redstone.
 * Gives a real basic support, nothing fancy.
 * 
 * Currently supports receiver(redstone) and sensor(player movement above sign)
 * They will make a defined transmitter turn on.
 * 
 * Only works in the same world since it stores its data on the receiver/sensor sign (coords only)
 * @author egroen
 */
public class SWRS extends SubModule implements Listener {
	
	public SWRS(MasterModule plugin) {
		super(plugin);
	}

	@Override
	public void start() {
		plugin.registerListener(this);
		plugin.registerListener(new Receiver());
		plugin.registerListener(new Sensor());
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	
	private HashMap<String, Block> _transmitters = new HashMap<String, Block>();
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (event.isCancelled()) return;
		
		Player p = event.getPlayer();
		if (!p.hasPermission("gsurvivalextras.swrs")) {
			boolean test=false;
			if (event.getLine(0).equals("[receiver]")) test=true;
			else if (event.getLine(0).equals("[transmitter]")) test=true;
			else if (event.getLine(0).equals("[sensor]")) test=true;
			if (test) {
				event.setCancelled(true);
				p.sendMessage("You are not allowed to place redstone signs.");
			}
			return;
		}
		
		if (event.getLine(0).equals("[transmitter]")) {
			_transmitters.put(p.getName(), event.getBlock());
			p.sendMessage("Transmitter placed, you can now place a source.");
			return;
		}
		if (event.getLine(0).equals("[receiver]")
				|| event.getLine(0).equals("[sensor]")) {
			if (!_transmitters.containsKey(p.getName())) {
				p.sendMessage(ChatColor.RED+"You need to place a transmitter first.");
				event.setCancelled(true);
				return;
			}
			Block t = _transmitters.remove(p.getName());
			event.setLine(1, ""+t.getLocation().getBlockX());
			event.setLine(2, ""+t.getLocation().getBlockY());
			event.setLine(3, ""+t.getLocation().getBlockZ());
			p.sendMessage(ChatColor.GREEN+"Source has been created.");
		}
	}

}
