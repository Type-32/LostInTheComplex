package cn.crtlprototypestudios.litc.foundation.entity.predicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.PlayerPredicate;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.stat.StatType;

public class PlayerHealthPredicate {
    public static PlayerPredicate.Builder healthGreaterThan(float health) {
        return PlayerPredicate.Builder.create();
    }
}
