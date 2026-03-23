package me.dalek70.worldmanager.setting;

import java.util.List;
import me.dalek70.worldmanager.setting.Settings;

public class GetSetting {

	public static String getSettingString(String key) {
		return Settings.getConfig().getString(key);
	}

	public static List<String> getSettingStringList(String key) {
		return Settings.getConfig().getStringList(key);
	}

	public static int getSettingInt(String key) {
		return Settings.getConfig().getInt(key);
	}

	public static boolean getSettingBoolean(String key) {
		return Settings.getConfig().getBoolean(key);
	}

	public static long getSettingLong(String key) {
		return Settings.getConfig().getLong(key);
	}
}
