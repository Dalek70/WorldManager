package me.dalek70.worldmanager.events;

import me.dalek70.worldmanager.WorldManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class OnPlayerTeleport implements Listener {
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if(WorldManager.createdWorlds.contains(event.getTo().getWorld().getName())) {
			WorldManager.resetWorldDeletionTimeout(event.getTo().getWorld());
		}
	}
}
