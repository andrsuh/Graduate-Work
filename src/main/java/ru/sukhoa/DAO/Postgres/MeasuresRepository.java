package ru.sukhoa.DAO.Postgres;


import org.springframework.data.repository.CrudRepository;
import ru.sukhoa.domain.MeasureEntity;

import java.util.UUID;

public interface MeasuresRepository extends CrudRepository<MeasureEntity, UUID> {
}
