package net.darkhax.jmapstages;

import journeymap.client.model.Waypoint;
import journeymap.client.properties.WaypointProperties;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;

public class JMapPermissionHandler {
    
    private static final String ACTION_CREATE_WAYPOINT = "key.journeymap.create_waypoint";
    private static final String ACTION_CREATE_WAYPOINT_FULLSCREEN = "key.journeymap.fullscreen_create_waypoint";
    private static final String ACTION_WAYPOINT_MANAGER = "key.journeymap.fullscreen_waypoints";
    private static final String ACTION_MINIMAP_TOGGLE = "key.journeymap.minimap_toggle_alt";
    private static final String ACTION_FULLSCREEN_TOGGLE = "key.journeymap.map_toggle_alt";
    
    private final Minecraft mc;
    private final JMapActionManager actionManager;
    private final UIManager uiManager;
    private final WaypointProperties waypointProps;
    private final WaypointStore waypointData;
    
    public JMapPermissionHandler (JMapActionManager actionManager) {
        
        this.mc = Minecraft.getMinecraft();
        this.actionManager = actionManager;
        this.waypointProps = Journeymap.getClient().getWaypointProperties();
        this.waypointData = WaypointStore.INSTANCE;
        this.uiManager = UIManager.INSTANCE;
    }
    
    public void toggleFullscreen (boolean enable) {
        
        if (this.mc.currentScreen instanceof Fullscreen) {
            
            this.uiManager.closeAll();
        }
        
        // Toggle key binds
        this.actionManager.toggleAction(ACTION_FULLSCREEN_TOGGLE, enable);
    }
    
    public void toggleMinimap (boolean enable) {
        
        // Set the state of the minimap
        this.uiManager.setMiniMapEnabled(enable);
        
        // Toggle key binds
        this.actionManager.toggleAction(ACTION_MINIMAP_TOGGLE, enable);
    }
    
    public void toggleDeathpoints (boolean enable) {
        
        // Toggle the config option
        // this.waypointProps.createDeathpoints.set(enable);
        
        // If disabling, remove all existing deathpoints
        if (!enable) {
            
            for (final Waypoint point : this.waypointData.getAll()) {
                
                if (point.isDeathPoint()) {
                    
                    this.waypointData.remove(point);
                }
            }
        }
    }
    
    public void toggleWaypoints (boolean enable) {
        
        // Toggle the config options
        // this.waypointProps.showStaticBeam.set(enable);
        // this.waypointProps.showRotatingBeam.set(enable);
        // this.waypointProps.showName.set(enable);
        // this.waypointProps.showDistance.set(enable);
        // this.waypointProps.showTexture.set(enable);
        
        // Toggle key binds
        this.actionManager.toggleAction(ACTION_CREATE_WAYPOINT, enable);
        this.actionManager.toggleAction(ACTION_CREATE_WAYPOINT_FULLSCREEN, enable);
        this.actionManager.toggleAction(ACTION_WAYPOINT_MANAGER, enable);
        
        // If disabling, remove all existing waypoints
        if (!enable) {
            
            for (final Waypoint point : this.waypointData.getAll()) {
                
                if (!point.isDeathPoint()) {
                    
                    this.waypointData.remove(point);
                }
            }
        }
    }
}