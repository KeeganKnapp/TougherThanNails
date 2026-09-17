package dev.keeganknapp.tougherthannails;

import java.util.Set;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * Adds a chance for {@link ModItems#FUR_HIDE} to drop from furry / woolly mobs, without
 * replacing their vanilla loot. Uses Fabric's loot table modify event so it stacks with
 * any datapack that also touches these tables.
 */
public final class ModLoot {

}
