package ru.sukhoa.domain;

import ru.sukhoa.service.MeasureService;

public class MeasureEntity {
    private MeasureService.MeasureEvent event;
    private long numOfOperation;
    private long totalTime;

    public MeasureEntity(MeasureService.MeasureEvent event, long numOfOperation, long totalTime) {
        this.event = event;
        this.numOfOperation = numOfOperation;
        this.totalTime = totalTime;
    }

    public MeasureService.MeasureEvent getEvent() {
        return event;
    }

    public long getNumOfOperation() {
        return numOfOperation;
    }

    public long getTotalTime() {
        return totalTime;
    }
}
