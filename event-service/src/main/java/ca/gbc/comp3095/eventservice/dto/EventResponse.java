package ca.gbc.comp3095.eventservice.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record EventResponse(
        Long eventId,
        String title,
        String description,
        LocalDateTime date,
        String location,
        Integer capacity,
        int registeredCount,
        Set<Long> resourceIds
) {}