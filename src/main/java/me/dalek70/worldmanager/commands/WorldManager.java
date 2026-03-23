package me.dalek70.worldmanager.commands;

import me.dalek70.worldmanager.Text;
import me.dalek70.worldmanager.setting.GetSetting;
import me.dalek70.worldmanager.VoidChunkGenerator;
import me.dalek70.worldmanager.Util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.io.File;
import java.util.List;

public class WorldManager extends SimpleCommand {

	public WorldManager() {
		super("worldmanager");
		setPermission("simplecustomstuff.command.worldmanager");
	}

	public static long lastVisitTime = 0;
	public static final String campWorldName = "camp_world";

	@Override
	protected void onCommand() {
		checkConsole();
		Player player = getPlayer();
		if(args.length == 0) {
			tell("Please specify a subcommand.");
		} else {
			switch (args[0].toLowerCase()) {
				case "create" -> {
					WorldCreator worldCreator = new WorldCreator(campWorldName);
					worldCreator.generator(new VoidChunkGenerator());
					Bukkit.createWorld(worldCreator);
					File faweFolder = new File(Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit").getDataFolder(), "schematics");
					File schemFile = new File(faweFolder, GetSetting.getSettingString("camp-world-schematic-file"));
					World world = Bukkit.getWorld(campWorldName);
					try {
						Util.pasteSchematicAsync(schemFile, world.getSpawnLocation());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					tell("Created the camp world.");
				}

				case "teleport" -> {
					if(args.length == 2) {
						if(args[1].equals("-all")) {
							if(player.hasPermission("simplecustomstuff.command.worldmanager.teleport.single")) {
								World world = Bukkit.getWorld(campWorldName);
								if(world == null) {
									tell("The world " + campWorldName + " does not exist. Please create it first.");
								} else {
									for(Player playerToTeleport : Bukkit.getOnlinePlayers()) {
										playerToTeleport.teleport(world.getSpawnLocation());
										playerToTeleport.sendMessage(Text.green + "You have been summoned to the camp world by " + Text.orange + getPlayer().getName() + Text.green + ".");
									}
									lastVisitTime = System.currentTimeMillis();
									tell("Teleported all online splayers to the camp world.");
								}
							} else {
								tell(Text.red + "You do not have permission to teleport all players to the camp world.");
							}
						} else {
							Player playerToTeleport = Bukkit.getPlayer(args[1]);
							if(playerToTeleport == null) {
								tell("That player does not exist or is not online.");
							}

							if(playerToTeleport.getName().equals(player.getName())) {
								World world = Bukkit.getWorld(campWorldName);
								if(world == null) {
									tell("The camp world does not exist. Please create it first.");
								} else {
									player.teleport(world.getSpawnLocation());
									lastVisitTime = System.currentTimeMillis();
									tell("Teleported you to the camp world.");
								}
							} else {
								if(player.hasPermission("simplecustomstuff.command.worldmanager.teleport.other")) {
									World world = Bukkit.getWorld(campWorldName);
									if(world == null) {
										tell("The camp world does not exist. Please create it first.");
									} else {
										playerToTeleport.teleport(world.getSpawnLocation());
										playerToTeleport.sendMessage(Text.green + "You have been summoned to the camp world by" + Text.orange + getPlayer().getName() + Text.green + ".");
										lastVisitTime = System.currentTimeMillis();
										tell("Teleported " + playerToTeleport.getName() + " to the camp world.");
									}
								} else {
									tell(Text.red + "You do not have permission to teleport other players to the camp world.");
								}
							}
						}
					} else if(args.length == 1) {
						World world = Bukkit.getWorld(campWorldName);
						if(world == null) {
							tell("The camp world does not exist. Please create it first.");
						} else {
							player.teleport(world.getSpawnLocation());
							tell("You have teleported to the camp world.");
						}
					}
				}

				case "delete" -> {
					if(player.hasPermission("simplecustomstuff.command.worldmanager.delete")) {
						if(args.length == 1) {
							World world = Bukkit.getWorld(campWorldName);
							if(world == null) {
								tell("The camp world does not exist and thus cannot be deleted.");
							} else {
								for(Player playerToTeleport : Bukkit.getOnlinePlayers()) {
									if(playerToTeleport.getWorld().getName().equals(campWorldName)) {
										playerToTeleport.teleport(Bukkit.getWorld("world").getSpawnLocation());
										playerToTeleport.sendMessage(Text.green + "This world is being deleted. You have been moved to the main world.");
									}
								}
								Bukkit.unloadWorld(world, false);
								Util.deleteWorldFolder(world.getWorldFolder());
								tell("The camp world has been deleted.");
							}
						} else {
							tell("Please check the usage and try again because you provided an invalid number of arguments.");
						}
					} else {
						tell(Text.red + "You do not have permission to delete the camp world.");
					}
				}

				case "settimeout" -> {
					if(player.hasPermission("simplecustomstuff.command.worldmanager.setting.settimeout")) {
						int timeout;

						try {
							timeout = Integer.parseInt(args[1]);
							tell(Text.green + "Set the camp world timeout to " + Text.orange +  timeout + Text.green + " seconds.");
						} catch (Exception e) {
							tell("Please provide a valid number for the timeout in seconds.");
						}
					} else {
						tell(Text.red + "You do not have permission to set the camp world timeout.");
					}
				}

				case "test" -> {
					for(Player playerToTeleport : Bukkit.getOnlinePlayers()) {
						if(playerToTeleport.getWorld().getName().equals(campWorldName)) {
							playerToTeleport.teleport(Bukkit.getWorld("world").getSpawnLocation());
							playerToTeleport.sendMessage(Text.green + "This world is being deleted. You have been moved to the main world.");
						}
					}
					World world = Bukkit.getWorld(WorldManager.campWorldName);
					Bukkit.unloadWorld(world, false);
					Util.deleteWorldFolder(world.getWorldFolder());
				}
			}
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return completeLastWord("create", "teleport", "delete", "settimeout");
		} else if (args.length == 2 && args[0].equals("teleport")) {
			List<String> completions = completeLastWord("-all");
			for (Player p : Bukkit.getOnlinePlayers()) {
				completions.add(p.getName());
			}
			return completions;
		}
		return NO_COMPLETE;
	}
}
