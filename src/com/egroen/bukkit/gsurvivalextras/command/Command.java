package com.egroen.bukkit.gsurvivalextras.command;

import java.util.Arrays;
import java.util.List;

public class Command {
	private String cmd;
	private List<String> usage;
	private CommandListener runner;
	
	public Command(String cmd, List<String> usage, CommandListener runner) {
		this.cmd = cmd;
		this.usage = usage;
		this.runner = runner;
	}
	
	public Command(String cmd, String[] usage, CommandListener runner) {
		this(cmd, (List<String>)Arrays.asList(usage), runner);
	}
	
	public String getCMD() { return cmd; }
	public List<String> getUsage() { return usage; }
	public CommandListener getRunner() { return runner; }
}
