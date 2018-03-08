package net.darkhax.jmapstages;

import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = "jmapstages", name = "JMap Stages", version = "@VERSION@", dependencies = "required-after:journeymap@[1.12.2-5.5.2];required-after:bookshelf@[2.2.525,);required-after:gamestages@[1.0.75,);required-after:crafttweaker@[4.1.4.,)", clientSideOnly = true, certificateFingerprint = "@FINGERPRINT@")
public class JMapStages {
    
    public static String stageFullscreen = "";
    public static String stageMinimap = "";
    public static String stageWaypoint = "";
    public static String stageDeathoint = "";
    
    private JMapPermissionHandler perms;
    
    @EventHandler
    public void pre (FMLPreInitializationEvent event) {
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @EventHandler
    public void post (FMLPostInitializationEvent event) {
        
        JMapActionManager actions = new JMapActionManager();
        actions.loadActions();
        this.perms = new JMapPermissionHandler(actions);
    }
    
    @SubscribeEvent
    public void onStageAdded (GameStageEvent.Add event) {
        
        if (event.getPlayer() == Minecraft.getMinecraft().player) {
            
            if (stageFullscreen.equals(event.getStageName())) {
                
                this.perms.toggleFullscreen(true);
            }
            
            else if (stageMinimap.equals(event.getStageName())) {
                
                this.perms.toggleMinimap(true);
            }
            
            else if (stageWaypoint.equals(event.getStageName())) {
                
                this.perms.toggleWaypoints(true);
            }
            
            else if (stageDeathoint.equals(event.getStageName())) {
                
                this.perms.toggleDeathpoints(true);
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerTick (TickEvent.PlayerTickEvent event) {
        
        if (event.player.world.getTotalWorldTime() % 5 == 0) {
            
            final IStageData stages = PlayerDataHandler.getStageData(event.player);
            
            if (!stageFullscreen.isEmpty() && !stages.hasUnlockedStage(stageFullscreen)) {
                
                this.perms.toggleFullscreen(false);
            }
            
            if (!stageMinimap.isEmpty() && !stages.hasUnlockedStage(stageMinimap)) {
                
                this.perms.toggleMinimap(false);
            }
            
            if (!stageWaypoint.isEmpty() && !stages.hasUnlockedStage(stageWaypoint)) {
                
                this.perms.toggleWaypoints(false);
            }
            
            if (!stageDeathoint.isEmpty() && !stages.hasUnlockedStage(stageDeathoint)) {
                
                this.perms.toggleDeathpoints(false);
            }
        }
    }
}