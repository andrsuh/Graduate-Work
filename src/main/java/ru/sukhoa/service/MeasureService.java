package ru.sukhoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sukhoa.DAO.Postgres.MeasuresRepository;
import ru.sukhoa.domain.MeasureEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MeasureService {
    private MeasuresRepository repository;
    private Map<MeasureEvent, Measurer> measurers = new ConcurrentHashMap<>();

    @Autowired
    public MeasureService(MeasuresRepository repository, @Value("${cleanMeasures}") boolean cleanMeasures) {
        this.repository = repository;

        for (MeasureEvent event : MeasureEvent.values()) {
            measurers.put(event, new Measurer());
        }

        if (cleanMeasures) {
            cleanMeasures();
        }
    }

    public void cleanMeasures() {
        Arrays.stream(MeasureEvent.values())
                .map(this::getAproppriateMeasurer)
                .forEach(Measurer::reset);
        repository.deleteAll();
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

    public MeasureEntity getLastMeasureEntityByEventName(String eventName) {
        MeasureEvent event = MeasureEvent.valueOf(eventName);
        return repository.getLastEntityByEventName(event.name());
    }

    @Scheduled(fixedDelayString = "${measureFrequency}")
    private void persistMeasures() {
        repository.save(getAllCurrentMeasures());
    }

    @Nonnull
    private List<MeasureEntity> getAllCurrentMeasures() {
        Date current = Calendar.getInstance().getTime();
        return Arrays.stream(MeasureEvent.values())
                .map(event -> new MeasureEntity(event, getNumberOfOperations(event), getTotalTime(event), current))
                .collect(Collectors.toList());
    }

    public List<MeasureEntity> getAllMeasuresInRange(Date fromDate, Date toDate) {
        if (fromDate == null && toDate == null) {
            List<MeasureEntity> entities = new ArrayList<>();
            repository.findAll().forEach(entities::add);
            return entities;
        } else if (fromDate == null) {
            return repository.findByTimestampLessThan(toDate);
        } else if (toDate == null) {
            return repository.findByTimestampGreaterThanEqual(fromDate);
        }

        return repository.findByTimestampBetween(fromDate, toDate);
    }

    public List<String> getAllEventsNames() {
        return Arrays.stream(MeasureEvent.values())
                .map(MeasureEvent::toString)
                .collect(Collectors.toList());
    }

    public List<MeasureEntity> getMeasuresByEventNameInRange(String eventName, @Nullable Date fromDate, @Nullable Date toDate) {
        MeasureEvent event = MeasureEvent.valueOf(eventName);
        if (fromDate == null && toDate == null) {
            return repository.findByEvent(event);
        } else if (fromDate == null) {
            return repository.findByEventAndTimestampLessThan(event, toDate);
        } else if (toDate == null) {
            return repository.findByEventAndTimestampGreaterThanEqual(event, fromDate);
        }

        return repository.findByEventAndTimestampBetween(event, fromDate, toDate);
    }

    public List<MeasureEntity> getAllLastMeasures() {
        return repository.getLastEntities();
    }

    public enum MeasureEvent {
        POSTGRES_PERSIST,
        POSTGRES_SUBTREE_FETCH,
        POSTGRES_CHECK_DESCENDANT,
        POSTGRES_FIND_NODE_BY_NAME,
        POSTGRES_FIND_NODE,
        POSTGRES_GET_CHILDREN,
        POSTGRES_UPDATE,
        NEO_PERSIST,
        NEO_SUBTREE_FETCH,
        NEO_CHECK_DESCENDANT,
        NEO_FIND_NODE_BY_NAME,
        NEO_FIND_NODE,
        NEO_GET_CHILDREN,
        NEO_UPDATE
    }
}