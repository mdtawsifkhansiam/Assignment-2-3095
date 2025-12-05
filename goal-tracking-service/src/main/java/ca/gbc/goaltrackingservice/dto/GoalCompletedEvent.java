package ca.gbc.goaltrackingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalCompletedEvent {
    private String goalId;
    private String studentId;
    private String goalTitle;
    private String category;
    private LocalDateTime completedAt;
}