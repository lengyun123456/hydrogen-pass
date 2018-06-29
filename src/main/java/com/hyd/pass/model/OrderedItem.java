package com.hyd.pass.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * (description)
 * created at 2018/2/6
 *
 * @author yidin
 */
public abstract class OrderedItem {

    private long id = System.currentTimeMillis();

    private StringProperty name = new SimpleStringProperty();

    private IntegerProperty order = new SimpleIntegerProperty();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getOrder() {
        return order.get();
    }

    public IntegerProperty orderProperty() {
        return order;
    }

    public void setOrder(int order) {
        this.order.set(order);
    }
}
