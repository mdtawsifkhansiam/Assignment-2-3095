package ca.gbc.comp3095.eventservice.controller;

import ca.gbc.comp3095.eventservice.dto.EventRequest;
import ca.gbc.comp3095.eventservice.dto.EventResponse;
import ca.gbc.comp3095.eventservice.dto.ResourceDTO;
import ca.gbc.comp3095.eventservice.service.EventService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService svc;

    public EventController(EventService svc) {
        this.svc = svc;
    }

    @PostMapping
    @PreAuthorize("hasRole('staff')")
    public EventResponse create(@RequestBody @Valid EventRequest req){
        return svc.create(req);
    }

    @GetMapping("/{id}")
    public EventResponse get(@PathVariable Long id){
        return svc.get(id);
    }

    @GetMapping
    public List<EventResponse> list(){
        return svc.list();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('staff')")
    public EventResponse update(@PathVariable Long id, @RequestBody @Valid EventRequest req){
        return svc.update(id, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('staff')")
    public void delete(@PathVariable Long id){
        svc.delete(id);
    }

    @GetMapping("/search/location")
    public List<EventResponse> byLocation(@RequestParam String loc){
        return svc.byLocation(loc);
    }

    @GetMapping("/dto")
    public List<ResourceDTO> getResourcesForTopic(@RequestParam String topic) {
        return svc.fetchResourcesForEvent(topic);
    }

    @GetMapping("/search/dates")
    public List<EventResponse> byDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
        return svc.byDateRange(start, end);
    }

    @PostMapping("/{id}/register")
    @PreAuthorize("hasRole('student')")
    public EventResponse register(@PathVariable Long id, @RequestParam String student){
        return svc.register(id, student);
    }

    @PostMapping("/{id}/unregister")
    @PreAuthorize("hasRole('student')")
    public EventResponse unregister(@PathVariable Long id, @RequestParam String student){
        return svc.unregister(id, student);
    }

    @PostMapping("/{id}/resources/{resourceId}")
    @PreAuthorize("hasRole('staff')")
    public EventResponse addResource(@PathVariable Long id, @PathVariable Long resourceId){
        return svc.addResource(id, resourceId);
    }

    @DeleteMapping("/{id}/resources/{resourceId}")
    @PreAuthorize("hasRole('staff')")
    public EventResponse removeResource(@PathVariable Long id, @PathVariable Long resourceId){
        return svc.removeResource(id, resourceId);
    }
}