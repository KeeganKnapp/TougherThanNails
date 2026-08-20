
scoreboard players add @a[gamemode=survival] thirstTick 1
scoreboard players set @a[scores={thirst=21..}] thirst 20
scoreboard players set @a[scores={thirst=..-1}] thirst 0
execute as @a[gamemode=survival,scores={thirst=..0}] run tag @s add hypothermia
execute as @a[gamemode=survival,scores={thirst=1..}] run tag @s remove hypothermia

execute as @a[gamemode=survival,scores={thirstSat=..0}] if score @s thirstTick >= @s thirstReset run scoreboard players remove @s thirst 1
execute as @a[gamemode=survival,scores={thirstSat=1..}] if score @s thirstTick >= @s thirstReset run scoreboard players remove @s thirstSat 1
execute as @a if score @s thirstTick >= @s thirstReset run scoreboard players set @s thirstTick 0
execute as @a if score @s thirstSat > @s thirst run scoreboard players operation @s thirstSat = @s thirst


effect give @a[gamemode=survival,scores={thirst=..6}] slowness 1 0 true


#change when thirst decreases based on temperature
execute as @a[scores={temp=25..25}] run scoreboard players set @s thirstReset 150
execute as @a[scores={temp=24..24}] run scoreboard players set @s thirstReset 160
execute as @a[scores={temp=23..23}] run scoreboard players set @s thirstReset 170
execute as @a[scores={temp=22..22}] run scoreboard players set @s thirstReset 180
execute as @a[scores={temp=21..21}] run scoreboard players set @s thirstReset 190
execute as @a[scores={temp=20..20}] run scoreboard players set @s thirstReset 200
execute as @a[scores={temp=19..19}] run scoreboard players set @s thirstReset 210
execute as @a[scores={temp=18..18}] run scoreboard players set @s thirstReset 220
execute as @a[scores={temp=17..17}] run scoreboard players set @s thirstReset 230
execute as @a[scores={temp=16..16}] run scoreboard players set @s thirstReset 240
execute as @a[scores={temp=15..15}] run scoreboard players set @s thirstReset 250
execute as @a[scores={temp=14..14}] run scoreboard players set @s thirstReset 260
execute as @a[scores={temp=13..13}] run scoreboard players set @s thirstReset 270
execute as @a[scores={temp=12..12}] run scoreboard players set @s thirstReset 280
execute as @a[scores={temp=11..11}] run scoreboard players set @s thirstReset 290
execute as @a[scores={temp=..10}] run scoreboard players set @s thirstReset 300
#execute as @a[scores={temp=9..9}] run scoreboard players set @s thirstReset 310
#execute as @a[scores={temp=8..8}] run scoreboard players set @s thirstReset 320
#execute as @a[scores={temp=7..7}] run scoreboard players set @s thirstReset 330
#execute as @a[scores={temp=6..6}] run scoreboard players set @s thirstReset 340
#execute as @a[scores={temp=5..5}] run scoreboard players set @s thirstReset 350
#execute as @a[scores={temp=4..4}] run scoreboard players set @s thirstReset 360
#execute as @a[scores={temp=3..3}] run scoreboard players set @s thirstReset 370
#execute as @a[scores={temp=2..2}] run scoreboard players set @s thirstReset 380
#execute as @a[scores={temp=1..1}] run scoreboard players set @s thirstReset 390
#execute as @a[scores={temp=0..0}] run scoreboard players set @s thirstReset 400




#purified water bottle
tag @a[nbt={SelectedItem:{id:"minecraft:potion",tag:{display:{Name:'{"text":"Purified Water Bottle","italic":false}'}}}}] add holdingPureBottle
scoreboard players add @a[scores={drinkBottle=1..},tag=holdingPureBottle] thirst 4
scoreboard players add @a[scores={drinkBottle=1..},tag=holdingPureBottle] thirstSat 4
tag @a[tag=holdingPureBottle,nbt=!{SelectedItem:{id:"minecraft:potion",tag:{display:{Name:'{"text":"Purified Water Bottle","italic":false}'}}}}] remove holdingPureBottle

#drink potion
scoreboard players add @a[tag=!holdingPureBottle,scores={drinkBottle=1..}] thirst 4
scoreboard players add @a[tag=!holdingPureBottle,scores={drinkBottle=1..}] thirstSat 4


