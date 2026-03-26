package me.dalek70.worldmanager.commands.worldmanager;

import me.dalek70.worldmanager.Text;
import me.dalek70.worldmanager.Util;
import me.dalek70.worldmanager.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleSubCommand;

import static me.dalek70.worldmanager.Util.sendCustomMessageColor;

public class HandleDelete extends SimpleSubCommand {
	public HandleDelete() {
		super("delete");
		setPermission("worldmanager.command.delete");
	}

	@Override
	public void onCommand() {
		checkConsole();
		Player player = getPlayer();

		if(args.length == 1) {
			String worldName = args[0];
			World world = Bukkit.getWorld(worldName);
			if(world == null) {
				sendCustomMessageColor("The world " + Text.aqua + worldName + Text.orange + " does not exist and thus can not be deleted.", player, Text.orange);
				return;
			}
			Bukkit.unloadWorld(world, false);
			if(world != null) {
				Util.removePlayersFromWorld(world);
				boolean success = Util.removeWorld(world);
				if(success) {
					WorldManager.createdWorlds.remove(worldName);
					sendCustomMessageColor("The world " + Text.aqua + worldName + Text.orange + " has been deleted successfully.", player, Text.orange);
				} else {
					sendCustomMessageColor("An error occurred while trying to delete the world " + Text.aqua + worldName + Text.red + ".", player, Text.red);
				}
			} else {
				sendCustomMessageColor("The world " + Text.aqua + worldName + Text.orange + " does not exist and thus can not be deleted.", player, Text.orange);
			}
		}
	}
}
