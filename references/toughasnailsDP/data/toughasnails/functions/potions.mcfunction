




execute as @a[nbt={SelectedItem:{id:"minecraft:brewing_stand"}}] at @s positioned ^ ^1 ^.5 align xyz run summon minecraft:armor_stand ~.5 ~1 ~.5 {Invisible:1b,Marker:1b,Tags:["brewingTest"],Small:1b,NoGravity:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:brewing_stand"}}] at @s positioned ^ ^1 ^1 align xyz run summon minecraft:armor_stand ~.5 ~1 ~.5 {Invisible:1b,Marker:1b,Tags:["brewingTest"],Small:1b,NoGravity:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:brewing_stand"}}] at @s positioned ^ ^1 ^1.5 align xyz run summon minecraft:armor_stand ~.5 ~1 ~.5 {Invisible:1b,Marker:1b,Tags:["brewingTest"],Small:1b,NoGravity:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:brewing_stand"}}] at @s positioned ^ ^1 ^2 align xyz run summon minecraft:armor_stand ~.5 ~1 ~.5 {Invisible:1b,Marker:1b,Tags:["brewingTest"],Small:1b,NoGravity:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:brewing_stand"}}] at @s positioned ^ ^1 ^3 align xyz run summon minecraft:armor_stand ~.5 ~1 ~.5 {Invisible:1b,Marker:1b,Tags:["brewingTest"],Small:1b,NoGravity:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:brewing_stand"}}] at @s positioned ^ ^1 ^4 align xyz run summon minecraft:armor_stand ~.5 ~1 ~.5 {Invisible:1b,Marker:1b,Tags:["brewingTest"],Small:1b,NoGravity:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:brewing_stand"}}] at @s positioned ^ ^1 ^4.5 align xyz run summon minecraft:armor_stand ~.5 ~1 ~.5 {Invisible:1b,Marker:1b,Tags:["brewingTest"],Small:1b,NoGravity:1b}
execute at @e[tag=brewingTest] if block ~ ~ ~ brewing_stand unless entity @e[distance=..1,tag=brewingStandAS] run summon armor_stand ~ ~ ~ {Marker:1b,Tags:["brewingStandAS"],Invisible:1b,Small:1b,NoGravity:1b}
kill @e[tag=brewingTest]


#heat resistance
	#(3:00)
		execute as @e[tag=brewingStandAS] at @s if block ~ ~ ~ brewing_stand[has_bottle_0=true]{BrewTime:1s,Items:[{Slot:3b,id:"minecraft:magma_cream",tag:{CustomModelData:122}}]} run replaceitem block ~ ~ ~ container.0 minecraft:potion{Count:1b,display:{Name:'{"text":"Potion of Heat Resistance","italic":false}',Lore:['{"text":"Heat Resistance (3:00)","color":"blue","italic":false}']},HideFlags:32,CustomPotionEffects:[{Id:26,Amplifier:1,Duration:5s}],CustomPotionColor:16736294}
		execute as @e[tag=brewingStandAS] at @s if block ~ ~ ~ brewing_stand[has_bottle_1=true]{BrewTime:1s,Items:[{Slot:3b,id:"minecraft:magma_cream",tag:{CustomModelData:122}}]} run replaceitem block ~ ~ ~ container.1 minecraft:potion{Count:1b,display:{Name:'{"text":"Potion of Heat Resistance","italic":false}',Lore:['{"text":"Heat Resistance (3:00)","color":"blue","italic":false}']},HideFlags:32,CustomPotionEffects:[{Id:26,Amplifier:1,Duration:5s}],CustomPotionColor:16736294}
		execute as @e[tag=brewingStandAS] at @s if block ~ ~ ~ brewing_stand[has_bottle_2=true]{BrewTime:1s,Items:[{Slot:3b,id:"minecraft:magma_cream",tag:{CustomModelData:122}}]} run replaceitem block ~ ~ ~ container.2 minecraft:potion{Count:1b,display:{Name:'{"text":"Potion of Heat Resistance","italic":false}',Lore:['{"text":"Heat Resistance (3:00)","color":"blue","italic":false}']},HideFlags:32,CustomPotionEffects:[{Id:26,Amplifier:1,Duration:5s}],CustomPotionColor:16736294}

