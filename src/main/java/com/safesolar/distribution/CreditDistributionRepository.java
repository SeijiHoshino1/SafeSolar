package com.safesolar.distribution;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CreditDistributionRepository extends JpaRepository<CreditDistribution, Long> {
    @Override
    @EntityGraph(attributePaths = {"allocations", "allocations.unit"})
    Optional<CreditDistribution> findById(Long id);

    @EntityGraph(attributePaths = {"allocations", "allocations.unit"})
    List<CreditDistribution> findAllByOrderByCreatedAtDesc();
}
