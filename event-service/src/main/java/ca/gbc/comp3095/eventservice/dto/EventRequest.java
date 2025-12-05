package ca.gbc.comp3095.eventservice.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

public record EventRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull LocalDateTime date,
        @NotBlank String location,
        @NotNull @Positive Integer capacity,
        Set<Long> resourceIds
) {}