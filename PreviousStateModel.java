package de.o2.portal.ui.selfcare.product.mobile.tariffoptions.common;

import org.apache.wicket.model.Model;

import java.io.Serializable;

public class PreviousStateModel<T extends Serializable> extends Model<T> {

    private T previousObject;

    public PreviousStateModel() {
    }

    public PreviousStateModel(T object) {
        super(object);
    }

    public T getPreviousObject() {
        return previousObject;
    }

    @Override
    public void setObject(T object) {
        previousObject = getObject();
        super.setObject(object);
    }
}
