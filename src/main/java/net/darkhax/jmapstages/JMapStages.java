package net.darkhax.jmapstages;

import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.waypoint.WaypointEditor;
import journeymap.client.ui.waypoint.WaypointManager;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Objects;

@Mod(JMapStagesConstants.MODID)
public class JMapStages {
    
    public static String stageFullscreen = "";
    public static String stageMinimap = "";
    public static String stageWaypoint = "";
    public static String stageDeathPoint = "";
    
    private JMapPermissionHandler perms;

    public JMapStages() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void loadComplete(final FMLLoadCompleteEvent ev) {
        this.perms = new JMapPermissionHandler();
    }
    
    @SubscribeEvent
    public void onGuiOpen(ScreenOpenEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(Objects.nonNull(player)) {
            Screen screen = event.getScreen();
            if(!stageWaypoint.isEmpty() && (screen instanceof WaypointEditor || screen instanceof WaypointManager) &&
                    !GameStageHelper.hasStage(player,stageWaypoint)) {
                player.sendMessage(new TranslatableComponent("jmapstages.restrict.waypoint",stageWaypoint),player.getUUID());
                event.setCanceled(true);
            } else if(!stageFullscreen.isEmpty() && screen instanceof Fullscreen && !GameStageHelper.hasStage(player,stageFullscreen)) {
                player.sendMessage(new TranslatableComponent("jmapstages.restrict.fullscreen",stageFullscreen),player.getUUID());
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public void onStageSynced(StagesSyncedEvent event) {
        if(event.getData().hasStage(stageMinimap)) this.perms.toggleMinimap(true);
    }
    
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.player.level.isClientSide && event.player.level.getGameTime()%5==0) {
            Player player = event.player;
            if(!stageMinimap.isEmpty() && !GameStageHelper.hasStage(player,stageMinimap))
                this.perms.toggleMinimap(false);
            if(!stageWaypoint.isEmpty() && !GameStageHelper.hasStage(player,stageWaypoint))
                this.perms.clearWaypoints();
            if(!stageDeathPoint.isEmpty() && !GameStageHelper.hasStage(player,stageDeathPoint))
                this.perms.clearDeathpoints();
        }
    }
}