package com.gildorymrp.permissions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

public class GildorymPermissions extends JavaPlugin {

public static final String PREFIX = "" + ChatColor.DARK_GREEN + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.GOLD + "GildorymPermissions" + ChatColor.DARK_GREEN + ChatColor.MAGIC + "| " + ChatColor.RESET;

public Map<String, PermissionAttachment> permissionAttachments = new HashMap<String, PermissionAttachment>();

public void onEnable() {
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

public void assignPermissions(Player player) {
if (permissionAttachments.get(player.getName()) == null) {
permissionAttachments.put(player.getName(), player.addAttachment(this));
} else {
this.unassignPermissions(player);
permissionAttachments.put(player.getName(), player.addAttachment(this));
}
for (String group : this.getConfig().getStringList("users/" + player.getName())) {
this.assignGroupPermissions(player, group);
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

}