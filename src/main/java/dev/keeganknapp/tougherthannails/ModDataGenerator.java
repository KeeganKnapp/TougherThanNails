package dev.keeganknapp.tougherthannails;

import dev.keeganknapp.tougherthannails.dataproviders.ModEnglishLangProvider;
import dev.keeganknapp.tougherthannails.dataproviders.ModItemTagProvider;
import dev.keeganknapp.tougherthannails.dataproviders.ModLootProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
            FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

            pack.addProvider(ModEnglishLangProvider::new);
            pack.addProvider(ModItemTagProvider::new);
            pack.addProvider((output, registries) -> 
                new ModLootProvider(output, registries)
            );
	}

}
