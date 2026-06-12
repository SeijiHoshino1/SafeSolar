package com.safesolar.reading;

public record ReadingCreatedEvent(EnergyReading current, EnergyReading previous) {}
