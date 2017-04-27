package de.core_concepts.wicket

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class IndexChoiceModel<T> extends AbstractReadOnlyModel<T> {

    public interface IndexChoiceModelDelegate {

        /**
         * Queries the delegate for the model index which should be returned.
         * This method is only called when IndexChoiceModel#getObject() is
         * called so it's save to call any getObject() within the implementation
         * of this method.
         *
         * @return the index for the model which should be used. Must be in
         * bounds of the models array provided, otherwise an
         * IndexOutOfBoundsException is thrown.
         */
        int getModelIndex();
    }

    private final IndexChoiceModelDelegate delegate;
    private final IModel<T>[] models;

    public IndexChoiceModel(IndexChoiceModelDelegate delegate, IModel<T>... models) {
        this.delegate = delegate;
        this.models = models;
    }

    @Override
    public T getObject() {
        int index = delegate.getModelIndex();
        if (index < 0 || index >= models.length) {
            throw new IndexOutOfBoundsException("Model with the provided index does not exist. Provide a valid index.");
        }
        return models[index].getObject();
    }
}
