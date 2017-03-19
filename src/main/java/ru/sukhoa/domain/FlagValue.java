package ru.sukhoa.domain;

import javax.persistence.*;

@Entity
@Table(name = "attribute_value")
public class FlagValue extends AttributeValue {
    private Boolean value;

    @Column(name = "boolean_attr_value")
    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (Boolean) value;
    }
}
