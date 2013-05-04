package com.gildorymrp.permissions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListGroupsCommand implements CommandExecutor {

	private GildorymPermissions plugin;
	
	public ListGroupsCommand(GildorymPermissions plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("gildorym.permissions.command.listgroups")) {
			sender.sendMessage(GildorymPermissions.PREFIX + ChatColor.BLUE + "Group list:");
			for (String group : plugin.getConfig().getConfigurationSection("groups").getKeys(false)) {
				sender.sendMessage(ChatColor.BLUE + group);
			}
		}
		return true;
	}

}
