package cn.crtlprototypestudios.litc.experimental.neural.ssm;

import java.util.*;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SituationalStateMachine {
    private final List<SituationCondition> conditions;
    private final double normalizedThreshold;

    public SituationalStateMachine(double normalizedThreshold) {
        this.conditions = new ArrayList<>();
        this.normalizedThreshold = normalizedThreshold;
    }

    public void addCondition(String name, double weight, Function<SituationalInformation, Boolean> check, Consumer<SituationalInformation> action, double passingThreshold) {
        SituationCondition condition = new SituationCondition(name, weight, check, action, passingThreshold);
        conditions.add(condition);
    }

    public void process(SituationalInformation si) {
        Map<SituationCondition, Double> weightedChances = new HashMap<>();
        double totalWeight = 0;

        // Perception-Interpretation Process
        for (SituationCondition condition : conditions) {
            double weight = condition.getWeight();
            totalWeight += weight;
            if (condition.checkCondition(si)) {
                weightedChances.put(condition, weight);
            }
        }

        // Normalize weighted chances
        for (Map.Entry<SituationCondition, Double> entry : weightedChances.entrySet()) {
            entry.setValue(entry.getValue() / totalWeight);
        }

        // Priority-Comprehension Process
        double totalChance = weightedChances.values().stream().mapToDouble(Double::doubleValue).sum();

        if (totalChance >= normalizedThreshold) {
            // Sort conditions by their weighted chances
            List<Map.Entry<SituationCondition, Double>> sortedConditions = new ArrayList<>(weightedChances.entrySet());
            sortedConditions.sort(Map.Entry.<SituationCondition, Double>comparingByValue().reversed());

            // Execute actions for conditions that pass their individual thresholds
            for (Map.Entry<SituationCondition, Double> entry : sortedConditions) {
                SituationCondition condition = entry.getKey();
                double chance = entry.getValue();
                if (chance >= condition.getPassingThreshold()) {
                    condition.runAction(si);
                }
            }
        }
    }

//    public static void main(String[] args) {
//        SituationalStateMachine ssm = new SituationalStateMachine(0.5);
//        SituationalInformation si = new SituationalInformation(SituationalContext.General, null, null);
//
//        // Add conditions with their weights, checks, and actions
//        ssm.addCondition("IsOnFire", 0.9,
//                info -> true,
//                info -> System.out.println("Putting out fire!"),
//                0.6);
//        ssm.addCondition("IsThirsty", 0.6,
//                info -> true,
//                info -> System.out.println("Drinking water"),
//                0.3);
//        ssm.addCondition("IsHot", 0.3,
//                info -> true,
//                info -> System.out.println("Cooling down"),
//                0.1);
//        ssm.addCondition("IsOnRoughGround", 0.1,
//                info -> true,
//                info -> System.out.println("Sitting down"),
//                0.7);
//
//        // Process the state machine
//        ssm.process(si);
//    }

    public static class Builder {
        private double normalizedThreshold;
        private final SituationalStateMachine ssm;

        public Builder(double normalizedThreshold) {
            this.normalizedThreshold = normalizedThreshold;
            this.ssm = new SituationalStateMachine(normalizedThreshold);
        }

        public Builder setThreshold(double threshold){
            normalizedThreshold = threshold;
            return this;
        }

        public Builder addCondition(String name, double weight, Function<SituationalInformation, Boolean> check, Consumer<SituationalInformation> action, double passingThreshold) {
            ssm.addCondition(name, weight, check, action, passingThreshold);
            return this;
        }

        public SituationalStateMachine build() {
            return ssm;
        }

        public static Builder create(double normalizedThreshold){
            return new Builder(normalizedThreshold);
        }
    }
}