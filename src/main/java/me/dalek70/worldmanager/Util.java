package me.dalek70.worldmanager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;

public class Util {
	public static boolean deleteWorldFolder(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						deleteWorldFolder(file);
					} else {
						file.delete();
					}
				}
			}
		}
		return path.delete();
	}

	public static long secondsSinceLastVisit(long lastVisitTime) {
		if (lastVisitTime == 0) return -1;
		long now = System.currentTimeMillis();
		return (now - lastVisitTime) / 1000;
	}

	public static void pasteSchematicAsync(File schemFile, Location loc) {
		Bukkit.getScheduler().runTaskAsynchronously(WorldManager.getInstance(), () -> {
			try {
				ClipboardFormat format = ClipboardFormats.findByFile(schemFile);
				if (format == null)
					throw new IllegalArgumentException("Unknown schematic format");

				try (ClipboardReader reader = format.getReader(new FileInputStream(schemFile))) {
					Clipboard clipboard = reader.read();
					try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
						Operation operation = new ClipboardHolder(clipboard)
								.createPaste(editSession)
								.to(BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))
								.ignoreAirBlocks(true)
								.build();
						Operations.complete(operation);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}


	public static void sendCustomMessageColor(String message, Player player, String color) {
		player.sendMessage(Text.gray + "[" + Text.aqua + "World Manager" + Text.gray + "] " + color + message);
	}

	public static void sendCustomMessage(String message, Player player) {
		player.sendMessage(Text.gray + "[" + Text.aqua + "World Manager" + Text.gray + "] " + Text.white + message);
	}

	public static boolean removeWorld(World world) {
		boolean returnValue = Bukkit.unloadWorld(world, false);
		deleteWorldFolder(world.getWorldFolder());
		return returnValue;
	}

	public static void removePlayersFromWorld(World world) {
		for(Player player : world.getPlayers()) {
			player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
			sendCustomMessageColor("You have been teleported to spawn because the world you were in is being deleted.", player, Text.orange);
		}
	}
}