#cold resistance
	#(3:00)
		execute as @e[tag=brewingStandAS] at @s if block ~ ~ ~ brewing_stand[has_bottle_0=true]{BrewTime:1s,Items:[{Slot:3b,id:"minecraft:glistering_melon_slice",tag:{CustomModelData:122}}]} run replaceitem block ~ ~ ~ container.0 minecraft:potion{Count:1b,display:{Name:'{"text":"Potion of Cold Resistance","italic":false}',Lore:['{"text":"Cold Resistance (3:00)","color":"blue","italic":false}']},HideFlags:32,CustomPotionEffects:[{Id:26,Amplifier:1,Duration:5s}],CustomPotionColor:1026047}
		execute as @e[tag=brewingStandAS] at @s if block ~ ~ ~ brewing_stand[has_bottle_1=true]{BrewTime:1s,Items:[{Slot:3b,id:"minecraft:glistering_melon_slice",tag:{CustomModelData:122}}]} run replaceitem block ~ ~ ~ container.1 minecraft:potion{Count:1b,display:{Name:'{"text":"Potion of Cold Resistance","italic":false}',Lore:['{"text":"Cold Resistance (3:00)","color":"blue","italic":false}']},HideFlags:32,CustomPotionEffects:[{Id:26,Amplifier:1,Duration:5s}],CustomPotionColor:1026047}
		execute as @e[tag=brewingStandAS] at @s if block ~ ~ ~ brewing_stand[has_bottle_2=true]{BrewTime:1s,Items:[{Slot:3b,id:"minecraft:glistering_melon_slice",tag:{CustomModelData:122}}]} run replaceitem block ~ ~ ~ container.2 minecraft:potion{Count:1b,display:{Name:'{"text":"Potion of Cold Resistance","italic":false}',Lore:['{"text":"Cold Resistance (3:00)","color":"blue","italic":false}']},HideFlags:32,CustomPotionEffects:[{Id:26,Amplifier:1,Duration:5s}],CustomPotionColor:1026047}
	#(8:00)
		#execute as @e[tag=brewingStandAS] at @s if block ~ ~ ~ brewing_stand{BrewTime:1s,Items:[{Slot:3b,id:"minecraft:redstone"},{Slot:0b,id:"minecraft:potion",tag:{display:{Name:'{"text":"Potion of Cold Resistance","italic":false}',Lore:['{"text":"Cold Resistance (3:00)","color":"blue","italic":false}']}}}]} run replaceitem block ~ ~ ~ container.0 minecraft:potion{Count:1b,display:{Name:'{"text":"Potion of Cold Resistance","italic":false}',Lore:['{"text":"Cold Resistance (8:00)","color":"blue","italic":false}']},HideFlags:32,CustomPotionEffects:[{Id:26b,Amplifier:255b,Duration:1}],CustomPotionColor:1026047}
		#execute as @e[tag=brewingStandAS] at @s if block ~ ~ ~ brewing_stand{Items:[{Slot:3b,id:"minecraft:redstone"},{Slot:0b,id:"minecraft:potion"}]} run scoreboard players remove @s tick 1
		#execute as @e[tag=brewingStandAS] at @s if block ~ ~ ~ brewing_stand{BrewTime:0,Items:[{Slot:3b,id:"minecraft:redstone"},{Slot:0b,id:"minecraft:potion"}]} run scoreboard players set @s tick 360
		#execute as @e[tag=brewingStandAS] at @s store result block ~ ~ ~ BrewTime int 1 run scoreboard players get @s tick
		#execute as @e[tag=brewingStandAS,scores={tick=..0}] run scoreboard players set @s tick 360
		
		
#cold resistance II
execute as @e[tag=brewingStandAS] at @s if block ~ ~ ~ brewing_stand{Items:[{Slot:3b,id:"minecraft:redstone"},{Slot:0b},{},{}]}

