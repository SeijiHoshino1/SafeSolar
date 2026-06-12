package com.safesolar.distribution;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/distributions")
public class CreditDistributionController {
    private final CreditDistributionService service;
    public CreditDistributionController(CreditDistributionService service) { this.service = service; }

    @PostMapping("/calculate")
    @ResponseStatus(HttpStatus.CREATED)
    public DistributionDtos.Response calculate(@Valid @RequestBody DistributionDtos.CalculateRequest request) {
        return service.calculate(request);
    }

    @PatchMapping("/{id}/confirm")
    public DistributionDtos.Response confirm(@PathVariable Long id) { return service.confirm(id); }

    @GetMapping
    public List<DistributionDtos.Response> list() { return service.list(); }
}
