package ca.gbc.goaltrackingservice;

import ca.gbc.goaltrackingservice.service.GoalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Testcontainers
public class GoalKafkaIT {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("schema.registry.url", () -> "http://mock-registry:8081");
    }

    @Autowired
    private GoalService goalService;

    @Test
    void testKafkaContainerStartup() {
        assertDoesNotThrow(() -> {
            System.out.println("Kafka TestContainer is running on: " + kafka.getBootstrapServers());
        });
    }
}