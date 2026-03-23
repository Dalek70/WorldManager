package me.dalek70.worldmanager;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class VoidChunkGenerator extends ChunkGenerator {
	@Override
	public ChunkData generateChunkData(
			World world,
			Random random,
			int chunkX,
			int chunkZ,
			BiomeGrid biome
	) {
		ChunkData data = createChunkData(world);

		return data;
	}
}
