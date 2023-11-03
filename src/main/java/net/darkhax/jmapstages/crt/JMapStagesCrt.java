package net.darkhax.jmapstages.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import net.darkhax.jmapstages.crt.ActionToggleStage.Type;
import org.openzen.zencode.java.ZenCodeType;

@SuppressWarnings("unused")
@ZenRegister
@ZenCodeType.Name("mods.jmapstages.JMapStages")
public class JMapStagesCrt {
    
    @ZenCodeType.Method
    public static void setFullscreenStage(String stage) {
        CraftTweakerAPI.apply(new ActionToggleStage(Type.FULLSCREEN, stage));
    }

    @ZenCodeType.Method
    public static void setMinimapStage(String stage) {
        CraftTweakerAPI.apply(new ActionToggleStage(Type.MINIMAP, stage));
    }

    @ZenCodeType.Method
    public static void setWaypointStage(String stage) {
        CraftTweakerAPI.apply(new ActionToggleStage(Type.WAYPOINT, stage));
    }

    @ZenCodeType.Method
    public static void setDeathpointStage(String stage) {
        CraftTweakerAPI.apply(new ActionToggleStage(Type.DEATHPOINT, stage));
    }
}
