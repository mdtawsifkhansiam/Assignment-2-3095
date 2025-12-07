package ca.gbc.goaltrackingservice.controller;

import ca.gbc.goaltrackingservice.model.Goal;
import ca.gbc.goaltrackingservice.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    // INTER-SERVICE COMMUNICATION ENDPOINT
    @GetMapping("/{goalId}/suggested-resources")
    public List<Object> getSuggestedResources(@PathVariable String goalId) {
        return goalService.suggestResourcesForGoal(goalId);
    }

    // REGULAR CRUD ENDPOINTS
    @GetMapping
    public List<Goal> getAllGoals() {
        return goalService.getAllGoals();
    }

    @GetMapping("/{id}")
    public Goal getGoal(@PathVariable String id) {
        return goalService.getGoalById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('student')")
    public Goal createGoal(@RequestBody Goal goal) {
        return goalService.createGoal(goal);
    }

    @PutMapping("/{id}")
    public Goal updateGoal(@PathVariable String id, @RequestBody Goal goal) {
        return goalService.updateGoal(id, goal);
    }

    @DeleteMapping("/{id}")
    public void deleteGoal(@PathVariable String id) {
        goalService.deleteGoal(id);
    }

    @PatchMapping("/{id}/complete")
    public Goal markGoalAsCompleted(@PathVariable String id) {
        return goalService.markAsCompleted(id);
    }

    @GetMapping("/status/{status}")
    public List<Goal> getGoalsByStatus(@PathVariable String status) {
        return goalService.getGoalsByStatus(status);
    }

    @GetMapping("/category/{category}")
    public List<Goal> getGoalsByCategory(@PathVariable String category) {
        return goalService.getGoalsByCategory(category);
    }
}