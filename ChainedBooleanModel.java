package de.core_concepts.wicket

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;

/**
 * A model that allows chaining together a boolean expression with IModels of type
 * Boolean. This allows you to perform boolean evaluations without having to access
 * the model object before render time by passing in PropertyModels.
 *
 * Sample usage: new ChainedBooleanPropertyModel(new PropertyModel<Boolean>(...)
 *                      .And(new PropertyModel<Boolean>(...)))
 *
 * See Tests for more examples.
 */
public class ChainedBooleanModel extends AbstractReadOnlyModel<Boolean> {

    private IModel<Boolean> operand;

    public ChainedBooleanModel(IModel<Boolean> operand) {
        this.operand = operand;
    }

    public ChainedBooleanModel(IModel<? extends Serializable> parentModel, String expression) {
        this(new PropertyModel<Boolean>(parentModel, expression));
    }

    private ChainedBooleanModel() {
    }

    public ChainedBooleanModel And(IModel<Boolean> rightOperand) {
        return new And(this, new ChainedBooleanModel(rightOperand));
    }

    public ChainedBooleanModel And(Object parentModel, String expression) {
        return And(new PropertyModel<Boolean>(parentModel, expression));
    }


    public ChainedBooleanModel AndNot(IModel<Boolean> rightOperand) {
        return new And(this, new ChainedBooleanModel(rightOperand).Not());
    }

    public ChainedBooleanModel AndNot(Object parentModel, String expression) {
        return And(new PropertyModel<Boolean>(parentModel, expression));
    }

    public ChainedBooleanModel OrNot(IModel<Boolean> rightOperand) {
        return new Or(this, new ChainedBooleanModel(rightOperand).Not());
    }


    public ChainedBooleanModel OrNot(Object parentModel, String expression) {
        return Or(new PropertyModel<Boolean>(parentModel, expression));
    }


    public ChainedBooleanModel Or(IModel<Boolean> rightOperand) {
        return new Or(this, new ChainedBooleanModel(rightOperand));
    }


    public ChainedBooleanModel Or(Object parentModel, String expression) {
        return Or(new PropertyModel<Boolean>(parentModel, expression));
    }

    public ChainedBooleanModel Not() {
        return new Not(this);
    }

    public Boolean evaluate() {
        return operand.getObject();
    }

    @Override
    public Boolean getObject() {
        return this.evaluate();
    }

    private static class And extends ChainedBooleanModel {

        private ChainedBooleanModel leftOperand;
        private ChainedBooleanModel rightOperand;

        public And(ChainedBooleanModel leftOperand, ChainedBooleanModel rightOperand) {
            this.leftOperand = leftOperand;
            this.rightOperand = rightOperand;
        }

        @Override
        public Boolean evaluate() {
            return leftOperand.evaluate() && rightOperand.evaluate();
        }
    }

    private static class Or extends ChainedBooleanModel {

        private ChainedBooleanModel leftOperand;
        private ChainedBooleanModel rightOperand;

        public Or(ChainedBooleanModel leftOperand, ChainedBooleanModel rightOperand) {
            this.leftOperand = leftOperand;
            this.rightOperand = rightOperand;
        }

        @Override
        public Boolean evaluate() {
            return Boolean.TRUE.equals(leftOperand.evaluate())  || Boolean.TRUE.equals(rightOperand.evaluate());
        }
    }

    private class Not extends ChainedBooleanModel {

        private ChainedBooleanModel operand;

        public Not(ChainedBooleanModel operand) {
            this.operand = operand;
        }

        @Override
        public Boolean evaluate() {
            return !operand.evaluate();
        }
    }
}
