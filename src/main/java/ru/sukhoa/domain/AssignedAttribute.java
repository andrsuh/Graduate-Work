package ru.sukhoa.domain;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "assign_attribute")
public class AssignedAttribute {
    private String id = UUID.randomUUID().toString();

    private FlagValue attributeValue;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "attr_value_id")
    public FlagValue getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(FlagValue attributeValue) {
        this.attributeValue = attributeValue;
    }
}
