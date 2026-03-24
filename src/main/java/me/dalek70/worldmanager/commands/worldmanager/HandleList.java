package me.dalek70.worldmanager.commands.worldmanager;

import me.dalek70.worldmanager.Text;
import me.dalek70.worldmanager.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleSubCommand;
import static me.dalek70.worldmanager.Util.sendCustomMessageColor;

public class HandleList extends SimpleSubCommand {
	public HandleList() {
		super("list");
		setPermission("worldmanager.command.list");
	}

	@Override
	public void onCommand() {
		checkConsole();
		Player player = getPlayer();

		if(player.hasPermission("worldmanager.command.list")) {

			sendCustomMessageColor("Created worlds: " , player, Text.yellow);
			for(String worldName : WorldManager.createdWorlds) {
				sendCustomMessageColor(worldName + ":", player, Text.orange);
				sendCustomMessageColor(" Timeout: " + Text.aqua + WorldManager.worldDeletionTimeout.get(Bukkit.getWorld(worldName)).getKey(), player, Text.orange);
				sendCustomMessageColor(" Seconds until deletion: " + Text.aqua + (WorldManager.worldDeletionTimeout.get(Bukkit.getWorld(worldName)).getKey() - ((System.currentTimeMillis() - WorldManager.worldDeletionTimeout.get(Bukkit.getWorld(worldName)).getValue()) / 1000)) , player, Text.orange);
			}
		} else {
			sendCustomMessageColor("You do not have permission to use this command.", player, Text.red);
		}
	}
}
