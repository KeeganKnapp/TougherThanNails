	

#holding item
	#soulfire torch
		execute as @a[nbt={SelectedItem:{id:"minecraft:soul_torch"}}] run scoreboard players remove @s tempscore 4
		execute as @a[nbt={Inventory:[{Slot:-106b,id:"minecraft:soul_torch"}]}] run scoreboard players remove @s tempscore 4
	#torch
		execute as @a[nbt={SelectedItem:{id:"minecraft:torch"}}] run scoreboard players add @s tempscore 4
		execute as @a[nbt={Inventory:[{Slot:-106b,id:"minecraft:torch"}]}] run scoreboard players add @s tempscore 4

		execute as @a[nbt={SelectedItem:{id:"minecraft:lava_bucket"}}] run scoreboard players add @s tempscore 15
		execute as @a[nbt={Inventory:[{Slot:-106b,id:"minecraft:lava_bucket"}]}] run scoreboard players add @s tempscore 15

		execute as @a[nbt={SelectedItem:{id:"minecraft:magma_block"}}] run scoreboard players add @s tempscore 10
		execute as @a[nbt={Inventory:[{Slot:-106b,id:"minecraft:magma_block"}]}] run scoreboard players add @s tempscore 10
		
		execute as @a[nbt={SelectedItem:{id:"minecraft:campfire"}}] run scoreboard players add @s tempscore 8
		execute as @a[nbt={Inventory:[{Slot:-106b,id:"minecraft:campfire"}]}] run scoreboard players add @s tempscore 8

		execute as @a[nbt={SelectedItem:{id:"minecraft:soul_campfire"}}] run scoreboard players remove @s tempscore 8
		execute as @a[nbt={Inventory:[{Slot:-106b,id:"minecraft:soul_campfire"}]}] run scoreboard players remove @s tempscore 8
#mobs near
	execute as @a at @s if entity @e[type=blaze,distance=..4] run scoreboard players add @s tempscore 3
	execute as @a at @s if entity @e[type=stray,distance=..4] run scoreboard players remove @s tempscore 3
#nearby blocks
	#lava near
		execute as @a at @s if score @s lavaNear matches 1.. run scoreboard players add @s blockNear 15
	#magma block near
		execute as @a at @s if score @s magmaNear matches 1.. run scoreboard players add @s blockNear 10
	#lit furnace block near
		#execute as @a at @s if score @s furnaceNear matches 1.. run scoreboard players add @s blockNear 8
	#campfire near
		execute as @a at @s if score @s campfireNear matches 1.. run scoreboard players add @s blockNear 8
	#soul fire near
		execute as @a at @s if score @s soulFireNear matches 1.. run scoreboard players remove @s blockNear 8
	#soul campfire near
		execute as @a at @s if score @s soulCampfireNear matches 1.. run scoreboard players remove @s blockNear 8
	#fire near
		execute as @a at @s if score @s fireNear matches 1.. run scoreboard players add @s blockNear 8
	#torch near
		execute as @a at @s if score @s torchNear matches 1.. run scoreboard players add @s blockNear 4
	#soul torch near
		execute as @a at @s if score @s soulTorchNear matches 1.. run scoreboard players remove @s blockNear 4
	execute as @a run scoreboard players operation @s tempscore += @s blockNear
#wet/in water
	execute as @a at @s if block ~ ~ ~ water run scoreboard players remove @s tempscore 7
#raining
	execute as @a at @s store result score @s raining run loot spawn 0 -1 0 loot toughasnails:weather
	execute as @a at @s if block ~ ~2 ~ air if block ~ ~3 ~ air if block ~ ~4 ~ air if block ~ ~5 ~ air if block ~ ~6 ~ air if block ~ ~7 ~ air if block ~ ~8 ~ air if block ~ ~9 ~ air if score @s raining matches 1.. run scoreboard players remove @s tempscore 7

#armor
	execute as @a[nbt={Inventory:[{Slot:100b,id:"minecraft:chainmail_boots",Count:1b}]}] run scoreboard players add @s tempscore 1
	execute as @a[nbt={Inventory:[{Slot:102b,id:"minecraft:chainmail_chestplate",Count:1b}]}] run scoreboard players add @s tempscore 1
	execute as @a[nbt={Inventory:[{Slot:101b,id:"minecraft:chainmail_leggings",Count:1b}]}] run scoreboard players add @s tempscore 1
	execute as @a[nbt={Inventory:[{Slot:103b,id:"minecraft:chainmail_helmet",Count:1b}]}] run scoreboard players add @s tempscore 1
	execute as @a[nbt={Inventory:[{Slot:100b,id:"minecraft:netherite_boots",Count:1b}]}] run scoreboard players remove @s tempscore 1
	execute as @a[nbt={Inventory:[{Slot:102b,id:"minecraft:netherite_chestplate",Count:1b}]}] run scoreboard players remove @s tempscore 1
	execute as @a[nbt={Inventory:[{Slot:101b,id:"minecraft:netherite_leggings",Count:1b}]}] run scoreboard players remove @s tempscore 1
	execute as @a[nbt={Inventory:[{Slot:103b,id:"minecraft:netherite_helmet",Count:1b}]}] run scoreboard players remove @s tempscore 1

#running
	execute as @a[scores={running=1..}] run scoreboard players add @s tempscore 2
	scoreboard players set @a[scores={running=1..}] running 0

#undergound/in cave
	execute as @a at @s unless block ~ ~2 ~ air unless block ~ ~3 ~ air unless block ~ ~4 ~ air if block ~ ~ ~ cave_air run scoreboard players remove @s tempscore 4


