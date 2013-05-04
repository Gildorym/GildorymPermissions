package com.gildorymrp.permissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
	
	private GildorymPermissions plugin;
	
	public PlayerQuitListener(GildorymPermissions plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.unassignPermissions(event.getPlayer());
	}

}
