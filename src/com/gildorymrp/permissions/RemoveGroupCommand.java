package com.gildorymrp.permissions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RemoveGroupCommand implements CommandExecutor {

private GildorymPermissions plugin;
	
	public RemoveGroupCommand(GildorymPermissions plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("gildorym.permissions.command.removegroup")) {
			if (args.length >= 2) {
				if (plugin.getServer().getPlayer(args[0]) != null) {
					if (plugin.getConfig().getConfigurationSection("groups").contains(args[1])) {
						List<String> groups = plugin.getConfig().getStringList("users/" + plugin.getServer().getPlayer(args[0]).getName());
						if (groups.contains(args[1])) {
							groups.remove(args[1]);
							plugin.getConfig().set("users/" + plugin.getServer().getPlayer(args[0]).getName(), groups);
							plugin.saveConfig();
							plugin.assignPermissions(plugin.getServer().getPlayer(args[0]));
							sender.sendMessage(GildorymPermissions.PREFIX + ChatColor.GREEN + plugin.getServer().getPlayer(args[0]).getName() + " was set to the group " + args[1]);
						} else {
							sender.sendMessage(GildorymPermissions.PREFIX + ChatColor.RED + "That player does not have that group assigned.");
						}
					} else {
						sender.sendMessage(GildorymPermissions.PREFIX + ChatColor.RED + "That group does not exist.");
					}
				} else {
					sender.sendMessage(GildorymPermissions.PREFIX + ChatColor.RED + "That player is not online.");
				}
			} else {
				sender.sendMessage(GildorymPermissions.PREFIX + ChatColor.RED + "Usage: /addgroup [player] [group]");
			}
		} else {
			sender.sendMessage(GildorymPermissions.PREFIX + ChatColor.RED + "You do not have permission.");
		}
		return true;
	}

}
