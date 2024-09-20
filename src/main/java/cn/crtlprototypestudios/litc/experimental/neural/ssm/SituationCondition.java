package cn.crtlprototypestudios.litc.experimental.neural.ssm;

import cn.crtlprototypestudios.litc.foundation.ModGameRules;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Consumer;

public class SituationCondition {
    private final String name;
    private double weight;
    private Function<SituationalInformation, Boolean> conditionCheck;
    private final Consumer<SituationalInformation> action;
    private final double passingThreshold;

    public SituationCondition(String name, double weight, Function<SituationalInformation, Boolean> conditionCheck, Consumer<SituationalInformation> action, double passingThreshold) {
        this.name = name;
        this.weight = weight;
        this.conditionCheck = conditionCheck;
        this.action = action;
        this.passingThreshold = passingThreshold;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean checkCondition(SituationalInformation si) {
        return conditionCheck.apply(si);
    }

    public Function<SituationalInformation, Boolean> getConditionCheck() {
        return conditionCheck;
    }

    public void setConditionCheck(Function<SituationalInformation, Boolean> conditionCheck) {
        this.conditionCheck = conditionCheck;
    }

    public Consumer<SituationalInformation> getAction() {
        return action;
    }

    public void runAction(SituationalInformation si) {
        action.accept(si);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SituationCondition condition = (SituationCondition) o;
        return Objects.equals(name, condition.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public double getPassingThreshold() {
        return passingThreshold;
    }

    public static class Predicates {
        public static boolean canSeeOtherPlayers(SituationalInformation information) {
            if (information.serverPlayerEntity.isEmpty() || information.serverWorld.isEmpty()) {
                return false;
            }

            ServerPlayerEntity player = information.serverPlayerEntity.get();
            ServerWorld world = information.serverWorld.get();

            for (ServerPlayerEntity otherPlayer : world.getPlayers()) {
                if (!otherPlayer.equals(player)) {
                    Vec3d start = player.getCameraPosVec(1.0F);
                    Vec3d end = otherPlayer.getCameraPosVec(1.0F);
                    HitResult hitResult = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));

                    // TODO This may not be the best way to check if the player can see another player
                    if (hitResult.getType().equals(HitResult.Type.ENTITY) || (hitResult.getPos().equals(end) || hitResult.getPos().equals(otherPlayer.getPos()))) {
                        return true;
                    }
                }
            }

            return false;
        }

        public static boolean hasPlayersNearby(SituationalInformation information) {
            if (information.serverPlayerEntity.isEmpty() || information.serverWorld.isEmpty()) {
                return false;
            }

            ServerPlayerEntity player = information.serverPlayerEntity.get();
            ServerWorld world = information.serverWorld.get();

            for (ServerPlayerEntity otherPlayer : world.getPlayers()) {
                if (!otherPlayer.equals(player)) {
                    double dist = Math.sqrt(Math.pow(otherPlayer.getX() - player.getX(), 2) + Math.pow(otherPlayer.getZ() - player.getZ(), 2));
                    if (dist <= world.getGameRules().getInt(ModGameRules.PROXIMITY_DISTANCE)) {
                        return true;
                    }
                }
            }

            return false;
        }

        public static boolean isThisPlayerUnseenAndAlone(SituationalInformation information) {
            return !canSeeOtherPlayers(information) && !hasPlayersNearby(information);
        }

        public static boolean isThisPlayerVeryHungry(SituationalInformation information){
            if (information.serverPlayerEntity.isEmpty() || information.serverWorld.isEmpty()) {
                return false;
            }
            return information.serverPlayerEntity.get().getHungerManager().getFoodLevel() < information.serverWorld.get().getGameRules().getInt(ModGameRules.LOW_HUNGER_THRESHOLD);
        }

        public static boolean isThisPlayerOnLowHealth(SituationalInformation information){
            if (information.serverPlayerEntity.isEmpty() || information.serverWorld.isEmpty()) {
                return false;
            }
            return information.serverPlayerEntity.get().getHealth() < information.serverWorld.get().getGameRules().getInt(ModGameRules.LOW_HEALTH_THRESHOLD);
        }
    }
}