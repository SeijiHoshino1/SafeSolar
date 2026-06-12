package com.safesolar.reading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EnergyReadingRepository extends JpaRepository<EnergyReading, Long> {
    List<EnergyReading> findByUnitIdAndRecordedAtBetweenOrderByRecordedAtAsc(Long unitId, Instant start, Instant end);
    Optional<EnergyReading> findFirstByUnitIdAndTypeOrderByRecordedAtDesc(Long unitId, ReadingType type);

    @Query("""
        select coalesce(sum(r.energyKwh), 0) from EnergyReading r
        where r.unit.id = :unitId and r.type = :type and r.recordedAt between :start and :end
        """)
    BigDecimal sumByUnitAndTypeAndPeriod(@Param("unitId") Long unitId, @Param("type") ReadingType type,
                                         @Param("start") Instant start, @Param("end") Instant end);
}
