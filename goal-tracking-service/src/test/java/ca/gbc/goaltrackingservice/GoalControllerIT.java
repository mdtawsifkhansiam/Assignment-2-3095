package ca.gbc.goaltrackingservice;

import ca.gbc.goaltrackingservice.model.Goal;
import ca.gbc.goaltrackingservice.repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class GoalControllerIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GoalRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void testCreateAndGetGoal() throws Exception {
        // Create a goal
        String json = """
        {
          "title": "Test Goal",
          "description": "Test Description",
          "targetDate": "2024-12-31",
          "status": "in-progress",
          "category": "test"
        }
        """;

        // Test POST
        mockMvc.perform(post("/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Goal"));

        // Test GET all
        mockMvc.perform(get("/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Goal"));
    }

    @Test
    void testGetGoalsByStatus() throws Exception {
        // Setup: create a goal first
        repository.save(new Goal(null, "Goal 1", "Desc 1", LocalDate.now(), "in-progress", "cat1"));

        mockMvc.perform(get("/goals/status/in-progress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("in-progress"));
    }

    @Test
    void testGetGoalsByCategory() throws Exception {
        // Setup: create a goal first
        repository.save(new Goal(null, "Goal 1", "Desc 1", LocalDate.now(), "in-progress", "mindfulness"));

        mockMvc.perform(get("/goals/category/mindfulness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("mindfulness"));
    }
}