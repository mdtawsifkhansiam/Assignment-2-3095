package ca.gbc.goaltrackingservice.service;

import ca.gbc.goaltrackingservice.dto.GoalCompletedEvent;
import ca.gbc.goaltrackingservice.model.Goal;
import ca.gbc.goaltrackingservice.repository.GoalRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class GoalService {

    private static final Logger logger = Logger.getLogger(GoalService.class.getName());
    private static final String WELLNESS_SERVICE_CB = "wellnessService";

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${wellness.service.url:http://wellness-service:8080}")
    private String wellnessServiceBaseUrl;

    @CircuitBreaker(name = WELLNESS_SERVICE_CB, fallbackMethod = "fallbackSuggestResources")
    public List<Object> suggestResourcesForGoal(String goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " + goalId));

        String goalCategory = goal.getCategory();
        String wellnessServiceUrl = wellnessServiceBaseUrl + "/resources/category/" + goalCategory;

        logger.info("Calling Wellness Service: " + wellnessServiceUrl);

        ResponseEntity<List<Object>> response = restTemplate.exchange(
                wellnessServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Object>>() {}
        );

        return response.getBody();
    }

    public List<Object> fallbackSuggestResources(String goalId, Exception e) {
        logger.warning("Circuit Breaker triggered! Fallback for goal: " + goalId + ", Error: " + e.getMessage());
        return List.of(
                Map.of(
                        "title", "Default Wellness Resource",
                        "description", "This is a fallback resource while wellness service is unavailable",
                        "category", "general",
                        "fallback", true
                )
        );
    }

    public List<Goal> getAllGoals() {
        return goalRepository.findAll();
    }

    public Goal getGoalById(String id) {
        return goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
    }

    public Goal createGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    public Goal updateGoal(String id, Goal goalDetails) {
        Goal goal = getGoalById(id);
        goal.setTitle(goalDetails.getTitle());
        goal.setDescription(goalDetails.getDescription());
        goal.setTargetDate(goalDetails.getTargetDate());
        goal.setStatus(goalDetails.getStatus());
        goal.setCategory(goalDetails.getCategory());
        return goalRepository.save(goal);
    }

    public void deleteGoal(String id) {
        goalRepository.deleteById(id);
    }

    public Goal markAsCompleted(String id) {
        Goal goal = getGoalById(id);
        goal.setStatus("completed");
        Goal savedGoal = goalRepository.save(goal);

        GoalCompletedEvent event = new GoalCompletedEvent(
                savedGoal.getId(),
                "student123",
                savedGoal.getTitle(),
                savedGoal.getCategory(),
                LocalDateTime.now()
        );

        kafkaTemplate.send("goal-completed-topic", event);
        logger.info("Published GoalCompletedEvent for goal: " + savedGoal.getId());

        return savedGoal;
    }

    public List<Goal> getGoalsByStatus(String status) {
        return goalRepository.findByStatus(status);
    }

    public List<Goal> getGoalsByCategory(String category) {
        return goalRepository.findByCategory(category);
    }
}