package ca.gbc.comp3095.eventservice.repository;

import ca.gbc.comp3095.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByLocationIgnoreCase(String location);
    List<Event> findByDateBetween(LocalDateTime start, LocalDateTime end);
}