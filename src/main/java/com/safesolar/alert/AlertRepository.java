package com.safesolar.alert;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    @EntityGraph(attributePaths = "unit")
    List<Alert> findByStatusIn(List<AlertStatus> statuses);
}
