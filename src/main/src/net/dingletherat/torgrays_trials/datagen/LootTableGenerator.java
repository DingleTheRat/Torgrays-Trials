// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.datagen;

import com.ususstudios.torgrays_datagen.DataGenerationException;
import com.ususstudios.torgrays_datagen.dataclasses.LootTableItem;
import com.ususstudios.torgrays_datagen.dataclasses.SpecialLootTableItem;

public class LootTableGenerator extends com.ususstudios.torgrays_datagen.generators.LootTableGenerator {
	
	@Override
	public void registerAll() throws DataGenerationException {
		registerMultiSelect("chests/amethyst", "Amethyst Chest", new LootTableItem[]{
				// new LootTableItem(Sword_Amethyst.class, 1, 1),
				// new LootTableItem(Shield_Amethyst.class, 1, 1),
				// new LootTableItem(Coins.class, 1, 4)
		});
		
		registerSpecialSelect("entity_drops/dracores", "Dracores", new SpecialLootTableItem[]{
				new SpecialLootTableItem(0.25f, new LootTableItem[]{
						new LootTableItem("", 1, 1),
				}),
				new SpecialLootTableItem(0.40f, new LootTableItem[]{
						new LootTableItem("", 1, 1),
						// new LootTableItem(Coins.class, 0.55f, 1),
						// new LootTableItem(Coins.class, 0.35f, 2),
						// new LootTableItem(Coins.class, 0.10f, 3),
				}),
				new SpecialLootTableItem(0.35f, new LootTableItem[]{
						new LootTableItem("", 1, 1),
						// new LootTableItem(Torgray_Soup.class, 1, 1),
				})
		}, -1, 1);
		
		registerSpecialSelect("dark_chests/normal", "Normal Dark Chest", new SpecialLootTableItem[]{
				new SpecialLootTableItem(0.35f, new LootTableItem[]{
						new LootTableItem("", 1, 1),
						// new LootTableItem(Torgray_Soup.class, 1, 1),
				}),
				new SpecialLootTableItem(0.25f, new LootTableItem[]{
						new LootTableItem("", 1, 1),
						// new LootTableItem(Coins.class, 0.40f, 1),
						// new LootTableItem(Coins.class, 0.30f, 2),
						// new LootTableItem(Coins.class, 0.20f, 3),
						// new LootTableItem(Coins.class, 0.07f, 4),
						// new LootTableItem(Coins.class, 0.03f, 5)
				}),
				new SpecialLootTableItem(0.10f, new LootTableItem[]{
						new LootTableItem("", 1, 1),
						// new LootTableItem(Stick.class, 1, 1),
				})
		}, 1, -1);
		registerSpecialSelect("dark_chests/coiner's", "Coiner's Dark Chest", new SpecialLootTableItem[]{
				new SpecialLootTableItem(0.4f, new LootTableItem[]{
						new LootTableItem("", 1, 1),
						// new LootTableItem(Coins.class, 0.36f, 2),
						// new LootTableItem(Coins.class, 0.30f, 3),
						// new LootTableItem(Coins.class, 0.16f, 4),
						// new LootTableItem(Coins.class, 0.10f, 5),
						// new LootTableItem(Coins.class, 0.04f, 6),
						// new LootTableItem(Coins.class, 0.04f, 7)
				}),
				new SpecialLootTableItem(0.25f, new LootTableItem[]{
						new LootTableItem("", 1, 1),
						// new LootTableItem(Coiner_Soup.class, 1, 1),
				}),
				new SpecialLootTableItem(0.15f, new LootTableItem[]{
						new LootTableItem("", 1, 1),
						// new LootTableItem(Torgray_Soup.class, 1, 1),
				}),
				new SpecialLootTableItem(0.05f, new LootTableItem[]{
						new LootTableItem("", 1, 1),
						// new LootTableItem(World_Map.class, 1, 1),
				}),
		}, 1, 3);
	}
}