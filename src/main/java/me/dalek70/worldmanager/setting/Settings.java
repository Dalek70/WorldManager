package me.dalek70.worldmanager.setting;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Settings {

	private static FileConfiguration config;
	private static File configFile;

	public static void init(JavaPlugin plugin) {
		configFile = new File(plugin.getDataFolder(), "settings.yml");
		if (!configFile.exists()) {
			plugin.saveResource("settings.yml", false);
		}
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public static FileConfiguration getConfig() {
		return config;
	}

	public static void save(JavaPlugin plugin) {
		try {
			config.save(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}