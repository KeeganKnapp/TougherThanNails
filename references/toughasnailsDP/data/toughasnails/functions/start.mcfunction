gamerule sendCommandFeedback false

scoreboard objectives add fillCanteen minecraft.used:minecraft.glass_bottle
scoreboard objectives add air air
scoreboard objectives add canteenDur dummy
scoreboard objectives add raining dummy
scoreboard objectives add slot dummy
scoreboard objectives add TANdeaths deathCount
scoreboard objectives add temp dummy
scoreboard objectives add tempscore dummy
scoreboard objectives add tick dummy
scoreboard objectives add lavaNear dummy
scoreboard objectives add furnaceNear dummy
scoreboard objectives add soulFireNear dummy
scoreboard objectives add campfireNear dummy
scoreboard objectives add coldResistTick dummy
scoreboard objectives add heatResistTick dummy
scoreboard objectives add magmaNear dummy
scoreboard objectives add torchNear dummy
scoreboard objectives add soulTorchNear dummy
scoreboard objectives add blockNear dummy
scoreboard objectives add brewingASTick dummy
scoreboard objectives add fireNear dummy
scoreboard objectives add ylevel dummy
scoreboard objectives add thirstSat dummy
scoreboard objectives add thirst dummy
scoreboard objectives add thirstReset dummy
scoreboard objectives add running minecraft.custom:minecraft.sprint_one_cm
scoreboard objectives add totalTempTick dummy
scoreboard objectives add tempDifference dummy
scoreboard objectives add hypoTick dummy
scoreboard objectives add hyperTick dummy
scoreboard objectives add drinkBottle minecraft.used:minecraft.potion
scoreboard objectives add health health
scoreboard objectives add underwater dummy
scoreboard objectives add thirstTick dummy
scoreboard objectives add playerBiome dummy
scoreboard objectives add useCarrotStick minecraft.used:minecraft.carrot_on_a_stick
scoreboard objectives add TTTResetTime dummy
scoreboard objectives add soulCampfireNear dummy
scoreboard objectives add drinkMilk minecraft.used:minecraft.milk_bucket
scoreboard objectives add config dummy
scoreboard objectives add clearedBucket dummy

scoreboard players set waterBucketDrink config 1
scoreboard players set hotcoldoverlay config 1


scoreboard objectives add operations dummy
scoreboard players set 15 operations 15
scoreboard players set 5 operations 5
scoreboard players set 10 operations 10
scoreboard players set 7 operations 7
scoreboard players set 8 operations 8

summon minecraft:area_effect_cloud ~ ~ ~ {Tags:["initialized"],NoGravity:1b,Duration:2147483647}

execute at @a run setblock ~ ~ ~ water
execute at @a run setblock ~ ~ ~ air
tellraw @a ["",{"text":"                                                                   Tough as Nails datapack successfully installed","bold":true,"color":"light_purple"}]
tellraw @a ["",{"text":"        \uE830","color":"white"},{"text":"click to check out the youtube video","bold":true,"color":"red","clickEvent":{"action":"open_url","value":"https://youtu.be/mPyw-shxw70"}}]
execute as @a run function toughasnails:config

gamerule sendCommandFeedback true