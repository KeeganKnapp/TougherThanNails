#underwater thirst mover
execute as @a at @s if block ~ ~1.25 ~ water run scoreboard players set @s underwater 1
execute as @a at @s if score @s air matches 300.. unless block ~ ~1.25 ~ water run scoreboard players set @s underwater 0
#title @a times 0 1 2
#hot/cold overlay

	

