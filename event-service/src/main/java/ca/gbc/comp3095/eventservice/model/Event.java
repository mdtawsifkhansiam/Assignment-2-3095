package ca.gbc.comp3095.eventservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @NotBlank
    private String title;

    @NotBlank
    @Column(length = 2000)
    private String description;

    @NotNull
    private LocalDateTime date;

    @NotBlank
    private String location;

    @NotNull
    @Positive
    private Integer capacity;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> registeredStudents = new HashSet<>();

    @ElementCollection
    private Set<Long> resourceIds = new HashSet<>();
}