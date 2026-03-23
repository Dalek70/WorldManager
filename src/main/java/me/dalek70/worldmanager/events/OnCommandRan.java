package me.dalek70.worldmanager.events;

import me.dalek70.worldmanager.WorldManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public class OnCommandRan implements Listener {
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if(WorldManager.createdWorlds.contains(event.getPlayer().getWorld().getName())) {
			WorldManager.resetWorldDeletionTimeout(event.getPlayer().getWorld());
		}
	}
}
