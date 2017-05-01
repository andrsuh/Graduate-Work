package ru.sukhoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sukhoa.DAO.Postgres.MeasuresRepository;
import ru.sukhoa.domain.MeasureEntity;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MeasureService {
    private MeasuresRepository repository;
    private Map<MeasureEvent, Measurer> measurers = new ConcurrentHashMap<>();

    public MeasureService() {
        for (MeasureEvent event : MeasureEvent.values()) {
            measurers.put(event, new Measurer());
        }
    }

    @Autowired
    public void setRepository(MeasuresRepository repository) {
        this.repository = repository;
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

    private Measurer getAproppriateMeasurer(@Nullable MeasureEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Measure event must not be a null");
        }

        Measurer measurer = measurers.get(event);
        if (measurer == null) {
            throw new IllegalArgumentException("Unsupportable measure event");
        }

        return measurer;
    }

    public MeasureEntity getMeasureEntityByEvent(@Nullable MeasureEvent event) {
        return new MeasureEntity(event, getNumberOfOperations(event), getTotalTime(event), null);
    }

    @Scheduled(fixedDelayString = "${measureFrequency}")
    private void persistMeasures() {
        Date current = Calendar.getInstance().getTime();
        Arrays.stream(MeasureEvent.values())
                .map(event -> new MeasureEntity(event, getNumberOfOperations(event), getTotalTime(event), current))
                .forEach(repository::save);
    }

    public List<MeasureEntity> getAllStatistics() {
        return Arrays.stream(MeasureEvent.values())
                .map(event -> new MeasureEntity(event, getNumberOfOperations(event), getTotalTime(event), null))
                .collect(Collectors.toList());
    }

    public enum MeasureEvent {
        POSTGRES_PERSIST,
        POSTGRES_SUBTREE_FETCH,
        POSTGRES_CHECK_DESCENDANT,
        POSTGRES_FIND_NODE,
        POSTGRES_GET_CHILDREN,
        POSTGRES_UPDATE,
        NEO_PERSIST,
        NEO_SUBTREE_FETCH,
        NEO_CHECK_DESCENDANT,
        NEO_FIND_NODE,
        NEO_GET_CHILDREN,
        NEO_UPDATE
    }
}