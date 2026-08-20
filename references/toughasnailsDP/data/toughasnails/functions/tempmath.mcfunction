#run temptick clock (allows for adding up environmental factors)
	scoreboard players add tempTick tick 1
	execute if score tempTick tick matches 10.. run scoreboard players set tempTick tick 0
#reset tempscore to 12 (equilibrium) to add/subtract environmental factors which will then be used to change temperature
	execute if score tempTick tick matches 9.. run scoreboard players set @a tempscore 12
#wont allow 'tempscore' below temp value (allows for temperature change variables to actually work [ex. wool coat when player has 'tempscore' of -4 will only bring temp up to zero which its already at])
	execute as @a if score @s tempscore matches ..-1 run scoreboard players set @s tempscore 0
	execute as @a if score @s tempscore matches 26.. run scoreboard players set @s tempscore 25


scoreboard players add @a totalTempTick 1
execute as @a if score @s tempscore < @s temp if score @s totalTempTick >= @s TTTResetTime run scoreboard players remove @s temp 1
execute as @a if score @s tempscore > @s temp if score @s totalTempTick >= @s TTTResetTime run scoreboard players add @s temp 1
execute as @a at @s if score @s totalTempTick >= @s TTTResetTime run scoreboard players set @s totalTempTick 0

#increase totalTempTick reset time to allow for faster/slower cooling off/warming up (ex. temp = 25, jump in water = fast cool off)
execute as @a if score @s tempDifference matches 1..3 run scoreboard players set @s TTTResetTime 320
execute as @a if score @s tempDifference matches 4..6 run scoreboard players set @s TTTResetTime 260
execute as @a if score @s tempDifference matches 7..9 run scoreboard players set @s TTTResetTime 200
execute as @a if score @s tempDifference matches 10..12 run scoreboard players set @s TTTResetTime 140
execute as @a if score @s tempDifference matches 13.. run scoreboard players set @s TTTResetTime 80

#finds difference in players core temp vs environment temp
execute as @a if score @s tempscore < @s temp if score tempTick tick matches 4..4 run scoreboard players operation @s tempDifference = @s temp
execute as @a if score @s tempscore > @s temp if score tempTick tick matches 4..4 run scoreboard players operation @s tempDifference = @s tempscore
execute as @a if score @s tempscore > @s temp if score tempTick tick matches 4..4 run scoreboard players operation @s tempDifference -= @s temp
execute as @a if score @s tempscore < @s temp if score tempTick tick matches 4..4 run scoreboard players operation @s tempDifference -= @s tempscore

#wont allow 'temp' to go above 25 or below 0
execute as @a[scores={temp=26..}] at @s run scoreboard players set @s temp 25
execute as @a[scores={temp=..-1}] at @s run scoreboard players set @s temp 0

#just spawned, resets temperature and thirst
tag @a[scores={TANdeaths=1..}] remove spawned
scoreboard players set @a[tag=!spawned] temp 12
scoreboard players set @a[tag=!spawned] thirst 20
scoreboard players set @a[tag=!spawned] thirstSat 10
tag @a[tag=!spawned] add spawned
scoreboard players set @a[scores={TANdeaths=1..}] TANdeaths 0

execute as @a[tag=!spawned] at @s run setblock ~ ~ ~ water
execute as @a[tag=!spawned] at @s run setblock ~ ~ ~ air

