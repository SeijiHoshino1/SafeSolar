package com.safesolar.unit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConsumerUnitRepository extends JpaRepository<ConsumerUnit, Long> {
    Optional<ConsumerUnit> findByCodeIgnoreCase(String code);
    boolean existsByCodeIgnoreCase(String code);
    List<ConsumerUnit> findByActiveTrueOrderByCodeAsc();
}
