

tag @a[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add holdingWaterBucket
tag @a[nbt=!{SelectedItem:{id:"minecraft:water_bucket"}}] remove holdingWaterBucket
tag @a[nbt={SelectedItem:{id:"minecraft:milk_bucket",tag:{CustomModelData:122}}}] add holdingMilkBucket
tag @a[nbt=!{SelectedItem:{id:"minecraft:milk_bucket",tag:{CustomModelData:122}}}] remove holdingMilkBucket

execute as @a[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] at @s run summon armor_stand ^ ^1 ^1.0 {Tags:["milkTestAS1"],Small:1b,NoGravity:1b,Marker:1b,Invisible:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] at @s run summon armor_stand ^ ^1 ^2.288 {Tags:["milkTestAS2"],Small:1b,NoGravity:1b,Marker:1b,Invisible:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] at @s run summon armor_stand ^ ^1 ^3.388 {Tags:["milkTestAS3"],Small:1b,NoGravity:1b,Marker:1b,Invisible:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] at @s run summon armor_stand ^ ^1 ^4.488 {Tags:["milkTestAS4"],Small:1b,NoGravity:1b,Marker:1b,Invisible:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:milk_bucket",tag:{CustomModelData:122,display:{Name:"{\"text\":\"Drink\",\"italic\":\"false\"}"}}}}] at @s run summon armor_stand ^ ^1 ^1.0 {Tags:["milkTestAS1"],Small:1b,NoGravity:1b,Marker:1b,Invisible:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:milk_bucket",tag:{CustomModelData:122,display:{Name:"{\"text\":\"Drink\",\"italic\":\"false\"}"}}}}] at @s run summon armor_stand ^ ^1 ^2.288 {Tags:["milkTestAS2"],Small:1b,NoGravity:1b,Marker:1b,Invisible:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:milk_bucket",tag:{CustomModelData:122,display:{Name:"{\"text\":\"Drink\",\"italic\":\"false\"}"}}}}] at @s run summon armor_stand ^ ^1 ^3.388 {Tags:["milkTestAS3"],Small:1b,NoGravity:1b,Marker:1b,Invisible:1b}
execute as @a[nbt={SelectedItem:{id:"minecraft:milk_bucket",tag:{CustomModelData:122,display:{Name:"{\"text\":\"Drink\",\"italic\":\"false\"}"}}}}] at @s run summon armor_stand ^ ^1 ^4.488 {Tags:["milkTestAS4"],Small:1b,NoGravity:1b,Marker:1b,Invisible:1b}

execute as @e[tag=milkTestAS1] at @s if block ~ ~ ~ cave_air run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 1check
execute as @e[tag=milkTestAS2] at @s if block ~ ~ ~ cave_air run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 2check
execute as @e[tag=milkTestAS3] at @s if block ~ ~ ~ cave_air run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 3check
execute as @e[tag=milkTestAS4] at @s if block ~ ~ ~ cave_air run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 4check
execute as @e[tag=milkTestAS1] at @s if block ~ ~ ~ air run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 1check
execute as @e[tag=milkTestAS2] at @s if block ~ ~ ~ air run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 2check
execute as @e[tag=milkTestAS3] at @s if block ~ ~ ~ air run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 3check
execute as @e[tag=milkTestAS4] at @s if block ~ ~ ~ air run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 4check
execute as @e[tag=milkTestAS1] at @s if block ~ ~ ~ water run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 1check
execute as @e[tag=milkTestAS2] at @s if block ~ ~ ~ water run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 2check
execute as @e[tag=milkTestAS3] at @s if block ~ ~ ~ water run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 3check
execute as @e[tag=milkTestAS4] at @s if block ~ ~ ~ water run tag @p[nbt={SelectedItem:{id:"minecraft:water_bucket"}}] add 4check
#execute as @e[tag=milkTestAS1] at @s unless block ~ ~ ~ cave_air run tag @p[nbt={SelectedItem:{id:"minecraft:milk_bucket",tag:{CustomModelData:122}}}] remove 1check
 
execute as @e[tag=milkTestAS1] at @s unless block ~ ~ ~ cave_air unless block ~ ~ ~ air unless block ~ ~ ~ water run tag @p[tag=1check] remove 1check
execute as @e[tag=milkTestAS2] at @s unless block ~ ~ ~ cave_air unless block ~ ~ ~ air unless block ~ ~ ~ water run tag @p[tag=2check] remove 2check
execute as @e[tag=milkTestAS3] at @s unless block ~ ~ ~ cave_air unless block ~ ~ ~ air unless block ~ ~ ~ water run tag @p[tag=3check] remove 3check
execute as @e[tag=milkTestAS4] at @s unless block ~ ~ ~ cave_air unless block ~ ~ ~ air unless block ~ ~ ~ water run tag @p[tag=4check] remove 4check

kill @e[tag=milkTestAS1]
kill @e[tag=milkTestAS2]
kill @e[tag=milkTestAS3]
kill @e[tag=milkTestAS4]


execute as @a[tag=holdingWaterBucket] if entity @s[tag=1check,tag=2check,tag=3check,tag=4check] run replaceitem entity @s weapon.mainhand milk_bucket{CustomModelData:122,display:{Name:"{\"text\":\"Drink\",\"italic\":\"false\"}"}}
execute as @a[tag=holdingMilkBucket] unless entity @s[tag=1check,tag=2check,tag=3check,tag=4check] run replaceitem entity @s weapon.mainhand water_bucket

