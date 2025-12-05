package ca.gbc.comp3095.eventservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.logging.Logger;

@Service
public class GoalEventConsumer {

    private static final Logger logger = Logger.getLogger(GoalEventConsumer.class.getName());

    @KafkaListener(topics = "goal-completed-topic", groupId = "event-service")
    public void consumeGoalCompletedEvent(Map<String, Object> event) {
        String goalId = (String) event.get("goalId");
        String goalTitle = (String) event.get("goalTitle");
        String category = (String) event.get("category");
        String studentId = (String) event.get("studentId");

        logger.info("Received GoalCompletedEvent - Goal: " + goalTitle + " (ID: " + goalId + ")");
        logger.info("Student: " + studentId + ", Category: " + category);
        logger.info("This could be used to recommend relevant wellness events to student: " + studentId);

        // In a real implementation, you would:
        // 1. Find events matching the goal category
        // 2. Send notifications to the student
        // 3. Track event recommendations
    }
}