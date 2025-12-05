package ca.gbc.comp3095.wellnessservice;

import ca.gbc.comp3095.wellnessservice.model.Resource;
import ca.gbc.comp3095.wellnessservice.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class WellnessResourceControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7.2.3")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        repository.save(new Resource(
                null,
                "Mindfulness Basics",
                "Intro to mindfulness",
                "mental-health",
                "https://example.com"
        ));
    }

    @Test
    void testGetAllResources() throws Exception {
        mockMvc.perform(get("/api/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Mindfulness Basics"));
    }

    @Test
    void testCreateResource() throws Exception {
        String json = """
        {
          "title": "Yoga for Beginners",
          "description": "Gentle yoga intro",
          "category": "fitness",
          "url": "https://yoga.com"
        }
        """;

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Yoga for Beginners"));
    }

    @Test
    void testDeleteResource() throws Exception {
        Resource resource = repository.save(new Resource(
                null,
                "Sleep Hygiene",
                "Better sleep habits",
                "mental-health",
                "https://sleep.com"
        ));

        mockMvc.perform(delete("/api/resources/" + resource.getResourceId()))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchByKeyword() throws Exception {
        mockMvc.perform(get("/api/resources/search")
                        .param("keyword", "Mindfulness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Mindfulness Basics"));
    }

    @Test
    void testFilterByCategory() throws Exception {
        mockMvc.perform(get("/api/resources/category/mental-health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("mental-health"));
    }
}