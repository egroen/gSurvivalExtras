package com.egroen.bukkit.gsurvivalextras.command;

import org.bukkit.entity.Player;

public interface CommandListener {
	public boolean onCommand(Player player, String command, String[] args);
}
