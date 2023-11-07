package net.darkhax.jmapstages.crt;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.blamejared.crafttweaker.api.zencode.IScriptLoadSource;
import net.darkhax.jmapstages.JMapStages;

public class ActionToggleStage implements IRuntimeAction {
    
    private final Type type;
    private final String stage;
    
    public ActionToggleStage(Type type, String stage) {
        this.type = type;
        this.stage = stage;
    }
    
    @Override
    public void apply () {
        switch(this.type) {
            case FULLSCREEN:
                JMapStages.INSTANCE.fullscreenStage = this.stage;
                return;
            case MINIMAP:
                JMapStages.INSTANCE.minimapStage = this.stage;
                return;
            case WAYPOINT:
                JMapStages.INSTANCE.wayPointStage = this.stage;
                return;
            case DEATHPOINT:
                JMapStages.INSTANCE.deathPointStage = this.stage;
        }
    }

    @Override
    public boolean shouldApplyOn(final IScriptLoadSource source) {
        return true;
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