package net.darkhax.jmapstages;

import journeymap.client.model.Waypoint;
import journeymap.client.ui.UIManager;
import journeymap.client.waypoint.WaypointStore;

public class JMapPermissionHandler {

    private final UIManager uiManager;
    private final WaypointStore waypointData;

    public JMapPermissionHandler () {

        this.waypointData = WaypointStore.INSTANCE;
        this.uiManager = UIManager.INSTANCE;
    }

    public void toggleMinimap (boolean enable) {

        this.uiManager.setMiniMapEnabled(enable);
    }

    public void clearDeathpoints () {

        for (final Waypoint point : this.waypointData.getAll()) {

            if (point.isDeathPoint()) {

                point.setEnable(false);
                point.setDirty();
            }
        }
    }

    public void clearWaypoints () {

        for (final Waypoint point : this.waypointData.getAll()) {

            if (!point.isDeathPoint()) {

                point.setEnable(false);
                point.setDirty();
            }
        }
    }
}