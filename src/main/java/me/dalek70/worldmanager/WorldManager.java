package me.dalek70.worldmanager;

import me.dalek70.worldmanager.commands.CleanWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
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

	@Override
	protected void onPluginStart() {
		// start up the settings class for this plugin
		Settings.init(this);

		// try to start the code
		try {
			registerCommands(new CleanWorldManager());
			Common.runTimer(20 * 30, () -> {
				for (World world : worldDeletionTimeout.keySet()) {
					int timeout = worldDeletionTimeout.get(world).getKey();
					long lastVisitTime = worldDeletionTimeout.get(world).getValue();
					if (Util.secondsSinceLastVisit(lastVisitTime) >= timeout) {
						Common.log("Deleting world " + world.getName() + " due to inactivity");
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
}
