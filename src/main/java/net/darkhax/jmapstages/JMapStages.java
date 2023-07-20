package net.darkhax.jmapstages;

import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.waypoint.WaypointEditor;
import journeymap.client.ui.waypoint.WaypointManager;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = "jmapstages", name = "JMap Stages", version = "@VERSION@", dependencies = "required-after:journeymap@[1.12.2-5.7.1];" +
        "required-after:bookshelf;required-after:gamestages@[2.0.123,);required-after:crafttweaker", clientSideOnly = true)
public class JMapStages {
    
    public static String stageFullscreen = "";
    public static String stageMinimap = "";
    public static String stageWaypoint = "";
    public static String stageDeathPoint = "";
    
    private JMapPermissionHandler perms;
    
    @EventHandler
    public void pre (FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @EventHandler
    public void post (FMLPostInitializationEvent event) {
        this.perms = new JMapPermissionHandler();
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onGuiOpen (GuiOpenEvent event) {
        EntityPlayerSP player = PlayerUtils.getClientPlayerSP();
        GuiScreen screen = event.getGui();
        if (!stageWaypoint.isEmpty() && (screen instanceof WaypointEditor || screen instanceof WaypointManager) &&
                !GameStageHelper.hasStage(player, stageWaypoint)) {
            
            player.sendMessage(new TextComponentTranslation("jmapstages.restrict.waypoint", stageWaypoint));
            event.setCanceled(true);
        }
        else if (!stageFullscreen.isEmpty() && screen instanceof Fullscreen &&
                !GameStageHelper.hasStage(player, stageFullscreen)) {
            player.sendMessage(new TextComponentTranslation("jmapstages.restrict.fullscreen", stageFullscreen));
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onStageSynced (StagesSyncedEvent event) {
        if (event.getData().hasStage(stageMinimap)) this.perms.toggleMinimap(true);
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerTick (TickEvent.PlayerTickEvent event) {
        if (event.player.world.isRemote && event.player.world.getTotalWorldTime() % 5 == 0) {
            EntityPlayer player = event.player;
            if (!stageMinimap.isEmpty() && !GameStageHelper.hasStage(player, stageMinimap))
                this.perms.toggleMinimap(false);
            if (!stageWaypoint.isEmpty() && !GameStageHelper.hasStage(player, stageWaypoint))
                this.perms.clearWaypoints();
            if (!stageDeathPoint.isEmpty() && !GameStageHelper.hasStage(player, stageDeathPoint))
                this.perms.clearDeathpoints();
        }
    }
}