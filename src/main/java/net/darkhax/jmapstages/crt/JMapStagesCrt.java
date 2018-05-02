package net.darkhax.jmapstages.crt;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import net.darkhax.jmapstages.crt.ActionToggleStage.Type;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.jmapstages.JMapStages")
public class JMapStagesCrt {
    
    @ZenMethod
    public static void setFullscreenStage (String stage) {
        
        CraftTweakerAPI.apply(new ActionToggleStage(Type.FULLSCREEN, stage));
    }
    
    @ZenMethod
    public static void setMinimapStage (String stage) {
        
        CraftTweakerAPI.apply(new ActionToggleStage(Type.MINIMAP, stage));
    }
    
    @ZenMethod
    public static void setWaypointStage (String stage) {
        
        CraftTweakerAPI.apply(new ActionToggleStage(Type.WAYPOINT, stage));
    }
    
    @ZenMethod
    public static void setDeathpointStage (String stage) {
        
        CraftTweakerAPI.apply(new ActionToggleStage(Type.DEATHPOINT, stage));
    }
}
