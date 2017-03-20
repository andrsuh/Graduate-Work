package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.domain.MeasureEntity;
import ru.sukhoa.service.MeasureService;

@RestController()
@RequestMapping("/measure")
public class MeasureController {

    private MeasureService measureService;

    @Autowired
    public void setMeasureService(MeasureService measureService) {
        this.measureService = measureService;
    }

    @RequestMapping(value = "/subtree/postgres", method = RequestMethod.GET)
    public MeasureEntity measurePostgresSubtreeFetching() {
        return measureService.getMeasureEntityByEvent(MeasureService.MeasureEvent.POSTGRES_SUBTREE_FETCH);
    }

    @RequestMapping(value = "/subtree/neo", method = RequestMethod.GET)
    public MeasureEntity measureNeoSubtreeFetching() {
        return measureService.getMeasureEntityByEvent(MeasureService.MeasureEvent.NEO_SUBTREE_FETCH);
    }

    @RequestMapping(value = "/persist/postgres", method = RequestMethod.GET)
    public MeasureEntity measurePostgresPersist() {
        return measureService.getMeasureEntityByEvent(MeasureService.MeasureEvent.POSTGRES_PERSIST);
    }

    @RequestMapping(value = "/persist/neo", method = RequestMethod.GET)
    public MeasureEntity measureNeoPersist() {
        return measureService.getMeasureEntityByEvent(MeasureService.MeasureEvent.NEO_PERSIST);
    }
}
