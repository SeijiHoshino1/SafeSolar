package com.safesolar.reading;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/readings")
public class EnergyReadingController {
    private final EnergyReadingService service;
    public EnergyReadingController(EnergyReadingService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReadingDtos.Response create(@Valid @RequestBody ReadingDtos.CreateRequest request) { return service.create(request); }

    @GetMapping
    public List<ReadingDtos.Response> history(@RequestParam Long unitId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
        return service.history(unitId, start, end);
    }
}
