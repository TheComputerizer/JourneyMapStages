package net.darkhax.jmapstages.network;

import io.netty.buffer.ByteBuf;
import net.darkhax.jmapstages.JMapStages;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class JMapStagesMessage implements IMessage {

    private boolean enable;

    public JMapStagesMessage() {

    }

    public JMapStagesMessage(boolean enable) {

        this.enable = enable;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        this.enable = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeBoolean(enable);
    }

    public static class JMapStagesMessageHandler implements IMessageHandler<JMapStagesMessage, IMessage> {

        @Override
        public IMessage onMessage(JMapStagesMessage message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                Minecraft.getMinecraft().addScheduledTask(() -> JMapStages.INSTANCE.perms.toggleMinimap(message.enable));
            }

            return null;
        }
    }
}
