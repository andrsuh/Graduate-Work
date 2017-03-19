package ru.sukhoa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@NodeEntity(label = "node")
@Table(name = "node")
public class Node {

    private String pk = UUID.randomUUID().toString();

    private Long id;

    private String name;

    //    @JsonProperty(value = "partOf")
    private Node partOf;
    private String type;

    @JsonIgnore
    private Set<AssignedAttribute> attributes;

    public Node(String name, Node partOfNode, Set<AssignedAttribute> attributes) {
        this.name = name;
        this.partOf = partOfNode;
        this.attributes = attributes;
    }

    public Node() {
    }

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR (40)")
    @Property(name = "pk")
    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    @Transient
    @GraphId(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "type")
    @Property(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Property(name = "name")
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_of", referencedColumnName = "id")
    @Relationship(type = "PART_OF", direction = Relationship.OUTGOING)
    public Node getPartOf() {
        return partOf;
    }

    public void setPartOf(Node partOf) {
        this.partOf = partOf;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "node_id", referencedColumnName = "id")
    @Relationship(type = "MARKED_AS", direction = Relationship.OUTGOING)
    public Set<AssignedAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<AssignedAttribute> attributes) {
        this.attributes = attributes;
    }
}
