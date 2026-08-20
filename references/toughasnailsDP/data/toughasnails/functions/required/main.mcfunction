#start datapack
execute unless entity @e[tag=initialized,type=area_effect_cloud] run function toughasnails:start

#execute as @a if score waterBucketDrink config matches ..0 run replaceitem entity @s[tag=holdingMilkBucket] weapon.mainhand water_bucket

#ylevel tracker
execute as @a store result score @s ylevel run data get entity @s Pos[1]
	
	execute store result score timeTick tick run time query daytime
#/give @p minecraft:potion{display:{Name:'{"text":"Potion of Cooling","italic":false}',Lore:['{"text":"Cooling I","color":"blue","italic":false}']},HideFlags:32,CustomPotionEffects:[{Id:26b,Amplifier:0b,Duration:1}],CustomPotionColor:1877247} 1

function toughasnails:potions
function toughasnails:time
function toughasnails:hotbar
function toughasnails:altitude
function toughasnails:thirst
execute if score tempTick tick matches 1..1 run function toughasnails:biometemps
function toughasnails:woolarmor
function toughasnails:animations
function toughasnails:tempmath
execute if score tempTick tick matches 1..1 run function toughasnails:environment
function toughasnails:effects
function toughasnails:addons
function toughasnails:visuals
function toughasnails:newitems

execute as @a if score waterBucketDrink config matches ..0 run replaceitem entity @s[tag=holdingMilkBucket] weapon.mainhand water_bucket
execute as @a if score waterBucketDrink config matches ..0 run tag @s remove holdingMilkBucket

execute as @a at @s store success score @s soulTorchNear run clone ~-3 ~-1 ~-3 ~3 ~1 ~3 ~-3 ~-1 ~-3 filtered minecraft:soul_torch force
execute as @a at @s store success score @s torchNear run clone ~-3 ~-1 ~-3 ~3 ~1 ~3 ~-3 ~-1 ~-3 filtered minecraft:torch force
execute as @a at @s store success score @s fireNear run clone ~-3 ~-1 ~-3 ~3 ~1 ~3 ~-3 ~-1 ~-3 filtered minecraft:fire force
execute as @a at @s store success score @s soulCampfireNear run clone ~-3 ~-1 ~-3 ~3 ~1 ~3 ~-3 ~-1 ~-3 filtered minecraft:soul_campfire force
execute as @a at @s store success score @s soulFireNear run clone ~-3 ~-1 ~-3 ~3 ~1 ~3 ~-3 ~-1 ~-3 filtered minecraft:soul_fire force
execute as @a at @s store success score @s campfireNear run clone ~-3 ~-1 ~-3 ~3 ~1 ~3 ~-3 ~-1 ~-3 filtered minecraft:campfire force
#execute as @a at @s store success score @s furnaceNear run clone ~-3 ~-1 ~-3 ~3 ~1 ~3 ~-3 ~-1 ~-3 filtered minecraft:furnace[lit=true] force
execute as @a at @s store success score @s magmaNear run clone ~-3 ~-1 ~-3 ~3 ~1 ~3 ~-3 ~-1 ~-3 filtered minecraft:magma_block force
execute as @a at @s store success score @s lavaNear run clone ~-3 ~-1 ~-3 ~3 ~1 ~3 ~-3 ~-1 ~-3 filtered minecraft:lava force

execute as @a if score @s blockNear matches 15.. run scoreboard players set @s blockNear 15
execute as @a if score tempTick tick matches 9.. run scoreboard players set @s blockNear 0




#title @a[gamemode=creative] actionbar {"text":""}

#in cave
#execute as @a at @s if block ~ ~ ~ cave_air if score tempTick tick matches 1..1 run scoreboard players remove @s tempscore 7




#bedrock drip
execute as @a[nbt={Dimension:-1}] at @s if score tempTick tick matches 1..1 run particle minecraft:dripping_water ~ 123.9 ~ 1 0 1 0 3
execute as @a[nbt={Dimension:-1}] at @s if score tempTick tick matches 1..1 run particle minecraft:dripping_water ~ 124.9 ~ 1 0 1 0 3


#hot/cold overlay title time (prevent blinking)
title @a times 0 2 2


#purified water bucket
#/give cobblestoner minecraft:milk_bucket{CustomModelData:123,display:{Name:"{\"text\":\"Purified Water Bucket\",\"italic\":\"false\"}"}}
#purified water bottle
#/give @p minecraft:potion{display:{Name:'{"text":"Purified Water Bottle","italic":false}'},HideFlags:32,CustomModelData:121,CustomPotionColor:16777215} 1

