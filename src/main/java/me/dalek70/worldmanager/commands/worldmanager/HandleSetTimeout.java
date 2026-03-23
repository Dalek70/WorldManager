package me.dalek70.worldmanager.commands.worldmanager;

import me.dalek70.worldmanager.Text;
import me.dalek70.worldmanager.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.Tuple;
import static me.dalek70.worldmanager.Util.sendCustomMessageColor;

import java.util.Map;

public class HandleSetTimeout extends SimpleSubCommand {
	public HandleSetTimeout() {
		super("settimeout");
		setUsage("<world-name> <timeout-in-seconds>");
	}

	@Override
	public void onCommand() {
		checkConsole();
		Player player = getPlayer();

		if(args.length == 2) {
			// get the world name in a string
			String worldName = args[0];

			// define stuff
			int timeoutValue = 0;
			World world = null;

			// get the timeout value and put it into a integer and catch the error if it is not a number
			try {
				timeoutValue = Integer.parseInt(args[1]);
			} catch (Exception e) {
				sendCustomMessageColor("The timeout value must be a valid number (in seconds).", player, Text.red);
			}

			// get the world object from the world name and send the player a message if the world does not exist.
			try {
				world = Bukkit.getWorld(worldName);
			} catch (Exception e) {
				sendCustomMessageColor("The world " + Text.orange + worldName + Text.red + " does not exist.", player, Text.red);
			}

			Map<World, Tuple<Integer, Long>> worldDeletionTimeout;
			worldDeletionTimeout = WorldManager.worldDeletionTimeout;
			Tuple<Integer, Long> timeoutTupleOld = worldDeletionTimeout.get(world);
			Tuple<Integer, Long> timeoutTupleNew = new Tuple<Integer, Long>(timeoutValue, timeoutTupleOld.getValue());
			worldDeletionTimeout.replace(world, timeoutTupleNew);
			sendCustomMessageColor("The timeout for the world " + Text.aqua + worldName + Text.orange + " has been set to " + Text.aqua + timeoutValue + Text.orange + " seconds.", player, Text.orange);
		} else {
			returnUsage();
		}
	}
}
