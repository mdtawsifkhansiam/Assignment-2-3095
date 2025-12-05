package ca.gbc.comp3095.eventservice.service;

import ca.gbc.comp3095.eventservice.dto.EventRequest;
import ca.gbc.comp3095.eventservice.dto.EventResponse;
import ca.gbc.comp3095.eventservice.dto.ResourceDTO;
import ca.gbc.comp3095.eventservice.model.Event;
import ca.gbc.comp3095.eventservice.repository.EventRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class EventService {

    private static final Logger logger = Logger.getLogger(EventService.class.getName());
    private static final String WELLNESS_SERVICE_CB = "wellnessService";

    private final EventRepository repo;
    private final WebClient webClient;

    public EventService(EventRepository repo, WebClient.Builder webClientBuilder) {
        this.repo = repo;
        this.webClient = webClientBuilder.baseUrl("http://wellness-service:8080").build();
    }

    @CircuitBreaker(name = WELLNESS_SERVICE_CB, fallbackMethod = "fallbackFetchResources")
    public List<ResourceDTO> fetchResourcesForEvent(String topic) {
        logger.info("Calling wellness-service for resources with topic: " + topic);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/resources/category/{category}")
                        .build(topic))
                .retrieve()
                .bodyToFlux(ResourceDTO.class)
                .collectList()
                .block();
    }

    public List<ResourceDTO> fallbackFetchResources(String topic, Exception e) {
        logger.warning("Circuit Breaker triggered! Fallback for topic: " + topic + ", Error: " + e.getMessage());
        return List.of(
                new ResourceDTO(
                        "fallback-1",
                        "Default Wellness Resource",
                        "general",
                        "This is a fallback resource while wellness service is unavailable"
                )
        );
    }

    public EventResponse create(EventRequest r){
        Event e = new Event();
        e.setTitle(r.title());
        e.setDescription(r.description());
        e.setDate(r.date());
        e.setLocation(r.location());
        e.setCapacity(r.capacity());
        if (r.resourceIds()!=null) e.getResourceIds().addAll(r.resourceIds());
        e = repo.save(e);
        return map(e);
    }

    public EventResponse get(Long id){ return map(repo.findById(id).orElseThrow()); }
    public List<EventResponse> list(){ return repo.findAll().stream().map(this::map).toList(); }

    public EventResponse update(Long id, EventRequest r){
        Event e = repo.findById(id).orElseThrow();
        e.setTitle(r.title());
        e.setDescription(r.description());
        e.setDate(r.date());
        e.setLocation(r.location());
        e.setCapacity(r.capacity());
        e.getResourceIds().clear();
        if (r.resourceIds()!=null) e.getResourceIds().addAll(r.resourceIds());
        return map(e);
    }

    public void delete(Long id){ repo.deleteById(id); }

    public List<EventResponse> byLocation(String loc){
        return repo.findByLocationIgnoreCase(loc).stream().map(this::map).toList();
    }

    public List<EventResponse> byDateRange(LocalDateTime start, LocalDateTime end){
        return repo.findByDateBetween(start, end).stream().map(this::map).toList();
    }

    public EventResponse register(Long id, String student){
        Event e = repo.findById(id).orElseThrow();
        if (e.getRegisteredStudents().size() >= e.getCapacity())
            throw new IllegalStateException("Capacity reached");
        e.getRegisteredStudents().add(student);
        return map(e);
    }

    public EventResponse unregister(Long id, String student){
        Event e = repo.findById(id).orElseThrow();
        e.getRegisteredStudents().remove(student);
        return map(e);
    }

    public EventResponse addResource(Long id, Long resourceId){
        Event e = repo.findById(id).orElseThrow();
        e.getResourceIds().add(resourceId);
        return map(e);
    }

    public EventResponse removeResource(Long id, Long resourceId){
        Event e = repo.findById(id).orElseThrow();
        e.getResourceIds().remove(resourceId);
        return map(e);
    }

    private EventResponse map(Event e){
        return new EventResponse(
                e.getEventId(), e.getTitle(), e.getDescription(), e.getDate(),
                e.getLocation(), e.getCapacity(), e.getRegisteredStudents().size(), e.getResourceIds()
        );
    }
}