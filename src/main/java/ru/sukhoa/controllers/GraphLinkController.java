package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.service.GraphLinkService;

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
}
