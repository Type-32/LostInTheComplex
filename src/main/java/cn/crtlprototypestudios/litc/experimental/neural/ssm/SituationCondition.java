package cn.crtlprototypestudios.litc.experimental.neural.ssm;

import java.util.Objects;
import java.util.function.Supplier;

public class SituationCondition {
    private final String name;
    private double weight;
    private Supplier<Boolean> conditionCheck;
    private final Runnable action;
    private final double passingThreshold;

    public SituationCondition(String name, double weight, Supplier<Boolean> conditionCheck, Runnable action, double passingThreshold) {
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

    public boolean checkCondition() {
        return conditionCheck.get();
    }

    public Supplier<Boolean> getConditionCheck() {
        return conditionCheck;
    }

    public void setConditionCheck(Supplier<Boolean> conditionCheck) {
        this.conditionCheck = conditionCheck;
    }

    public Runnable getAction() {
        return action;
    }

    public void runAction() {
        action.run();
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
}
