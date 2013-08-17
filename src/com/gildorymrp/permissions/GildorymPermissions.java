package com.gildorymrp.permissions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import com.gildorymrp.api.Gildorym;
import com.gildorymrp.api.plugin.permissions.GildorymPermissionsPlugin;

public class GildorymPermissions extends JavaPlugin implements GildorymPermissionsPlugin {

	public static final String PREFIX = "" + ChatColor.DARK_GREEN + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.GOLD + "GildorymPermissions" + ChatColor.DARK_GREEN + ChatColor.MAGIC + "| " + ChatColor.RESET;

	public Map<String, PermissionAttachment> permissionAttachments = new HashMap<String, PermissionAttachment>();

	public void onEnable() {
		Gildorym.registerPermissionsPlugin(this);
		this.saveDefaultConfig();
		this.getConfig().options().pathSeparator('/');
		if (this.getServer().getOnlinePlayers().length != 0) {
			for (Player player : this.getServer().getOnlinePlayers()) {
				this.assignPermissions(player);
			}
		}
		this.registerListeners(new PlayerJoinListener(this), new PlayerQuitListener(this));
		this.getCommand("setgroup").setExecutor(new SetGroupCommand(this));
		this.getCommand("addgroup").setExecutor(new AddGroupCommand(this));
		this.getCommand("removegroup").setExecutor(new RemoveGroupCommand(this));
		this.getCommand("listgroups").setExecutor(new ListGroupsCommand(this));
	}

	private void registerListeners(Listener... listeners) {
		for (Listener listener : listeners) {
			this.getServer().getPluginManager().registerEvents(listener, this);
		}
	}

	public void onDisable() {
		if (this.getServer().getOnlinePlayers().length != 0) {
			for (Player player : this.getServer().getOnlinePlayers()) {
				this.unassignPermissions(player);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void assignPermissions(Player player) {
		if (permissionAttachments.get(player.getName()) == null) {
			permissionAttachments.put(player.getName(), player.addAttachment(this));
		} else {
			this.unassignPermissions(player);
			permissionAttachments.put(player.getName(), player.addAttachment(this));
		}
		if (this.getConfig().getConfigurationSection("users").contains(player.getName())) {
			for (String group : (Set<String>) this.getConfig().get("users/" + player.getName())) {
				this.assignGroupPermissions(player, group);
			}
		} else {
			this.assignGroupPermissions(player, "default");
		}
	}

	public void unassignPermissions(Player player) {
		player.removeAttachment(this.permissionAttachments.get(player.getName()));
		this.permissionAttachments.remove(player.getName());
	}

	public void assignGroupPermissions(Player player, String group) {
		if (this.getConfig().getString("groups/" + group + "/inheritsfrom") != null) {
			this.assignGroupPermissions(player, this.getConfig().getString("groups/" + group + "/inheritsfrom"));
		}
		for (String node : this.getConfig().getStringList("groups/" + group + "/allow")) {
			if (permissionAttachments.get(player.getName()).getPermissions().containsKey(node)) {
				permissionAttachments.get(player.getName()).unsetPermission(node);
			}
			permissionAttachments.get(player.getName()).setPermission(node, true);
		}
		for (String node : this.getConfig().getStringList("groups/" + group + "/deny")) {
			if (permissionAttachments.get(player.getName()).getPermissions().containsKey(node)) {
				permissionAttachments.get(player.getName()).unsetPermission(node);
			}
			permissionAttachments.get(player.getName()).setPermission(node, false);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getGroups(Player player) {
		return (Set<String>) this.getConfig().get("users/" + player.getName());
	}

	@Override
	public void setGroup(Player player, String groupName) {
		Set<String> groups = new HashSet<String>();
		groups.add(groupName);
		this.getConfig().set("users/" + player.getName(), groups);
		this.saveConfig();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addGroup(Player player, String groupName) {
		Set<String> groups = (Set<String>) this.getConfig().get("users/" + player.getName());
		groups.add(groupName);
		this.getConfig().set("users/" + player.getName(), groups);
		this.saveConfig();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeGroup(Player player, String groupName) {
		Set<String> groups = (Set<String>) this.getConfig().get("users/" + player.getName());
		groups.remove(groupName);
		this.getConfig().set("users/" + player.getName(), groups);
		this.saveConfig();
	}
	
	@Override
	public boolean hasPermission(Player player, String node) {
		return player.hasPermission(node);
	}
	
}