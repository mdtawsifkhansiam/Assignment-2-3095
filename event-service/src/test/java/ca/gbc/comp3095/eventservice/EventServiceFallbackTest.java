package ca.gbc.comp3095.eventservice;

import ca.gbc.comp3095.eventservice.dto.ResourceDTO;
import ca.gbc.comp3095.eventservice.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventServiceFallbackTest {

    @Autowired
    private EventService eventService;

    @MockBean
    private WebClient.Builder webClientBuilder;

    @Test
    public void testFetchResourcesFallback() {
        Exception e = new WebClientRequestException(new Exception("Service Down"), null, null, null);
        List<ResourceDTO> result = eventService.fallbackFetchResources("wellness", e);

        assertEquals(1, result.size());
        assertEquals("fallback-1", result.get(0).getId());
    }
}