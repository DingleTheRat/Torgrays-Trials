package net.dingletherat.torgrays_trials.datagen;

import com.ususstudios.torgrays_datagen.*;

public class DataGenerator extends MainDataGenerator {
	@Override
	public void registerAll() {
		register(new MapGenerator());
		register(new LootTableGenerator());
	}
	
	public static void main(String[] args) throws DataGenerationException {
		new DataGenerator().run();
	}
}