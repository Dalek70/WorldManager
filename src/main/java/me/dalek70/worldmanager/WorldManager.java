package me.dalek70.worldmanager;

import me.dalek70.worldmanager.commands.CleanWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.Tuple;
import org.mineacademy.fo.plugin.SimplePlugin;
import me.dalek70.worldmanager.setting.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldManager extends SimplePlugin {
	public static Map<World, Tuple<Integer, Long>> worldDeletionTimeout = new HashMap<>();
	public static File faweFolder = new File(Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit").getDataFolder(), "schematics");
	public static List<String> createdWorlds = new ArrayList<>();

	public static void resetWorldDeletionTimeout(World world) {
		WorldManager.worldDeletionTimeout.replace(world, new Tuple<Integer, Long> (WorldManager.worldDeletionTimeout.get(world).getKey(), System.currentTimeMillis()));
	}

	private void saveCreatedWorlds() {
		getConfig().set("created-worlds", createdWorlds);
		saveConfig();
		Bukkit.savePlayers();
		for(String worldName : createdWorlds) {
			Bukkit.unloadWorld(worldName, true);
		}
		Common.log("Saved " + createdWorlds.size() + " world(s) to config");
	}

	public void loadCreatedWorlds() {
		List<String> worlds = getConfig().getStringList("created-worlds");
		createdWorlds.clear();
		for(String worldName : worlds) {
			WorldCreator worldCreator = new WorldCreator(worldName);
			worldCreator.generator(new VoidChunkGenerator());
			Bukkit.createWorld(worldCreator);
		}
		createdWorlds.addAll(worlds);
		Common.log("Loaded " + worlds.size() + " world(s) from config");
	}

	@Override
	protected void onPluginStart() {
		// start up the settings class for this plugin
		Settings.init(this);

		// add shutdown hook for if the server crashes
		Runtime.getRuntime().addShutdownHook(new Thread(this::saveCreatedWorlds));

		loadCreatedWorlds();

		// try to start the code
		try {
			registerCommands(new CleanWorldManager());
			Common.runTimer(20 * 30, () -> {
				for (World world : worldDeletionTimeout.keySet()) {
					int timeout = worldDeletionTimeout.get(world).getKey();
					long lastVisitTime = worldDeletionTimeout.get(world).getValue();
					if (Util.secondsSinceLastVisit(lastVisitTime) >= timeout) {
						Common.log("Deleting world " + world.getName() + " due to inactivity");
						Util.removePlayersFromWorld(world);
						Util.deleteWorldFolder(world.getWorldFolder());
						worldDeletionTimeout.remove(world);
						createdWorlds.remove(world.getName());
					}
				}
			});
		} catch (Exception e) {
			Common.warning("Failed to register WorldManager command:");
			e.printStackTrace();
			Common.warning("Disabling plugin");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	@Override
	protected void onPluginStop() {
		saveCreatedWorlds();
	}
}
