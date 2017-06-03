package ru.sukhoa.domain;

public enum NodeType {
    GROUP("GROUP"), OTHER("OTHER");

    private final String name;

    NodeType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
