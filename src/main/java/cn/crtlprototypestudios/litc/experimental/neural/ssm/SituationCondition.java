package cn.crtlprototypestudios.litc.experimental.neural.ssm;

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
}