#drink from water
execute as @a[nbt=!{SelectedItem:{}}] at @s if block ^ ^.5 ^1 water if block ~ ~1 ~ air run replaceitem entity @s weapon.mainhand carrot_on_a_stick{CustomModelData:122,display:{Name:'{"text":""}'}}
execute as @a[nbt=!{SelectedItem:{}}] at @s if block ^ ^.5 ^1 water if block ~ ~1 ~ cave_air run replaceitem entity @s weapon.mainhand carrot_on_a_stick{CustomModelData:122,display:{Name:'{"text":""}'}}
execute as @a[scores={useCarrotStick=1..},nbt={SelectedItem:{id:"minecraft:carrot_on_a_stick",tag:{display:{Name:'{"text":""}'}}}}] run scoreboard players add @s thirst 2
execute as @a[scores={useCarrotStick=1..},nbt={SelectedItem:{id:"minecraft:carrot_on_a_stick",tag:{display:{Name:'{"text":""}'}}}}] at @s run playsound entity.generic.drink player @s ~ ~ ~ 1 1 1
execute as @a[scores={useCarrotStick=1..},nbt={SelectedItem:{id:"minecraft:carrot_on_a_stick",tag:{display:{Name:'{"text":""}'}}}}] run scoreboard players set @s useCarrotStick 0
execute as @a[nbt={SelectedItem:{id:"minecraft:carrot_on_a_stick",tag:{display:{Name:'{"text":""}'}}}}] at @s unless block ^ ^.5 ^1 water run replaceitem entity @s weapon.mainhand air
execute as @a[nbt={SelectedItem:{id:"minecraft:carrot_on_a_stick",tag:{display:{Name:'{"text":""}'}}}}] at @s unless block ~ ~1 ~ air run replaceitem entity @s weapon.mainhand air
#execute as @a at @s unless block ^ ^.5 ^1 water run clear @s minecraft:carrot_on_a_stick{display:{Name:'{"text":""}'}}
kill @e[type=item,nbt={Item:{tag:{display:{Name:'{"text":""}'}}}}]

#milk bucket
scoreboard players add @a[tag=!holdingPureBucket,scores={drinkMilk=1..}] thirst 6
scoreboard players add @a[tag=!holdingPureBucket,scores={drinkMilk=1..}] thirstSat 6

#canteen + empty canteen


scoreboard players set @a[nbt={SelectedItemSlot:0}] slot 0
scoreboard players set @a[nbt={SelectedItemSlot:1}] slot 1
scoreboard players set @a[nbt={SelectedItemSlot:2}] slot 2
scoreboard players set @a[nbt={SelectedItemSlot:3}] slot 3
scoreboard players set @a[nbt={SelectedItemSlot:4}] slot 4
scoreboard players set @a[nbt={SelectedItemSlot:5}] slot 5
scoreboard players set @a[nbt={SelectedItemSlot:6}] slot 6
scoreboard players set @a[nbt={SelectedItemSlot:7}] slot 7
scoreboard players set @a[nbt={SelectedItemSlot:8}] slot 8


#execute as @a[nbt={SelectedItem:{display:{Name:'{"text":"Empty Canteen","italic":false}',Lore:['{"text":"0/4","color":"blue","italic":false}']},HideFlags:32,4:0,CustomModelData:126,CustomPotionColor:16777215}}] run


tag @a[nbt={SelectedItem:{tag:{display:{Name:'{"text":"Water Canteen","italic":false}'}}}}] add holdingRegCanteen
tag @a[nbt={Inventory:[{Slot:-106b,tag:{display:{Name:'{"text":"Water Canteen","italic":false}'}}}]}] add holdingRegCanteen
tag @a[nbt={Inventory:[{Slot:-106b,tag:{display:{Name:'{"text":"Purified Water Canteen","italic":false}'}}}]}] add holdingPureCanteen
tag @a[nbt={SelectedItem:{tag:{display:{Name:'{"text":"Purified Water Canteen","italic":false}'}}}}] add holdingPureCanteen
tag @a[tag=holdingPureCanteen,nbt={Inventory:[{Slot:-106b,tag:{display:{Name:'{"text":"Purified Water Canteen","italic":false}'}}}]}] add offhand
tag @a[tag=holdingRegCanteen,nbt={Inventory:[{Slot:-106b,tag:{display:{Name:'{"text":"Water Canteen","italic":false}'}}}]}] add offhand


