package com.safesolar.report;

import com.safesolar.reading.EnergyReadingRepository;
import com.safesolar.reading.ReadingType;
import com.safesolar.shared.BusinessRuleException;
import com.safesolar.unit.ConsumerUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportService {
    private final ConsumerUnitRepository units;
    private final EnergyReadingRepository readings;

    public ReportService(ConsumerUnitRepository units, EnergyReadingRepository readings) {
        this.units = units;
        this.readings = readings;
    }

    @Transactional(readOnly = true)
    public List<EfficiencyReport> efficiency(LocalDate startDate, LocalDate endDate, String sortBy) {
        if (startDate.isAfter(endDate)) throw new BusinessRuleException("O inicio do periodo deve ser anterior ao fim.");
        var start = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        var end = endDate.plusDays(1).atStartOfDay().minusNanos(1).toInstant(ZoneOffset.UTC);
        List<EfficiencyReport> result = new ArrayList<>(units.findByActiveTrueOrderByCodeAsc().stream().map(unit -> {
            BigDecimal generation = readings.sumByUnitAndTypeAndPeriod(unit.getId(), ReadingType.GENERATION, start, end);
            BigDecimal consumption = readings.sumByUnitAndTypeAndPeriod(unit.getId(), ReadingType.CONSUMPTION, start, end);
            return new EfficiencyReport(unit.getId(), unit.getCode(), generation, consumption,
                    generation.subtract(consumption));
        }).toList());
        Comparator<EfficiencyReport> comparator = switch (sortBy.toLowerCase()) {
            case "generation" -> Comparator.comparing(EfficiencyReport::generationKwh).reversed();
            case "consumption" -> Comparator.comparing(EfficiencyReport::consumptionKwh).reversed();
            case "balance" -> Comparator.comparing(EfficiencyReport::balanceKwh).reversed();
            default -> throw new BusinessRuleException("Ordenacao invalida. Use generation, consumption ou balance.");
        };
        QuickSort.sort(result, comparator);
        return result;
    }
}
