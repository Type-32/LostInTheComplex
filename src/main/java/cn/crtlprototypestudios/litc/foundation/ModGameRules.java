package cn.crtlprototypestudios.litc.foundation;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class ModGameRules {
    public static final GameRules.Key<GameRules.BooleanRule>
            SHOW_PLAYER_NAMETAGS = GameRuleRegistry.register("showPlayerNametags", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false)),
            PROXIMITY_CHAT = GameRuleRegistry.register("proximityChat", GameRules.Category.CHAT, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.IntRule>
            PROXIMITY_DISTANCE = GameRuleRegistry.register("proximityDistance", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(16, 8, 32)),
            LOW_HUNGER_THRESHOLD = GameRuleRegistry.register("lowHungerThreshold", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(6, 2, 10)),
            LOW_HEALTH_THRESHOLD = GameRuleRegistry.register("lowHealthThreshold", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(6, 2, 10));

    public static void register() {

    }
}
