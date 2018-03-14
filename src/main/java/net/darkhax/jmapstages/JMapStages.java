package net.darkhax.jmapstages;

import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.waypoint.WaypointEditor;
import journeymap.client.ui.waypoint.WaypointManager;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.client.Minecraft;
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

@Mod(modid = "jmapstages", name = "JMap Stages", version = "@VERSION@", dependencies = "required-after:journeymap@[1.12.2-5.5.2];required-after:bookshelf@[2.2.535,);required-after:gamestages@[1.0.82,);required-after:crafttweaker@[4.1.4.,)", clientSideOnly = true, certificateFingerprint = "@FINGERPRINT@")
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

        this.perms = new JMapPermissionHandler();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onGuiOpen (GuiOpenEvent event) {

        final EntityPlayer player = Minecraft.getMinecraft().player;
        final IStageData stages = PlayerDataHandler.getStageData(player);

        if ((event.getGui() instanceof WaypointEditor || event.getGui() instanceof WaypointManager) && !stages.hasUnlockedStage(stageWaypoint)) {

            player.sendMessage(new TextComponentTranslation("jmapstages.restrict.waypoint", stageWaypoint));
            event.setCanceled(true);
        }

        else if (event.getGui() instanceof Fullscreen && !stages.hasUnlockedStage(stageFullscreen)) {

            player.sendMessage(new TextComponentTranslation("jmapstages.restrict.fullscreen", stageFullscreen));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onStageAdded (GameStageEvent.Add event) {

        if (event.getEntityPlayer() == Minecraft.getMinecraft().player && stageMinimap.equals(event.getStageName())) {

            this.perms.toggleMinimap(true);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerTick (TickEvent.PlayerTickEvent event) {

        if (event.player.world.getTotalWorldTime() % 5 == 0) {

            final IStageData stages = PlayerDataHandler.getStageData(event.player);

            if (stages.hasBeenSynced()) {

                if (!stageMinimap.isEmpty() && !stages.hasUnlockedStage(stageMinimap)) {

                    this.perms.toggleMinimap(false);
                }

                if (!stageWaypoint.isEmpty() && !stages.hasUnlockedStage(stageWaypoint)) {

                    this.perms.clearWaypoints();
                }

                if (!stageDeathoint.isEmpty() && !stages.hasUnlockedStage(stageDeathoint)) {

                    this.perms.clearDeathpoints();
                }
            }
        }
    }
}