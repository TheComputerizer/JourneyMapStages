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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

import java.util.Objects;

@Mod(JMapStagesConstants.MODID)
public class JMapStages {

    public static JMapStages INSTANCE;
    public String fullscreenStage = "";
    public String minimapStage = "";
    public String wayPointStage = "";
    public String deathPointStage = "";
    
    private JMapPermissionHandler perms;

    public JMapStages() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT,() -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
            MinecraftForge.EVENT_BUS.register(this);
            ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                    () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY,(a,b) -> true));
        });
        INSTANCE = this;
    }

    public void loadComplete(final FMLLoadCompleteEvent ev) {
        this.perms = new JMapPermissionHandler();
    }
    
    @SubscribeEvent
    public void onGuiOpen(ScreenOpenEvent event) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if(Objects.nonNull(player)) {
            Screen screen = event.getScreen();
            if(checkScreen(screen,WaypointEditor.class,WaypointManager.class) && noStage(player,this.wayPointStage))
                sendTranslatedMsg(event,player,"waypoint",this.wayPointStage);
            else if(checkScreen(screen,Fullscreen.class) && noStage(player,this.fullscreenStage))
                sendTranslatedMsg(event,player,"fullscreen",this.fullscreenStage);
        }
    }

    private boolean checkScreen(Screen screen, Class<?> ... screenClasses) {
        if(Objects.isNull(screen)) return false;
        for(Class<?> screenClass : screenClasses)
            if(screenClass.isAssignableFrom(screen.getClass())) return true;
        return false;
    }

    private void sendTranslatedMsg(ScreenOpenEvent event, Player player, String type, String stage) {
        if(Objects.isNull(player)) return;
        TranslatableComponent msg =  new TranslatableComponent(JMapStagesConstants.MODID+".restrict."+type,stage);
        player.sendMessage(msg,player.getUUID());
        event.setCanceled(true);
    }
    
    @SubscribeEvent
    public void onStageSynced(StagesSyncedEvent event) {
        if(event.getData().hasStage(this.minimapStage)) this.perms.toggleMinimap(true);
    }
    
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.player.level.isClientSide && event.player.level.getGameTime()%5==0) {
            Player player = event.player;
            if(noStage(player,this.minimapStage)) this.perms.toggleMinimap(false);
            if(noStage(player,this.wayPointStage)) this.perms.clearWaypoints();
            if(noStage(player,this.deathPointStage)) this.perms.clearDeathpoints();
        }
    }

    private boolean noStage(Player player, String stage) {
        return !stage.isEmpty() && !GameStageHelper.hasStage(player,stage);
    }
}