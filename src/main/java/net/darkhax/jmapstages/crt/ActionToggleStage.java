package net.darkhax.jmapstages.crt;

import com.blamejared.crafttweaker.api.action.base.IAction;
import net.darkhax.jmapstages.JMapStages;

public class ActionToggleStage implements IAction {
    
    private final Type type;
    private final String stage;
    
    public ActionToggleStage(Type type, String stage) {
        this.type = type;
        this.stage = stage;
    }
    
    @Override
    public void apply () {
        switch(this.type) {
            case DEATHPOINT:
                JMapStages.stageDeathPoint = this.stage;
                return;
            case FULLSCREEN:
                JMapStages.stageFullscreen = this.stage;
                return;
            case MINIMAP:
                JMapStages.stageMinimap = this.stage;
                return;
            case WAYPOINT:
                JMapStages.stageWaypoint = this.stage;
        }
    }
    
    @Override
    public String describe() {
        return String.format("Restricting Journey Map %s stage to %s.",this.type.name().toLowerCase(),this.stage);
    }
    
    public enum Type {
        FULLSCREEN,
        MINIMAP,
        WAYPOINT,
        DEATHPOINT
    }
}