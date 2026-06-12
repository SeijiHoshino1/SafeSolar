package com.safesolar.alert;

import com.safesolar.shared.BusinessRuleException;
import com.safesolar.shared.NotFoundException;
import com.safesolar.unit.ConsumerUnit;
import com.safesolar.unit.ConsumerUnitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class AlertService {
    private final AlertRepository repository;
    private final ConsumerUnitService unitService;

    public AlertService(AlertRepository repository, ConsumerUnitService unitService) {
        this.repository = repository;
        this.unitService = unitService;
    }

    @Transactional
    public AlertDtos.Response create(AlertDtos.CreateRequest request) {
        ConsumerUnit unit = unitService.require(request.unitId());
        return AlertDtos.Response.from(repository.save(new Alert(unit, request.severity(), request.title().trim(),
                request.message().trim())));
    }

    @Transactional(readOnly = true)
    public List<AlertDtos.Response> prioritizedOpenAlerts() {
        Comparator<Alert> comparator = Comparator
                .comparingInt((Alert a) -> a.getSeverity().priority()).reversed()
                .thenComparing(Alert::getCreatedAt);
        PriorityQueue<Alert> queue = new PriorityQueue<>(comparator);
        queue.addAll(repository.findByStatusIn(List.of(AlertStatus.OPEN, AlertStatus.ACKNOWLEDGED)));
        return java.util.stream.Stream.generate(queue::poll).takeWhile(java.util.Objects::nonNull)
                .map(AlertDtos.Response::from).toList();
    }

    @Transactional
    public AlertDtos.Response updateStatus(Long id, AlertStatus status) {
        Alert alert = repository.findById(id).orElseThrow(() -> new NotFoundException("Alerta nao encontrado."));
        if (status == AlertStatus.OPEN) throw new BusinessRuleException("Nao e permitido reabrir um alerta por este endpoint.");
        if (alert.getStatus() == AlertStatus.RESOLVED) throw new BusinessRuleException("O alerta ja esta resolvido.");
        if (status == AlertStatus.ACKNOWLEDGED) alert.acknowledge();
        if (status == AlertStatus.RESOLVED) alert.resolve();
        return AlertDtos.Response.from(alert);
    }
}
