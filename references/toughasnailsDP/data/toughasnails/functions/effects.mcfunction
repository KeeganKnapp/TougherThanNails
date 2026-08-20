#effect tick (effect players with custom "effect" every 4 secs)
	scoreboard players add effectTick tick 1
	execute if score effectTick tick matches 80.. run scoreboard players set effectTick tick 0
#hypothermia
	execute as @a[scores={temp=..0},tag=!hypothermia,tag=!coldResist] at @s run tag @s add hypothermia
	execute as @a[scores={temp=25..},tag=!hypothermia,tag=!heatResist] at @s run tag @s add hypothermia
	scoreboard players add @a[tag=hypothermia] hypoTick 1
	execute as @a[scores={hypoTick=300..,temp=1..}] run tag @s remove hypothermia
	execute as @a[scores={hypoTick=300..,temp=..24}] run tag @s remove hypothermia
	execute as @a[scores={hypoTick=300..}] at @s run scoreboard players set @s hypoTick 0
		execute as @a[tag=hypothermia] if score @s health matches 8.. if score effectTick tick matches 1..1 run effect give @s instant_health
		execute as @a[tag=hypothermia] if score @s health matches 8.. if score effectTick tick matches 1..1 run effect give @s instant_damage
		execute as @a[tag=hypothermia] if score @s health matches ..7 if score effectTick tick matches 1..1 run function toughasnails:oneheartdamage
	
#hyperthermia
	
	


#execute as @a[tag=hypothermia] unless entity @s[nbt={ActiveEffects:[{Id:20b}]}] unless score @s health matches 8.. run effect give @s wither 2 0 true
#execute as @a[tag=hyperthermia] unless entity @s[nbt={ActiveEffects:[{Id:20b}]}] unless score @s health matches 8.. run effect give @s wither 2 0 true



		
