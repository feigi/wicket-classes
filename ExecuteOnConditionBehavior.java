package de.core_concepts.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;

import static de.o2.portal.ui.selfcare.common.behavior.ExecuteOnConditionBehavior.LifecylcePhase.*;

/**
 * A behavior that performs a callback if the given condition is true.
 * If needed, the lifecycle phase in which the callback should occur
 * can be configured.
 */
public class ExecuteOnConditionBehavior extends Behavior {

    public enum LifecylcePhase {
        ON_CONFIGURE, BEFORE_RENDER, AFTER_RENDER
    }

    private IExecuteOnVisibility delegate;
    private final IModel<Boolean> condition;
    private boolean executed;
    private boolean executeOnce;
    private LifecylcePhase phase = ON_CONFIGURE;

    public interface IExecuteOnVisibility {
        void executeOnCondition(Component component);
    }

    public ExecuteOnConditionBehavior(IExecuteOnVisibility delegate, IModel<Boolean> condition) {
        this.delegate = delegate;
        this.condition = condition;
    }

    public ExecuteOnConditionBehavior onlyOnce() {
        this.executeOnce = true;
        return this;
    }

    public ExecuteOnConditionBehavior inPhase(LifecylcePhase phase) {
        this.phase = phase;
        return this;
    }

    @Override
    public void beforeRender(Component component) {
        super.beforeRender(component);
        if (mayExecute(BEFORE_RENDER)) {
            execute(component);
        }
    }

    @Override
    public void afterRender(Component component) {
        super.afterRender(component);
        if (mayExecute(AFTER_RENDER)) {
            execute(component);
        }
    }

    @Override
    public void onConfigure(Component component) {
        super.onConfigure(component);
        if (mayExecute(ON_CONFIGURE)) {
            execute(component);
        }
    }

    private void execute(Component component) {
        delegate.executeOnCondition(component);
        executed = true;
    }

    private boolean mayExecute(LifecylcePhase currentPhase) {
        return isCorrectPhase(currentPhase)
                && (executeOnce ? !executed : true)
                && conditionTrue();
    }

    private boolean isCorrectPhase(LifecylcePhase currentPhase) {
        return currentPhase == phase;
    }

    private boolean conditionTrue() {
        return condition.getObject().equals(Boolean.TRUE);
    }
}
