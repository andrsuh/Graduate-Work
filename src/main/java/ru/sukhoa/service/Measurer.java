package ru.sukhoa.service;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Measurer {
    private AtomicLong totalTime = new AtomicLong();
    private AtomicLong numOfOperations = new AtomicLong();

    private Map<UUID, Long> measures = new ConcurrentHashMap<>();

    public UUID startMeasure() {
        UUID measureID;
        do {
            measureID = UUID.randomUUID();
        }
        while (measures.putIfAbsent(measureID, Calendar.getInstance().getTimeInMillis()) != null); // to avoid collision

        return measureID;
    }

    public void fixMeasure(@Nullable UUID measureId) {
        long currentTime = Calendar.getInstance().getTimeInMillis();

        if (measureId == null) {
            throw new IllegalArgumentException("MeasureUUID must not be a null");
        }

        Long measureStartTime = measures.get(measureId);
        if (measureStartTime == null) {
            throw new IllegalArgumentException("Measure with specifies uuid does not exist");
        }
        measures.remove(measureId);

        long measureDuration = currentTime - measureStartTime;

        long currentTotalTime = totalTime.get();
        while (!totalTime.compareAndSet(currentTotalTime, currentTotalTime + measureDuration)) {
            currentTotalTime = totalTime.get();
        }
        numOfOperations.incrementAndGet();
    }

    public long getTotalTime() {
        return totalTime.get();
    }

    public long getNumberOfOperations() {
        return numOfOperations.get();
    }
}
