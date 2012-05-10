package com.egroen.bukkit.gsurvivalextras.subplugins;

import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.egroen.bukkit.gsurvivalextras.MasterModule;
import com.egroen.bukkit.gsurvivalextras.SubModule;

public class NoCreeperWorldDamage extends SubModule implements Listener {
    public NoCreeperWorldDamage(MasterModule plugin) { super(plugin); }

	@EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Creeper)) return;
        event.blockList().clear();
    }

	@Override
	public void start() {
		plugin.registerListener(this);
	}

	@Override
	public void stop() {
	}
}
