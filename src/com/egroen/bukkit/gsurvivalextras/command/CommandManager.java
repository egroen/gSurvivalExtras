package com.egroen.bukkit.gsurvivalextras.command;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager implements Listener {
	private static CommandManager _singleton=null;
	//private JavaPlugin plugin=null;
	
	/**
	 * Holds all commands references.
	 */
	private HashMap<String, Command> _commands = new HashMap<String, Command>();
	
	/**
	 * Get singleton
	 * @return CommandManager
	 */
	public static CommandManager getInstance() { return _singleton; }
	/**
	 * Install singleton
	 * @param plugin
	 */
	public static void install(JavaPlugin plugin) { if (_singleton == null) _singleton = new CommandManager(plugin); }
	
	/**
	 * Create CommandManager object
	 * This will register itself to events.
	 * @param plugin The main plugin
	 */
	private CommandManager(JavaPlugin plugin) {
		//this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	/**
	 * Add a new command
	 * @param cmd Command to add
	 * @param usage Usage list
	 * @param runner Class to run it
	 */
	public void addCommand(String cmd, String[] usage, CommandListener runner) {
		Command command = new Command(cmd, usage, runner);
		_commands.put(cmd, command);
	}
	
	/**
	 * Event that runs on command-like input
	 * Naughty way to receive commands.
	 * @param event
	 */
	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String[] parts = event.getMessage().split(" "); 			// Split message to process
		String cmd = parts[0].toLowerCase();			// Retreive command
		
		if (!cmd.startsWith("/")) return;							// If first (the command) does not start with /, stop
		cmd = cmd.substring(1);										// Remove the /
		if (!_commands.containsKey(cmd)) return;					// If command does not exist, stop
		
		String[] args = new String[parts.length-1];					// Create args array
		for (int i=1; i<parts.length; i++) args[i-1] = parts[i];	// Fill it

		Command cmdh = _commands.get(cmd);							// Get the attached command
		if (!cmdh.getRunner().onCommand(event.getPlayer(), cmd, args)) {	// Try to run it
			event.getPlayer().sendMessage(ChatColor.RED+"Syntax of command "+cmd+":");	// print error message
			for (String line : cmdh.getUsage()) {					// Print usage
				event.getPlayer().sendMessage(ChatColor.GRAY+"     "+line);
			}
		}
		event.setCancelled(true);									// Cancel event for further processing, we just did.
	}
	
}
