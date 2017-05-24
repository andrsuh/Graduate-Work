package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.domain.MeasureEntity;
import ru.sukhoa.service.MeasureService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/measures")
public class MeasureController {

    private MeasureService measureService;

    @Autowired
    public void setMeasureService(MeasureService measureService) {
        this.measureService = measureService;
    }

    @RequestMapping(value = "/last", method = RequestMethod.GET)
    public MeasureEntity getLastMeasureByEventName(@RequestParam String eventName) {
        return measureService.getLastMeasureEntityByEventName(eventName);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MeasureEntity> getMeasuresByEventNameInRange(
            @RequestParam(value = "event_name") String eventName,
            @RequestParam(value = "from_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
            @RequestParam(value = "to_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate) {
        return measureService.getMeasuresByEventNameInRange(eventName, fromDate, toDate);
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public List<String> getAllEventsNames() {
        return measureService.getAllEventsNames();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<MeasureEntity> getAllMeasuresInRange(
            @RequestParam(value = "from_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
            @RequestParam(value = "to_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate) {
        return measureService.getAllMeasuresInRange(fromDate, toDate);
    }

    @RequestMapping(value = "/alllast", method = RequestMethod.GET)
    public List<MeasureEntity> getAllLastMeasures() {
        return measureService.getAllLastMeasures();
    }

}
