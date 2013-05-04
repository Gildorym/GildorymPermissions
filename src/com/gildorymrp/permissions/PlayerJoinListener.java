package com.gildorymrp.permissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	
	private GildorymPermissions plugin;
	
	public PlayerJoinListener(GildorymPermissions plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.assignPermissions(event.getPlayer());
	}

}
