package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sukhoa.domain.GraphLink;
import ru.sukhoa.service.GraphLinkService;

import java.util.List;

@RestController
@RequestMapping("/links")
public class GraphLinkController {
    private GraphLinkService graphLinkService;

    @Autowired
    public void setGraphLinkService(GraphLinkService graphLinkService) {
        this.graphLinkService = graphLinkService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void linkNodes(@RequestParam("child_pk") String childPk, @RequestParam("parent_pk") String parentPk) {
        graphLinkService.linkNodes(childPk, parentPk);
    }

    @RequestMapping(value = "/postgres", method = RequestMethod.POST)
    public void linkNodesPostgres(@RequestParam("child_pk") String childPk, @RequestParam("parent_pk") String parentPk) {
        graphLinkService.createPostgresGraphLink(childPk, parentPk);
    }

    @RequestMapping(value = "/neo", method = RequestMethod.POST)
    public void linkNodesNeo(@RequestParam("child_pk") String childPk, @RequestParam("parent_pk") String parentPk) {
        graphLinkService.createNeo4jGraphLink(childPk, parentPk);
    }

    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    public void linkNodes(@RequestBody List<GraphLink> links) {
        links.forEach(link -> graphLinkService.linkNodes(link.getLeftNodePk().getPk(), link.getRightNodePk().getPk()));
    }
}
