package ru.sukhoa.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.UUID;

@Entity
@Table(name = "attribute_value")
public abstract class AttributeValue {
    private String id = UUID.randomUUID().toString();

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Transient
    public abstract Object getValue();

    @Transient
    public abstract void setValue(Object value);
}
