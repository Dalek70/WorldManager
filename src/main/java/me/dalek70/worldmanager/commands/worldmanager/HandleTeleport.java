package me.dalek70.worldmanager.commands.worldmanager;

import me.dalek70.worldmanager.Text;
import me.dalek70.worldmanager.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleSubCommand;

import static me.dalek70.worldmanager.Util.sendCustomMessageColor;

public class HandleTeleport extends SimpleSubCommand {
	public HandleTeleport() {
		super("teleport");
	}

	@Override
	public void onCommand() {
		checkConsole();
		Player sender = getPlayer();

		if(args.length == 1) {
			if(sender.hasPermission("worldmanager.command.teleport.self")) {
				World world = Bukkit.getWorld(args[0]);
				if(world != null) {
					if(sender.hasPermission("worldmanager.command.teleport.world." + args[0].toLowerCase())) {
						Location location = world.getSpawnLocation();
						sender.teleport(location);
						sendCustomMessageColor("You have been teleported to " + Text.aqua + args[0] + Text.orange + ".", sender, Text.orange);
						WorldManager.resetWorldDeletionTimeout(world);
					} else {
						sendCustomMessageColor("You do not have permission to teleport to " + Text.aqua + args[0] + Text.orange + ".", sender, Text.orange);
					}
				} else {
					sendCustomMessageColor("The world " + Text.aqua + args[0] + Text.orange + " does not exist.", sender, Text.orange);
				}
			}
		} else if(args.length == 2) {
			if(args[1].equals("-all")) {
				if(sender.hasPermission("worldmanager.command.teleport.all")) {
					World world = Bukkit.getWorld(args[0]);
					if(world != null) {
						Location location = world.getSpawnLocation();
						for(Player playerToTeleport : Bukkit.getOnlinePlayers()) {
							playerToTeleport.teleport(location);
							sendCustomMessageColor("You have been teleported to " + Text.aqua + args[0] + Text.orange + " by " + Text.aqua + sender.getName() + Text.orange + ".", playerToTeleport, Text.orange);
						}
						WorldManager.resetWorldDeletionTimeout(world);
					} else {
						sendCustomMessageColor("The world " + Text.aqua + args[0] + Text.orange + " does not exist.", sender, Text.orange);
					}
				}
			}
		}
	}
}
