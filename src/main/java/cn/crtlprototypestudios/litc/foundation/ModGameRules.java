package cn.crtlprototypestudios.litc.foundation;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class ModGameRules {
    public static final GameRules.Key<GameRules.BooleanRule>
            SHOW_PLAYER_NAMETAGS = GameRuleRegistry.register("showPlayerNametags", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false)),
            PROXIMITY_CHAT = GameRuleRegistry.register("proximityChat", GameRules.Category.CHAT, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.IntRule> PROXIMITY_CHAT_DISTANCE = GameRuleRegistry.register("proximityChatDistance", GameRules.Category.CHAT, GameRuleFactory.createIntRule(16));

    public static void register() {

    }
}
