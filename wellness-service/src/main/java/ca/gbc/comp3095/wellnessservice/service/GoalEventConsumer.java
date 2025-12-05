package ca.gbc.comp3095.wellnessservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.logging.Logger;

@Service
public class GoalEventConsumer {

    private static final Logger logger = Logger.getLogger(GoalEventConsumer.class.getName());

    @KafkaListener(topics = "goal-completed-topic", groupId = "wellness-service")
    public void consumeGoalCompletedEvent(Map<String, Object> event) {
        String goalId = (String) event.get("goalId");
        String goalTitle = (String) event.get("goalTitle");
        String category = (String) event.get("category");

        logger.info("Received GoalCompletedEvent - Goal: " + goalTitle + " (ID: " + goalId + "), Category: " + category);
        logger.info("This could be used to track resource popularity for category: " + category);
    }
}