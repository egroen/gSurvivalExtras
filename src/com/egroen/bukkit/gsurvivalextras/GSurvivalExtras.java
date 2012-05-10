package com.egroen.bukkit.gsurvivalextras;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.egroen.bukkit.gsurvivalextras.command.CommandManager;
import com.egroen.bukkit.gsurvivalextras.configuration.ConfigManager;

public class GSurvivalExtras extends JavaPlugin implements MasterModule {
    public static GSurvivalExtras plugin;
    public static Logger logger;
    private List<SubModule> subMods = new ArrayList<SubModule>();

    @Override
    public void onDisable() {
        for (SubModule mod : subMods) {
            mod.stop();
        }
        subMods.clear();
    }
    
    @Override
    public void onEnable() {
        logger = getLogger();
        plugin = this;
        ConfigManager.install(this);
        CommandManager.install(this);

        // Save defaults
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Get all mods to load
        FileConfiguration config = getConfig();
        List<String> mods = config.getStringList("mods");
        for (String mod : mods) {
            try {
                Class<?> c = Class.forName(mod);
                logger.info("    Loading: " + c.getCanonicalName());
                Constructor<?> ct = c.getConstructor(MasterModule.class);
                SubModule submod = (SubModule) ct.newInstance(this);
                submod.start();

            } catch (Exception ex) {
                logger.severe("    Unable to load (unkown): " + mod);
                logger.severe(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void registerListener(Listener mod) {
        plugin.getServer().getPluginManager().registerEvents(mod, plugin);
    }

}
