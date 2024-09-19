package cn.crtlprototypestudios.litc.experimental.neural.ssm;

import java.util.*;
import java.util.function.Supplier;

public class SituationalStateMachine {
    private final List<SituationCondition> conditions;
//    private final Map<SituationCondition, Double> weights;
//    private final Map<SituationCondition, Supplier<Boolean>> conditionChecks;
//    private final Map<SituationCondition, Runnable> actions;
    private final double normalizedThreshold;

    public SituationalStateMachine(double normalizedThreshold) {
        this.conditions = new ArrayList<>();
        this.normalizedThreshold = normalizedThreshold;
    }

    public void addCondition(String name, double weight, Supplier<Boolean> check, Runnable action, double passingThreshold) {
        SituationCondition condition = new SituationCondition(name, weight, check, action, passingThreshold);
        conditions.add(condition);
    }

    public void process() {
        Map<SituationCondition, Double> weightedChances = new HashMap<>();
        double totalWeight = 0;

        // Perception-Interpretation Process
        for (SituationCondition condition : conditions) {
            double weight = condition.getWeight();
            totalWeight += weight;
            if (condition.checkCondition()) {
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
                    condition.runAction();
                }
            }
        }
    }

//    public static void main(String[] args) {
//        SituationalStateMachine ssm = new SituationalStateMachine(0.5);
//
//        // Add conditions with their weights, checks, and actions
//        ssm.addCondition("IsOnFire", 0.9, () -> true, () -> System.out.println("Putting out fire!"), 0.6);
//        ssm.addCondition("IsThirsty", 0.6, () -> true, () -> System.out.println("Drinking water"), 0.3);
//        ssm.addCondition("IsHot", 0.3, () -> true, () -> System.out.println("Cooling down"), 0.1);
//        ssm.addCondition("IsOnRoughGround", 0.1, () -> true, () -> System.out.println("Sitting down"), 0.7);
//
//        // Process the state machine
//        ssm.process();
//    }
}
