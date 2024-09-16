package cn.crtlprototypestudios.litc.foundation.event;

import cn.crtlprototypestudios.litc.foundation.ModGameRules;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class AllowChatMessageHandler implements ServerMessageEvents.AllowChatMessage{
    @Override
    public boolean allowChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) {
        if(!sender.getServerWorld().getGameRules().getBoolean(ModGameRules.PROXIMITY_CHAT)) {
            sender.getServerWorld().getPlayers().forEach(player -> player.sendChatMessage(SentMessage.of(message), false, params));
            return false;
        }

        BlockPos senderPos = sender.getBlockPos();
        List<ServerPlayerEntity> playersWithinProx = new ArrayList<>(sender.getServerWorld().getPlayers().stream().filter(serverPlayerEntity -> {
            double dist = Math.sqrt(Math.pow(serverPlayerEntity.getX() - senderPos.getX(), 2) + Math.pow(serverPlayerEntity.getZ() - senderPos.getZ(), 2));
            return dist <= sender.getServerWorld().getGameRules().getInt(ModGameRules.PROXIMITY_CHAT_DISTANCE);
        }).toList());

        playersWithinProx.remove(sender);

        if(playersWithinProx.isEmpty()){
            sender.sendMessageToClient(Text.translatable(String.format("chat.litc.response.no_players_within_proximity.%s", (int)(Math.random() * 9))).withColor(Colors.GRAY), false);
        } else {
            playersWithinProx.forEach(player -> player.sendChatMessage(SentMessage.of(message), false, params));
            sender.sendChatMessage(SentMessage.of(message), false, params);
        }

        return false;
    }

    public Text toMessage(String senderName, String message){
        return Text.of(String.format("<%s> %s", senderName, message));
    }
}
