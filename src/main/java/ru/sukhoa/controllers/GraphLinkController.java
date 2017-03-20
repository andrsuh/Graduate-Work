package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.service.GraphLinkService;

@RestController
@RequestMapping("/link")
public class GraphLinkController {
    private GraphLinkService graphLinkService;

    @Autowired
    public void setGraphLinkService(GraphLinkService graphLinkService) {
        this.graphLinkService = graphLinkService;
    }

    @RequestMapping(value = "{child_pk}/to/{parent_pk}", method = RequestMethod.POST)
    public void linkNodes(@PathVariable("child_pk") String childPk, @PathVariable("parent_pk") String parentPk) {
        graphLinkService.linkNodes(childPk, parentPk);
    }

}
