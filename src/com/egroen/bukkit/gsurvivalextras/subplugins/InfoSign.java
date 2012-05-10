package com.egroen.bukkit.gsurvivalextras.subplugins;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.egroen.bukkit.gsurvivalextras.MasterModule;
import com.egroen.bukkit.gsurvivalextras.SubModule;
import com.egroen.bukkit.gsurvivalextras.Tools;
import com.egroen.bukkit.gsurvivalextras.command.CommandListener;
import com.egroen.bukkit.gsurvivalextras.command.CommandManager;
import com.egroen.bukkit.gsurvivalextras.configuration.Config;
import com.egroen.bukkit.gsurvivalextras.configuration.ConfigManager;

public class InfoSign extends SubModule implements CommandListener, Listener {

	public InfoSign(MasterModule plugin) {
		super(plugin);
	}
	
	private Config config;
	private HashMap<String, Block> _editBlocks = new HashMap<String, Block>();

	@Override
	public void start() {
		CommandManager.getInstance().addCommand("infosign", new String[] {
				"/infosign print [from [to]]",
				"/infosign add[:index] <text> - Adds a new line (on index)",
				"/infosign set <index> <text> - Changes a specified line",
				"/infosign remove <index> - Removes a specified line",
				"/infosign clear - Removes sign from infosign/clear list",
				"/infosign save - Saves to disk",
				"/infosign reload - Reloads from disk"
		}, this);
		plugin.registerListener(this);
		config = ConfigManager.getInstance().loadConfig("InfoSign.yml");
		config.save();
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean onCommand(Player player, String command, String[] args) {
		if (!command.equals("infosign")) return false;
		if (args.length == 0) return false;
		
		// Check his sign.
		if (!_editBlocks.containsKey(player.getName())) { player.sendMessage(ChatColor.RED+"You do now have a sign selected!"); return true; }
		
		// save
		if (args[0].equalsIgnoreCase("save")) {
			if (config.save())
				player.sendMessage(ChatColor.GREEN+"Success!");
			else
				player.sendMessage(ChatColor.RED+"Failed!");
			return true;
		}
		
		// reload
		if (args[0].equalsIgnoreCase("reload")) {
			config.reload();
			player.sendMessage(ChatColor.GREEN+"Done!");
			return true;
		}
		
		String signConfPath = signConfPath(_editBlocks.get(player.getName()));
		
		// print [from [to]]
		if (args[0].equalsIgnoreCase("print")) {
			List<String> msgs = config.getStringList(signConfPath);

			int begin=0, end=msgs.size()-1;						// Default start/end
			if (args.length >= 2) {								// Check overruling start
				if (!args[1].matches("[0-9]+")) return false;	// is +number?
				begin = Integer.parseInt(args[1]);				// Parse it, can't be sub-zero
			}
			if (args.length >= 3) {								// Check overruling end
				if (!args[2].matches("[0-9]+")) return false;	// is +number?
				end = Integer.parseInt(args[2]);				// Parse
			}
			
			if (begin > end) { int tmp = begin; begin=end; end=tmp; }	// Begin higher then end? just rotate it..
			if (end > msgs.size()-1) end = msgs.size()-1;		// Check if not exceeding list, set max end.
			
			for (int i=begin; i<=end; i++) {					// Loop wanted messages
				player.sendMessage(i+": "+msgs.get(i));
			}
			return true;
		}
		
		// add[:index] <line ...>
		if (args[0].matches("(?i)^add(:[0-9]+)?$")) {
			if (args.length < 2) return false;					// Should add a line

			List<String> msgs = config.getStringList(signConfPath);
			String line = Tools.glue(args, 1);					// 1 -> Skip add[:index]

			if (args[0].length() > 3) {
				int i = Integer.parseInt(args[0].substring(4));
				if (i > msgs.size()) {
					player.sendMessage(ChatColor.GRAY+"Index does not exist, line added at the end.");
					i = msgs.size();
				}
				msgs.add(i, line);
			} else {
				msgs.add(line);
			}
			
			config.set(signConfPath, msgs);
			player.sendMessage(ChatColor.GREEN+"Line added.");
			return true;
		}
		
		// set <index> <line ...>
		if (args[0].equalsIgnoreCase("set")) {
			if (args.length < 3) return false;					// No index/line
			if (!args[1].matches("[0-9]+")) return false;		// invalid index
			
			int i = Integer.parseInt(args[1]);
			List<String> msgs = config.getStringList(signConfPath);
			if (i > msgs.size()-1) {
				player.sendMessage(ChatColor.RED+"Invalid index given.");
				return true;
			}
			
			msgs.set(i, Tools.glue(args, 2));					// 2 -> skip set AND index
			config.set(signConfPath, msgs);
			player.sendMessage(ChatColor.GREEN+"Line changed.");
			return true;
		}

		// remove <index>
		if (args[0].equalsIgnoreCase("remove")) {
			if (args.length != 2) return false;					// Not just 'remove <index>'
			if (!args[1].matches("[0-9]+")) return false;		// invalid index
			
			int i = Integer.parseInt(args[1]);
			List<String> msgs = config.getStringList(signConfPath);
			if (i > msgs.size()-1) {
				player.sendMessage(ChatColor.RED+"Invalid index given.");
				return true;
			}
			
			msgs.remove(i);
			config.set(signConfPath, msgs);
			player.sendMessage(ChatColor.GREEN+"Line removed.");
			return true;
		}
		
		// clear
		if (args[0].equalsIgnoreCase("clear")) {
			if (args.length != 1) return false;

			config.set(signConfPath, null);
			player.sendMessage(ChatColor.GREEN+"Sign removed from InfoSign.");
			return true;
		}


		
		return false;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;				// Not a right-click
		if (event.getClickedBlock().getType() != Material.SIGN
				&& event.getClickedBlock().getType() != Material.SIGN_POST) return;	// Not a sign
		
		if (event.getItem() == null) return;		// Nothing in hand

		// Handle the reading.
		if (event.getItem().getType() == Material.getMaterial(config.getString("settings.readTool"))) {
			List<String> messages = config.getStringList(signConfPath(event.getClickedBlock()));
			if (messages == null || messages.isEmpty()) return;	// Nothing found, so not InfoSign sign, or just no lines..
			new messageSender(event.getPlayer(), messages, config.getLong("settings.delay")).start();
			return;
		}
		
		// Handle the writing
		if (event.getItem().getType() == Material.getMaterial(config.getString("settings.editTool"))) {
			if (!event.getPlayer().hasPermission("InfoSign.edit")) return;			// No permissions to edit
			
			_editBlocks.put(event.getPlayer().getName(), event.getClickedBlock());
			event.getPlayer().sendMessage(ChatColor.GREEN+"You can now edit this sign!");
			event.getPlayer().sendMessage(ChatColor.GOLD+"Do not forget '/infosign save' to make it permanent!");
			return;
		}
		
	}
	
	private static String signConfPath(Block b) {
		return "data."+b.getWorld().getUID()+"."+Tools.vector2Hash(b.getLocation().toVector());
	}
	
	class messageSender implements Runnable {
		private Player p;
		private List<String> messages;
		private long delay;
		public messageSender(Player p, List<String> messages, long delay) {
			this.p = p;
			this.messages = Collections.unmodifiableList(messages);
			this.delay = delay;
		}
		
		public void start() {
			new Thread(this).start();
		}
		
		
		public void run() {
			for (String line : messages) {
				p.sendMessage(line);
				try { Thread.sleep(line.length()*delay); } catch (InterruptedException ex) { }
			}
		}
		
	}

}
