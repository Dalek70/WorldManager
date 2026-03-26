# WorldManager

WorldManager is a Minecraft plugin that creates simple private worlds, pastes a schematic into the spawn area, tracks player activity, and deletes inactive worlds after a timeout.

## Features

- Create a world with a void chunk generator
- Paste a schematic into the new world spawn
- Teleport one player or all players to a managed world
- List managed worlds and their remaining deletion time
- Set per-world inactivity timeouts
- Automatically delete worlds after they have been idle too long

## Requirements

- Java 21
- A Bukkit-compatible server such as Paper or Spigot
- Maven for building from source
- Foundation library from MineAcademy, handled by the Maven build
- FastAsyncWorldEdit installed on the server

## Build

To build the plugin jar with Maven:

```bash
mvn clean install
```

The shaded jar will be created in the `target` folder.

There is also an `build.xml` file for Ant-based copy-to-server workflows, but Maven is the main build path defined by `pom.xml`.

## Install

1. Build the jar.
2. Copy the generated jar into your server's `plugins` folder.
3. Make sure FastAsyncWorldEdit is installed and enabled.
4. Start or restart the server.
5. Confirm that `settings.yml` and the localization files are generated in the plugin folder.

## Configuration

Main configuration is in `src/main/resources/settings.yml` and is copied to the plugin folder on first run.
And the plugin config would be in plugins/WorldManager/settings.yml

### Settings

- `Command_Aliases` - aliases for the command group
- `Locale` - locale file to use
- `Prefix` - chat prefix format
- `world-schematic-file` - default schematic file name inside the FAWE schematics folder
- `world-deletion-timeout-seconds` - default idle time before a world is deleted

## Commands

The main command group is `worldmanager` with aliases defined in `settings.yml`.

### Subcommands

- `create <world-name> [schematic-file]`
- `delete <world-name>`
- `teleport <world-name>`
- `teleport <world-name> -all`
- `settimeout <world-name> <timeout-in-seconds>`
- `list`

## Permissions

The active command handlers explicitly check these permission nodes:

- `worldmanager.command.list`
- `worldmanager.command.teleport.self`
- `worldmanager.command.teleport.all`
- `worldmanager.command.teleport.world.<worldname>`
- `worldmanager.command.create`
- `worldmanager.command.delete`
- `worldmanager.command.settimeout`

Replace `<worldname>` with the lowercase world name you want to allow.

## Behavior

- Newly created worlds are tracked in memory and in the active timeout map.
- Player teleports and command usage in tracked worlds reset the inactivity timer.
- Worlds are deleted when the timeout expires and the folder is removed from disk.

## Notes

- The plugin expects FAWE's `schematics` folder to exist under the FAWE plugin data folder.
- If a schematic cannot be found, world creation will fail and the world folder may be cleaned up.

## License

See `LICENSE` for license information.

