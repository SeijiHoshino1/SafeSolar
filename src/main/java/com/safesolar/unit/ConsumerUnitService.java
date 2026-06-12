package com.safesolar.unit;

import com.safesolar.shared.BusinessRuleException;
import com.safesolar.shared.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ConsumerUnitService {
    private final ConsumerUnitRepository repository;

    public ConsumerUnitService(ConsumerUnitRepository repository) { this.repository = repository; }

    @Transactional
    public UnitDtos.Response create(UnitDtos.CreateRequest request) {
        if (repository.existsByCodeIgnoreCase(request.code())) {
            throw new BusinessRuleException("Ja existe uma unidade com o codigo informado.");
        }
        ConsumerUnit unit = new ConsumerUnit(request.code().trim().toUpperCase(), request.name().trim(),
                request.ownerName().trim(), request.type(), request.fixedShare());
        return UnitDtos.Response.from(repository.save(unit));
    }

    @Transactional(readOnly = true)
    public List<UnitDtos.Response> list() {
        return repository.findAll().stream().map(UnitDtos.Response::from).toList();
    }

    @Transactional(readOnly = true)
    public ConsumerUnit require(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Unidade consumidora nao encontrada."));
    }
}
