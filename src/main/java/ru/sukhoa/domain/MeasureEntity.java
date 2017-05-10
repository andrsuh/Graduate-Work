package ru.sukhoa.domain;

import org.neo4j.ogm.annotation.*;
import ru.sukhoa.service.MeasureService;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "measures")
@org.neo4j.ogm.annotation.Transient
public class MeasureEntity {
    private UUID id = UUID.randomUUID();

    private MeasureService.MeasureEvent event;

    private long numOfOperation;

    private long totalTime;

    private Date timestamp;

    public MeasureEntity() {
    }

    public MeasureEntity(MeasureService.MeasureEvent event, long numOfOperation, long totalTime, Date timestamp) {
        this.event = event;
        this.numOfOperation = numOfOperation;
        this.totalTime = totalTime;
        this.timestamp = timestamp;
    }

    @Id
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Column(name = "event")
    @Enumerated(value = EnumType.STRING)
    public MeasureService.MeasureEvent getEvent() {
        return event;
    }

    public void setEvent(MeasureService.MeasureEvent event) {
        this.event = event;
    }

    @Column(name = "times")
    public long getNumOfOperation() {
        return numOfOperation;
    }

    public void setNumOfOperation(long numOfOperation) {
        this.numOfOperation = numOfOperation;
    }

    @Column(name = "total_time")
    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    @Column(name = "time")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
