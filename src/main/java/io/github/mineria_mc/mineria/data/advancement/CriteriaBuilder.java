package io.github.mineria_mc.mineria.data.advancement;

import net.minecraft.Util;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTriggerInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CriteriaBuilder {
    private final Map<String, Criterion> criteria = new HashMap<>();
    private final Stack<Stack<String>> requirements = Util.make(new Stack<>(), stacks -> stacks.push(new Stack<>()));
    private final Empty emptyBuilder;
    private final Chainable chainableBuilder;

    public CriteriaBuilder() {
        this.emptyBuilder = (criterionId, criterion) -> {
            putCriterion(criterionId, criterion);
            requirements.peek().push(criterionId);
            return getChainableBuilder();
        };
        this.chainableBuilder = new Chainable() {
            @Override
            public Chainable or(String criterionId, Criterion criterion) {
                putCriterion(criterionId, criterion);
                requirements.peek().push(criterionId);
                return this;
            }

            @Override
            public Chainable and(String criterionId, Criterion criterion) {
                putCriterion(criterionId, criterion);
                Stack<String> requirementSet = new Stack<>();
                requirementSet.push(criterionId);
                requirements.push(requirementSet);
                return this;
            }
        };
    }

    private void putCriterion(String criterionId, Criterion criterion) {
        if(criteria.containsKey(criterionId)) {
            throw new IllegalArgumentException("Duplicate criterion " + criterionId);
        }
        this.criteria.put(criterionId, criterion);
    }

    public Empty getEmptyBuilder() {
        return emptyBuilder;
    }

    public Chainable getChainableBuilder() {
        return chainableBuilder;
    }

    public Map<String, Criterion> getCriteria() {
        return criteria;
    }

    public String[][] getRequirements() {
        return requirements.stream().map(stacks -> stacks.toArray(String[]::new)).toArray(String[][]::new);
    }

    public interface Empty {
        Chainable add(String criterionId, Criterion criterion);

        default Chainable add(String criterionId, CriterionTriggerInstance triggerInstance) {
            return add(criterionId, new Criterion(triggerInstance));
        }
    }

    public interface Chainable {
        Chainable or(String criterionId, Criterion criterion);

        Chainable and(String criterionId, Criterion criterion);

        default Chainable or(String criterionId, CriterionTriggerInstance triggerInstance) {
            return or(criterionId, new Criterion(triggerInstance));
        }

        default Chainable and(String criterionId, CriterionTriggerInstance triggerInstance) {
            return and(criterionId, new Criterion(triggerInstance));
        }
    }
}