tag @a[nbt={SelectedItem:{tag:{display:{Name:'{"text":"Empty Canteen","italic":false}'}}}}] add holdingEmpty
replaceitem entity @a[tag=!offhand,tag=holdingRegCanteen,scores={drinkBottle=1..,canteenDur=4..4}] weapon.mainhand minecraft:potion{display:{Name:'{"text":"Water Canteen","italic":false}',Lore:['{"text":"3/4","color":"blue","italic":false}']},HideFlags:32,4:3,CustomModelData:123,CustomPotionColor:16777215} 1
replaceitem entity @a[tag=!offhand,tag=holdingRegCanteen,scores={drinkBottle=1..,canteenDur=3..3}] weapon.mainhand minecraft:potion{display:{Name:'{"text":"Water Canteen","italic":false}',Lore:['{"text":"2/4","color":"blue","italic":false}']},HideFlags:32,4:2,CustomModelData:123,CustomPotionColor:16777215} 1
replaceitem entity @a[tag=!offhand,tag=holdingRegCanteen,scores={drinkBottle=1..,canteenDur=2..2}] weapon.mainhand minecraft:potion{display:{Name:'{"text":"Water Canteen","italic":false}',Lore:['{"text":"1/4","color":"blue","italic":false}']},HideFlags:32,4:1,CustomModelData:123,CustomPotionColor:16777215} 1
replaceitem entity @a[tag=!offhand,tag=holdingRegCanteen,scores={drinkBottle=1..,canteenDur=1..1}] weapon.mainhand minecraft:potion{display:{Name:'{"text":"Water Canteen","italic":false}',Lore:['{"text":"0/4","color":"blue","italic":false}']},HideFlags:32,4:0,CustomModelData:123,CustomPotionColor:16777215} 1
replaceitem entity @a[tag=offhand,tag=holdingRegCanteen,scores={drinkBottle=1..,canteenDur=4..4}] weapon.offhand minecraft:potion{display:{Name:'{"text":"Water Canteen","italic":false}',Lore:['{"text":"3/4","color":"blue","italic":false}']},HideFlags:32,4:3,CustomModelData:123,CustomPotionColor:16777215} 1
replaceitem entity @a[tag=offhand,tag=holdingRegCanteen,scores={drinkBottle=1..,canteenDur=3..3}] weapon.offhand minecraft:potion{display:{Name:'{"text":"Water Canteen","italic":false}',Lore:['{"text":"2/4","color":"blue","italic":false}']},HideFlags:32,4:2,CustomModelData:123,CustomPotionColor:16777215} 1
replaceitem entity @a[tag=offhand,tag=holdingRegCanteen,scores={drinkBottle=1..,canteenDur=2..2}] weapon.offhand minecraft:potion{display:{Name:'{"text":"Water Canteen","italic":false}',Lore:['{"text":"1/4","color":"blue","italic":false}']},HideFlags:32,4:1,CustomModelData:123,CustomPotionColor:16777215} 1
replaceitem entity @a[tag=offhand,tag=holdingRegCanteen,scores={drinkBottle=1..,canteenDur=1..1}] weapon.offhand minecraft:potion{display:{Name:'{"text":"Water Canteen","italic":false}',Lore:['{"text":"0/4","color":"blue","italic":false}']},HideFlags:32,4:0,CustomModelData:123,CustomPotionColor:16777215} 1

replaceitem entity @a[tag=holdingPureCanteen,scores={drinkBottle=1..,canteenDur=4..4}] weapon.mainhand minecraft:potion{display:{Name:'{"text":"Purified Water Canteen","italic":false}',Lore:['{"text":"3/4","color":"blue","italic":false}']},HideFlags:32,4:3,CustomModelData:123,CustomPotionColor:16777215} 1
replaceitem entity @a[tag=holdingPureCanteen,scores={drinkBottle=1..,canteenDur=3..3}] weapon.mainhand minecraft:potion{display:{Name:'{"text":"Purified Water Canteen","italic":false}',Lore:['{"text":"2/4","color":"blue","italic":false}']},HideFlags:32,4:2,CustomModelData:123,CustomPotionColor:16777215} 1
replaceitem entity @a[tag=holdingPureCanteen,scores={drinkBottle=1..,canteenDur=2..2}] weapon.mainhand minecraft:potion{display:{Name:'{"text":"Purified Water Canteen","italic":false}',Lore:['{"text":"1/4","color":"blue","italic":false}']},HideFlags:32,4:1,CustomModelData:123,CustomPotionColor:16777215} 1
replaceitem entity @a[tag=holdingPureCanteen,scores={drinkBottle=1..,canteenDur=1..1}] weapon.mainhand minecraft:potion{display:{Name:'{"text":"Purified Water Canteen","italic":false}',Lore:['{"text":"0/4","color":"blue","italic":false}']},HideFlags:32,4:0,CustomModelData:123,CustomPotionColor:16777215} 1

