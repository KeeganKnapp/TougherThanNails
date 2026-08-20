Tougher than nails : a polymer mod implementing thirst and temperature mechanics server-side only using polymer's resource pack capabilities.

Goal: add a seamless and vanilla aesthetic fully functioning thirst and temperature mechanics with accompanying client "faked" GUI bars using the action-bar

Core components:

    GUI:
        -thirst bar: will be a row of 10 droplet icons that functions similarly to the hunger-bar, with thirst saturation
            -uses the ascii texture hack to position and texture ascii characters to look as though they lay right above the hunger bar.
    
        -temperature indicator: similarly, uses the ascii texture hack to position and texture a ascii characters to look like a temperature monitor
    
        -all possibilities: because there is only one action-bar, we will need to account for all combinations of temperature + thirst, or rather build the GUI elements at each tick / couple of ticks based on player temp + thirst context.

    Mechanics:
        -thirst: will depend entirely on the temperature of the player to change. (hot => thirsty, thirsty => death)
        -temperature : locational predicates determine the temperature of the player based on real biome info
            -mods : will need to incorporate common mod biomes like teralith etc.
   

    Items:
        -water items: these additional water related items can be crafted to replenish hydration/thirst in addition to vanilla water items (water bottle, bucket, etc)
        -vanilla items: items like the water bottle + potions + milk etc all already exist as drinkable items, and thus can be easily integrated as hydrating items. Items like water buckets may be implemented provided their implementation does not compromise existing mechanics (water placement). 

        -temperature items: temperature items are items that can modify the temperature of the area around the player on top of location. These include certain light sources (mainly combustion based - torches, campfires) and other "hot" blocks, cooling sources (ice, snow, maybeee soul lights?). May need to implent additional items (like the coils from the original mod) to add to the cooling + heating abilities.

        
    Armor:
        -will add custom wool armor + slime armor or other alternatives to provide a way to keep cool/warm in different environments.


Flowchart:

    Game Mechanics :

'''mermaid
---
config:
  layout: elk
---
graph TD
    Player[Player]
    Biome[Current Biome]
    Temp[Temperature Value]
    Hydration[Hydration Level]
    
    Player -->|occupies| Biome
    Biome -->|affects| Temp
    Player -->|tracks| Hydration
    Player -->|tracks| Temp
    
    TempBlocks[Temperature Blocks<br/>in Proximity]
    Temp -->|modified by| TempBlocks
    
    Armor[Equipped Armor]
    Items[Inventory Items]
    
    Armor -->|affects| Temp
    Items -->|can restore| Hydration
    Items -->|can modify| Temp
    Armor -->|affects| Hydration
    
    HydrationEffect[Hydration Effects]
    TempEffect[Temperature Effects]
    
    Hydration -->|causes| HydrationEffect
    Temp -->|causes| TempEffect
    
    HydrationEffect -->|damages| Player
    TempEffect -->|damages| Player
    
    classDef playerNode stroke:#818cf8,fill:#eef2ff
    classDef systemNode stroke:#2dd4bf,fill:#f0fdfa
    classDef effectNode stroke:#fb923c,fill:#fff7ed
    classDef damageNode stroke:#f87171,fill:#fef2f2
    
    class Player playerNode
    class Biome,Temp,Hydration,TempBlocks,Armor,Items systemNode
    class HydrationEffect,TempEffect effectNode
    class HydrationEffect,TempEffect,damageNode damageNode

'''
