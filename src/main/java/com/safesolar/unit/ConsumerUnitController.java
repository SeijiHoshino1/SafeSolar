package com.safesolar.unit;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/units")
public class ConsumerUnitController {
    private final ConsumerUnitService service;
    public ConsumerUnitController(ConsumerUnitService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UnitDtos.Response create(@Valid @RequestBody UnitDtos.CreateRequest request) { return service.create(request); }

    @GetMapping
    public List<UnitDtos.Response> list() { return service.list(); }
}
