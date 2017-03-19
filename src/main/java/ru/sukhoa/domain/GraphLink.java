package ru.sukhoa.domain;

import javax.persistence.*;

@Entity(name = "graph_link")
public class GraphLink {

    private Integer id;
    private String leftNodePk;
    private String rightNodePk;

    public GraphLink(String leftNodePk, String rightNodePk) {
        this.leftNodePk = leftNodePk;
        this.rightNodePk = rightNodePk;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "left_node")
    public String getLeftNodePk() {
        return leftNodePk;
    }

    public void setLeftNodePk(String leftNodePk) {
        this.leftNodePk = leftNodePk;
    }

    @Column(name = "right_node")
    public String getRightNodePk() {
        return rightNodePk;
    }

    public void setRightNodePk(String rightNodePk) {
        this.rightNodePk = rightNodePk;
    }
}
