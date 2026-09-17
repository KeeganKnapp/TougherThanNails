package dev.keeganknapp.tougherthannails.dataproviders;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import dev.keeganknapp.tougherthannails.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.tags.BiomeTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;


public class ModLootProvider extends FabricEntityLootSubProvider {

    public ModLootProvider(FabricPackOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    private static final Set<EntityType<? extends Entity>> FUR_HIDE_DROPPERS = Set.of(
        EntityType.POLAR_BEAR,
        EntityType.WOLF,
        EntityType.FOX,
        EntityType.LLAMA,
        EntityType.TRADER_LLAMA,
        EntityType.GOAT
    );

    private static final Set<EntityType<? extends Entity>> FUR_HIDE_WEARERS = Set.of(
        EntityType.ZOMBIE,
        EntityType.STRAY,
        EntityType.SKELETON
    );

    @Override
    public void generate() {
        for(var entityType : FUR_HIDE_DROPPERS) {
            this.add(entityType, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(LootItemRandomChanceCondition.randomChance(0.5F))
                    .add(LootItem.lootTableItem(ModItems.FUR_HIDE)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))));
        }

/*
        var biomeRegistry = this.registries.lookupOrThrow(Registries.BIOME);

        HolderSet<Biome> coldBiomes = biomeRegistry.getOrThrow(ConventionalBiomeTags.IS_COLD);

        for(var entityType : FUR_HIDE_WEARERS) {
            this.add(entityType, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(LootItemRandomChanceCondition.randomChance(0.5F))
                    .when(LocationCheck.checkLocation(
                        LocationPredicate.Builder.location().setBiomes(coldBiomes)
                    )
                    .add(LootItem.lootTableItem(Items.AIR)
                    .apply(SetComponentsFunction.builder()
                        .setEquipment(EquipmentSlot.CHEST, ModItems.FUR_HIDE_CHESTPLATE)
                    ),
                    .apply(SetComponentsFunction.builder()
                        .setEquipment(EquipmentSlot.FEET, ModItems.FUR_HIDE_BOOTS)
                    )
        }
    */
    }

}
