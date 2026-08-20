

tellraw @s ["",{"text":"                                                                           Tough As Nails Configuration","bold":true,"color":"gold"}]

execute if score waterBucketDrink config matches ..0 run tellraw @s ["",{"text":"[disabled] drinking from regular water buckets","color":"red","clickEvent":{"action":"run_command","value":"/function toughasnails:config/waterbucketdrink1"},"hoverEvent":{"action":"show_text","value":["",{"text":"click to enable"}]}}]
execute if score waterBucketDrink config matches 1.. run tellraw @s ["",{"text":"[enabled] drinking from regular water buckets","color":"green","clickEvent":{"action":"run_command","value":"/function toughasnails:config/waterbucketdrink0"},"hoverEvent":{"action":"show_text","value":["",{"text":"click to disable"}]}}]

execute if score hotcoldoverlay config matches ..0 run tellraw @s ["",{"text":"[disabled] overlay for extreme temperatures (hot/cold)","color":"red","clickEvent":{"action":"run_command","value":"/function toughasnails:config/hotcoldoverlay1"},"hoverEvent":{"action":"show_text","value":["",{"text":"click to enable"}]}}]
execute if score hotcoldoverlay config matches 1.. run tellraw @s ["",{"text":"[enabled] overlay for extreme temperatures (hot/cold)","color":"green","clickEvent":{"action":"run_command","value":"/function toughasnails:config/hotcoldoverlay0"},"hoverEvent":{"action":"show_text","value":["",{"text":"click to disable"}]}}]
