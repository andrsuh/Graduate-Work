package ru.sukhoa.domain;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "graph_link")
@RelationshipEntity(type = "PART_OF")
public class GraphLink {

    private String pk = UUID.randomUUID().toString();

    @GraphId
    private Long id;

    @StartNode
    private Node leftNodePk;

    @EndNode
    private Node rightNodePk;

    // default constructor is required by neo4j graph mapping
    public GraphLink() {
    }

    public GraphLink(Node leftNodePk, Node rightNodePk) {
        this.leftNodePk = leftNodePk;
        this.rightNodePk = rightNodePk;
    }

    @Transient
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @Column(name = "id")
    @org.neo4j.ogm.annotation.Transient
    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "left_node", referencedColumnName = "id")
    public Node getLeftNodePk() {
        return leftNodePk;
    }

    public void setLeftNodePk(Node leftNodePk) {
        this.leftNodePk = leftNodePk;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "right_node", referencedColumnName = "id")
    public Node getRightNodePk() {
        return rightNodePk;
    }

    public void setRightNodePk(Node rightNodePk) {
        this.rightNodePk = rightNodePk;
    }

}
