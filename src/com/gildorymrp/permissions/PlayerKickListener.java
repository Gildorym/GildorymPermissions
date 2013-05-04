package com.gildorymrp.permissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerKickListener implements Listener {
	
	private GildorymPermissions plugin;
	
	public PlayerKickListener(GildorymPermissions plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		plugin.unassignPermissions(event.getPlayer());
	}

}
