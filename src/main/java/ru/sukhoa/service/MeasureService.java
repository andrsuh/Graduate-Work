package ru.sukhoa.service;

import com.sun.istack.internal.Nullable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MeasureService {
    private Map<MeasureEvent, BaseMeasurer> measurers = new ConcurrentHashMap<>();

    public MeasureService() {
        for (MeasureEvent event : MeasureEvent.values()) {
            measurers.put(event, new BaseMeasurer());
        }
    }

    public UUID startMeasure(@Nullable MeasureEvent event) {
        return getAproppriateMeasurer(event).startMeasure();
    }

    public void fixMeasure(MeasureEvent event, @Nullable UUID measureId) {
        getAproppriateMeasurer(event).fixMeasure(measureId);
    }

    public long getTotalTime(@Nullable MeasureEvent event) {
        return getAproppriateMeasurer(event).getTotalTime();
    }

    public long getNumberOfOperations(@Nullable MeasureEvent event) {
        return getAproppriateMeasurer(event).getNumberOfOperations();
    }

    private BaseMeasurer getAproppriateMeasurer(@Nullable MeasureEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Measure event must not be a null");
        }

        BaseMeasurer measurer = measurers.get(event);
        if (measurer == null) {
            throw new IllegalArgumentException("Unsupportable measure event");
        }

        return measurer;
    }

    public enum MeasureEvent {
        POSTGRES_PERSIST, NEO_PERSIST
    }
}