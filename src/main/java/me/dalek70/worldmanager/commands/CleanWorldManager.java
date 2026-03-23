package me.dalek70.worldmanager.commands;

import me.dalek70.worldmanager.commands.worldmanager.*;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommandGroup;

@AutoRegister
public final class CleanWorldManager extends SimpleCommandGroup {
	public CleanWorldManager() {
		super("worldmanager");
	}

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new HandleCreate());
		registerSubcommand(new HandleDelete());
		registerSubcommand(new HandleSetTimeout());
		registerSubcommand(new HandleTeleport());
		registerSubcommand(new HandleList());
	}
}
