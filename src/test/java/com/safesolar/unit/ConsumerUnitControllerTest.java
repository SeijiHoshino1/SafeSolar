package com.safesolar.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsumerUnitController.class)
class ConsumerUnitControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ConsumerUnitService service;

    @Test
    void listsUnitsUsingTheDocumentedContract() throws Exception {
        when(service.list()).thenReturn(List.of(new UnitDtos.Response(1L, "AP-101", "Apartamento 101",
                "Ana", UnitType.APARTMENT, new BigDecimal("0.2500"), true, Instant.parse("2026-06-12T10:00:00Z"))));

        mockMvc.perform(get("/api/v1/units"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].code").value("AP-101"))
                .andExpect(jsonPath("$[0].fixedShare").value(0.25));
    }

    @Test
    void rejectsInvalidUnitBeforeCallingBusinessLayer() throws Exception {
        mockMvc.perform(post("/api/v1/units").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code":"","name":"","ownerName":"","type":"APARTMENT","fixedShare":0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dados de entrada invalidos."))
                .andExpect(jsonPath("$.validationErrors.code").exists());
    }
}