#particles
	execute at @a[tag=heatResist] if score tempTick tick matches 4..4 run summon minecraft:area_effect_cloud ~ ~ ~ {Radius:.1f,Duration:1,Color:16736294}
	execute at @a[tag=heatResist] if score tempTick tick matches 4..4 run summon minecraft:area_effect_cloud ~ ~1 ~ {Radius:.1f,Duration:1,Color:16736294}

	execute at @a[tag=coldResist] if score tempTick tick matches 4..4 run summon minecraft:area_effect_cloud ~ ~ ~ {Radius:.1f,Duration:1,Color:1026047}
	execute at @a[tag=coldResist] if score tempTick tick matches 4..4 run summon minecraft:area_effect_cloud ~ ~1 ~ {Radius:.1f,Duration:1,Color:1026047}
#cold pot
tag @a[nbt={SelectedItem:{id:"minecraft:potion",Count:1b,tag:{display:{Name:'{"text":"Potion of Cold Resistance","italic":false}',Lore:['{"text":"Cold Resistance (3:00)","color":"blue","italic":false}']}}}}] add holdingColdPotI
tag @a[nbt={SelectedItem:{id:"minecraft:potion",Count:1b,tag:{display:{Name:'{"text":"Potion of Cold Resistance","italic":false}',Lore:['{"text":"Cold Resistance (3:00)","color":"blue","italic":false}']}}}}] add holdingColdPotII

tag @a[scores={drinkBottle=1..},tag=holdingColdPotI] add coldResist
tag @a[scores={drinkBottle=1..},tag=holdingColdPotI] add potionI
scoreboard players add @a[tag=coldResist] coldResistTick 1
tag @a[tag=coldResist,tag=potionI,scores={coldResistTick=3600..}] remove coldResist
tag @a[tag=!coldResist,tag=!heatResist,scores={coldResistTick=3600..}] remove potionI
scoreboard players set @a[tag=!coldResist] coldResistTick 0

tag @a[nbt=!{SelectedItem:{id:"minecraft:potion",Count:1b,tag:{display:{Name:'{"text":"Potion of Cold Resistance","italic":false}',Lore:['{"text":"Cold Resistance (3:00)","color":"blue","italic":false}']}}}}] remove holdingColdPotI
tag @a[nbt=!{SelectedItem:{id:"minecraft:potion",Count:1b,tag:{display:{Name:'{"text":"Potion of Cold Resistance","italic":false}',Lore:['{"text":"Cold Resistance (8:00)","color":"blue","italic":false}']}}}}] remove holdingColdPotII

#hot pot
tag @a[nbt={SelectedItem:{id:"minecraft:potion",Count:1b,tag:{display:{Name:'{"text":"Potion of Heat Resistance","italic":false}',Lore:['{"text":"Heat Resistance (3:00)","color":"blue","italic":false}']}}}}] add holdingHeatPotI
tag @a[nbt={SelectedItem:{id:"minecraft:potion",Count:1b,tag:{display:{Name:'{"text":"Potion of Heat Resistance","italic":false}',Lore:['{"text":"Heat Resistance (8:00)","color":"blue","italic":false}']}}}}] add holdingHeatPotII

tag @a[scores={drinkBottle=1..},tag=holdingHeatPotI] add heatResist
tag @a[scores={drinkBottle=1..},tag=holdingHeatPotI] add potionI
tag @a[scores={drinkBottle=1..},tag=holdingHeatPotII] add potionII
scoreboard players add @a[tag=heatResist] heatResistTick 1
tag @a[tag=heatResist,tag=potionI,scores={heatResistTick=3600..}] remove heatResist
tag @a[tag=!coldResist,tag=!heatResist,scores={heatResistTick=3600..}] remove potionI
tag @a[tag=!coldResist,tag=!heatResist,scores={heatResistTick=3600..}] remove potionII
scoreboard players set @a[tag=!heatResist] heatResistTick 0

tag @a[nbt=!{SelectedItem:{id:"minecraft:potion",Count:1b,tag:{display:{Name:'{"text":"Potion of Heat Resistance","italic":false}',Lore:['{"text":"Heat Resistance (3:00)","color":"blue","italic":false}']}}}}] remove holdingHeatPotI
tag @a[nbt=!{SelectedItem:{id:"minecraft:potion",Count:1b,tag:{display:{Name:'{"text":"Potion of Heat Resistance","italic":false}',Lore:['{"text":"Cold Resistance (8:00)","color":"blue","italic":false}']}}}}] remove holdingHeatPotII