replaceitem entity @a[scores={canteenDur=0..0},nbt={SelectedItem:{tag:{display:{Lore:['{"text":"0/4","color":"blue","italic":false}']},HideFlags:32,4:0,CustomModelData:123,CustomPotionColor:16777215}}}] weapon.mainhand minecraft:glass_bottle{display:{Name:'{"text":"Empty Canteen","italic":false}',Lore:['{"text":"0/4","color":"blue","italic":false}']},HideFlags:32,4:0,CustomModelData:126,CustomPotionColor:16777215} 1
replaceitem entity @a[scores={fillCanteen=1..1},tag=holdingEmpty,nbt={SelectedItem:{id:"minecraft:potion"}}] weapon.mainhand minecraft:potion{display:{Name:'{"text":"Water Canteen","italic":false}',Lore:['{"text":"4/4","color":"blue","italic":false}']},HideFlags:32,4:4,CustomModelData:123,CustomPotionColor:16777215} 1
execute as @a[nbt={SelectedItem:{id:"minecraft:potion"}}] store result score @s canteenDur run data get entity @s SelectedItem.tag.4
execute as @a[nbt={Inventory:[{Slot:-106b,id:"minecraft:potion"}]}] store result score @s canteenDur run data get entity @s Inventory[{Slot:-106b}].tag.4
tag @a[nbt=!{SelectedItem:{tag:{display:{Name:'{"text":"Water Canteen","italic":false}'}}}},nbt=!{Inventory:[{Slot:-106b,tag:{display:{Name:'{"text":"Water Canteen","italic":false}'}}}]}] remove holdingRegCanteen
tag @a[nbt=!{SelectedItem:{tag:{display:{Name:'{"text":"Purified Water Canteen","italic":false}'}}}},nbt=!{Inventory:[{Slot:-106b,tag:{display:{Name:'{"text":"Purified Water Canteen","italic":false}'}}}]}] remove holdingPureCanteen
tag @a[tag=holdingPureCanteen,nbt=!{Inventory:[{Slot:-106b,tag:{display:{Name:'{"text":"Purified Water Canteen","italic":false}'}}}]}] remove offhand
tag @a[tag=holdingRegCanteen,nbt=!{Inventory:[{Slot:-106b,tag:{display:{Name:'{"text":"Water Canteen","italic":false}'}}}]}] remove offhand
#give canteen if stacked
	tag @a[tag=holdingEmpty,nbt=!{SelectedItem:{Count:1b}}] add multCanteens
	clear @a[tag=multCanteens,scores={fillCanteen=1..}] minecraft:potion{Potion:"minecraft:water"} 1
	give @a[tag=multCanteens,scores={fillCanteen=1..}] minecraft:potion{display:{Name:'{"text":"Water Canteen","italic":false}',Lore:['{"text":"4/4","color":"blue","italic":false}']},HideFlags:32,4:4,CustomModelData:123,CustomPotionColor:16777215} 1
	tag @a[tag=holdingEmpty,nbt={SelectedItem:{Count:1b}}] remove multCanteens
tag @a[nbt=!{SelectedItem:{tag:{display:{Name:'{"text":"Empty Canteen","italic":false}'}}}}] remove holdingEmpty



#/give @p minecraft:potion{display:{Name:'{"text":"Purified Water Canteen","italic":false}',Lore:['{"text":"4/4","color":"blue","italic":false}']},HideFlags:32,CustomModelData:123,4,CustomPotionColor:16777215} 1

#purified water bucket
tag @a[nbt={SelectedItem:{id:"minecraft:milk_bucket",tag:{CustomModelData:123}}}] add holdingPureBucket
scoreboard players add @a[tag=holdingPureBucket,scores={drinkMilk=1..}] thirst 12
scoreboard players add @a[tag=holdingPureBucket,scores={drinkMilk=1..}] thirstSat 12
tag @a[tag=holdingPureBucket,scores={drinkMilk=1..}] remove holdingPureBucket

#reset milk bucket drink score + water bottle drinkscore (MUST GO ON BOTTOM)
scoreboard players set @a[scores={drinkMilk=1..}] drinkMilk 0
#scoreboard players set @a[scores={drinkBottle=1..},nbt=!{SelectedItem:{tag:{display:{Name:'{"text":"Purified Water Canteen","italic":false}',Lore:['{"text":"3/4","color":"blue","italic":false}']},HideFlags:32,CustomModelData:123,CustomPotionColor:16777215}}}] drinkBottle 0
scoreboard players set @a[scores={drinkBottle=1..}] drinkBottle 0
scoreboard players set @a[scores={fillCanteen=1..}] fillCanteen 0