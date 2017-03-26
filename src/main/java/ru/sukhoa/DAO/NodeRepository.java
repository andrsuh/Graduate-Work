package ru.sukhoa.DAO;

import ru.sukhoa.domain.Node;

import java.util.List;

public interface NodeRepository {

    String getDatasourceName();

    Node findOneByPk(String pk);

    List<Node> getSubtreeInRootOf(String id);

    boolean isNodeDescendantOfAnother(String descendantPk, String ancestorPk);

    List<Node> getChildrenOfNode(String id);
}
