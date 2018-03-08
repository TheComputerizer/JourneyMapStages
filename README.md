# Item Stages [![](http://cf.way2muchnoise.eu/289753.svg)](https://minecraft.curseforge.com/projects/journeymapstages) [![](http://cf.way2muchnoise.eu/versions/289753.svg)](https://minecraft.curseforge.com/projects/journeymapstages)

This mod is an addon for the GameStage API. This allows for aspects of Journey Map to be put into custom progression systems. You should check out the GameStage API mod's description for more info. To give a brief run down, stages are parts of the progression system set up by the modpack or server. Stages are given to players through a command, which is typically ran by a questing mod, advancement, or even a Command Block.

[![Nodecraft](https://i.imgur.com/sz9PUmK.png)](https://nodecraft.com/r/darkhax)    
This project is sponsored by Nodecraft. Use code [Darkhax](https://nodecraft.com/r/darkhax) for 30% off your first month of service!

## Setup
This mod uses CraftTweaker for configuration.

## What can be restricted?
 
### Waypoints
The player will not be able to create new waypoints or access the waypoint manager unless they have the required stage unlocked. If they somehow get a waypoint before hand it will be immediately destroyed. 

`mods.jmapstages.JMapStages.setWaypointStage(String stage);`

### Deathpoints
The player will not be able to create death points when they die until they unlock the required stage. If they somehow get a death point before the stage it will be immediately destroyed. 

`mods.jmapstages.JMapStages.setDeathpointStage(String stage);`

### Minimap
The player will not be able to see the minimap until they unlock the required stage. 

`mods.jmapstages.JMapStages.setMinimapStage(String stage);`

### Fullscreen Map / Options
The player will not be able to open the fullscreen map until they have the required stage.

`mods.jmapstages.JMapStages.setFullscreenStage(String stage);`

## Example Script

```
// Restricts players from making waypoints or seeing them in world.
mods.jmapstages.JMapStages.setWaypointStage("one");

// Restricts players from accessing the fullscreen and options screen.
mods.jmapstages.JMapStages.setFullscreenStage("two");

// Restricts players from making death waypoints on the map.
mods.jmapstages.JMapStages.setDeathpointStage("three");

// Restricts the player from having the minimap open on their screen.
mods.jmapstages.JMapStages.setMinimapStage("four");
```