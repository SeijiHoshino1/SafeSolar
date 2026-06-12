package com.safesolar.alert;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {
    private final AlertService service;
    public AlertController(AlertService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlertDtos.Response create(@Valid @RequestBody AlertDtos.CreateRequest request) { return service.create(request); }

    @GetMapping
    public List<AlertDtos.Response> prioritized() { return service.prioritizedOpenAlerts(); }

    @PatchMapping("/{id}/status")
    public AlertDtos.Response status(@PathVariable Long id, @RequestParam AlertStatus status) {
        return service.updateStatus(id, status);
    }
}
