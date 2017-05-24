package ru.sukhoa.DAO.Postgres;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.sukhoa.domain.MeasureEntity;
import ru.sukhoa.service.MeasureService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
public interface MeasuresRepository extends CrudRepository<MeasureEntity, UUID> {
    @Query(value = "select * from measures where time = (select max(time) from measures) and event = ?;", nativeQuery = true)
    MeasureEntity getLastEntityByEventName(String eventName);

    @Query(value = "select * from measures where time = (select max(time) from measures);", nativeQuery = true)
    List<MeasureEntity> getLastEntities();

    List<MeasureEntity> findByEventAndTimestampBetween(MeasureService.MeasureEvent event, Date from, Date to);

    List<MeasureEntity> findByEventAndTimestampGreaterThanEqual(MeasureService.MeasureEvent event, Date from);

    List<MeasureEntity> findByEventAndTimestampLessThan(MeasureService.MeasureEvent event, Date to);

    List<MeasureEntity> findByTimestampBetween(Date from, Date to);

    List<MeasureEntity> findByTimestampGreaterThanEqual(Date from);

    List<MeasureEntity> findByTimestampLessThan(Date to);

    List<MeasureEntity> findByEvent(MeasureService.MeasureEvent event);
}

