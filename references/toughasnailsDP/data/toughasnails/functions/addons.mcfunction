#zombies wear wool coats
execute as @e[type=zombie] at @s if block ~ ~ ~ minecraft:snow run replaceitem entity @s armor.head minecraft:chainmail_helmet
execute as @e[type=zombie] at @s if block ~ ~ ~ minecraft:snow run replaceitem entity @s armor.chest minecraft:chainmail_chestplate
execute as @e[type=zombie] at @s if block ~ ~ ~ minecraft:snow run replaceitem entity @s armor.legs minecraft:chainmail_leggings
execute as @e[type=zombie] at @s if block ~ ~ ~ minecraft:snow run replaceitem entity @s armor.feet minecraft:chainmail_boots

execute if score waterBucketDrink config matches 1.. run function toughasnails:waterbucketdrink

#scoreboard objectives add MGhealth minecraft.used:minecraft.crossbow
#replaceitem entity @a[nbt={SelectedItem:{id: "minecraft:crossbow", Count: 1b, tag: {display: {Name: '{"text":"MG"}'}}}}] weapon.mainhand crossbow{display:{Name:'{"text":"MG"}'},ChargedProjectiles:[{id:"minecraft:arrow",Count:1b}],Charged:1b}
#execute store result score @a MGhealth run replaceitem entity @a[nbt={SelectedItem:{id: "minecraft:crossbow", Count: 1b, tag: {Damage: 0, display: {Name: '{"text":"MG"}'}}}}] weapon.mainhand crossbow{display:{Name:'{"text":"MG"}'},ChargedProjectiles:[{id:"minecraft:arrow",Count:1b}],Charged:1b}
#execute store result entity cobblestoner Damage int 1 run replaceitem entity @a[nbt={SelectedItem:{id: "minecraft:crossbow", Count: 1b, tag: {Damage: 0, display: {Name: '{"text":"MG"}'}}}}] weapon.mainhand crossbow{display:{Name:'{"text":"MG"}'},ChargedProjectiles:[{id:"minecraft:arrow",Count:1b}],Charged:1b}
