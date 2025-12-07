package ca.gbc.goaltrackingservice;

import ca.gbc.goaltrackingservice.model.Goal;
import ca.gbc.goaltrackingservice.repository.GoalRepository;
import ca.gbc.goaltrackingservice.service.GoalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class GoalServiceFallbackTest {

    @Autowired
    private GoalService goalService;

    @MockBean
    private GoalRepository goalRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testSuggestResourcesFallback() {
        String goalId = "test-goal-id";
        Goal mockGoal = new Goal();
        mockGoal.setId(goalId);
        mockGoal.setCategory("fitness");

        Mockito.when(goalRepository.findById(goalId)).thenReturn(Optional.of(mockGoal));

        Mockito.when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                any(org.springframework.core.ParameterizedTypeReference.class)
        )).thenThrow(new RestClientException("Service Unavailable"));

        List<Object> result = goalService.suggestResourcesForGoal(goalId);

        assertEquals(1, result.size());

        @SuppressWarnings("unchecked")
        Map<String, Object> fallbackItem = (Map<String, Object>) result.get(0);
        assertTrue((Boolean) fallbackItem.get("fallback"));
    }
}