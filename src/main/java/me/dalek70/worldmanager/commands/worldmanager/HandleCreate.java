package me.dalek70.worldmanager.commands.worldmanager;

import me.dalek70.worldmanager.*;
import me.dalek70.worldmanager.Util;
import me.dalek70.worldmanager.setting.GetSetting;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.Tuple;
import me.dalek70.worldmanager.Text;

import java.io.File;

import static me.dalek70.worldmanager.WorldManager.faweFolder;
import static me.dalek70.worldmanager.Util.*;

public class HandleCreate extends SimpleSubCommand {
	public HandleCreate() {
		super("create");
		setUsage("<world-name> [schematic-file]");
		setPermission("worldmanager.command.create");
	}

	@Override
	public void onCommand() {
		checkConsole();
		Player player = getPlayer();

		if(args.length == 1) {
			// get world name from args
			String worldName = args[0];

			// check if world already exists
			World world = Bukkit.getWorld(worldName);

			// create the world only if it doesn't already exist
			if(world == null) {
				// create the world
				WorldCreator worldCreator = new WorldCreator(worldName);
				worldCreator.generator(new VoidChunkGenerator());
				try {
					long num = System.currentTimeMillis();
					Bukkit.createWorld(worldCreator);
					long num2 = System.currentTimeMillis();
					WorldManager.createdWorlds.add(worldName);
					System.out.println("Creating world took " + (num2 - num));
				} catch (Exception e) {
					sendCustomMessageColor("Error creating world: " + e.getMessage(), player, Text.red);
					return;
				}

				// get schematic file
				File schemFile = new File(faweFolder, GetSetting.getSettingString("world-schematic-file"));

				// update the world object after creation
				world = Bukkit.getWorld(worldName);

				// do a check to see if the user provided a custom schematic file name
				if(args.length == 2) {
					// paste in the custom schematic
					schemFile = new File(faweFolder, args[1]);

					try {
						Util.pasteSchematicAsync(schemFile, world.getSpawnLocation());
						WorldManager.worldDeletionTimeout.put(world, new Tuple<Integer, Long>(GetSetting.getSettingInt("world-deletion-timeout-seconds"), System.currentTimeMillis()));
						sendCustomMessageColor("World " + worldName + " created with no errors! You can now teleport to it via /worldmanager teleport " + worldName + ".", player, Text.green);
					} catch (Exception e) {
						sendCustomMessageColor("Error pasting schematic: " + e.getMessage() + ". Maybe schematic file " + schemFile.getName() + " does not exist? Check the console for more details.", player, Text.red);
						removeWorld(world);
						e.printStackTrace();
					}
				} else {
					// paste in the schematic set in the config
					try {
						Util.pasteSchematicAsync(schemFile, world.getSpawnLocation());
						WorldManager.worldDeletionTimeout.put(world, new Tuple<Integer, Long>(GetSetting.getSettingInt("world-deletion-timeout-seconds"), System.currentTimeMillis()));
						sendCustomMessageColor("World " + worldName + " created with no errors! You can now teleport to it via /worldmanager teleport " + worldName + ".", player, Text.green);
					} catch (Exception e) {
						sendCustomMessageColor("Error pasting schematic" + e.getMessage() + ". Maybe the set schematic file does not exist? Check the console for more details.", player, Text.red);
						removeWorld(world);
						e.printStackTrace();
					}
				}
			} else {
				// send error message
				sendCustomMessage(Text.red + "The world " + Text.orange +  worldName + Text.red +" already exists. Please choose a different name.", player);
			}
		} else {
			returnUsage();
		}
	}
}
