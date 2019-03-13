package net.darkhax.jmapstages.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class JMapStagesPacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("jmapstages");

    public static void init() {

        INSTANCE.registerMessage(JMapStagesMessage.JMapStagesMessageHandler.class, JMapStagesMessage.class, 0, Side.CLIENT);
    }
}
