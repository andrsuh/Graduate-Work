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

    @JsonIgnore
    private Long id;

    private String name;

    private Set<Node> partOf;

    private String type;

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "graph_link",
            joinColumns = @JoinColumn(name = "left_node"),
            inverseJoinColumns = @JoinColumn(name = "right_node")
    )
    @Relationship(type = "PART_OF", direction = Relationship.OUTGOING)
    public Set<Node> getPartOf() {
        return partOf;
    }

    public void setPartOf(Set<Node> partOf) {
        this.partOf = partOf;
    }

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "node_id", referencedColumnName = "id")
//    @Relationship(type = "MARKED_AS", direction = Relationship.OUTGOING)
//    public Set<AssignedAttribute> getAttributes() {
//        return attributes;
//    }

//    @Relationship(type = "PART_OF", direction = Relationship.OUTGOING)
//    public void setAttributes(Set<AssignedAttribute> attributes) {
//        this.attributes = attributes;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (!pk.equals(node.pk)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pk.hashCode();
    }
}