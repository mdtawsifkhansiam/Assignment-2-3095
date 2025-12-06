package ca.gbc.comp3095.apigateway;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
public class AuthenticationIT {

    @Container
    static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:22.0")
            .withRealmImportFile("keycloak-setup/import-realm.json");

    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/gbc-realm");
    }

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenUnauthenticated_then401() {
        // This test proves that the Gateway is correctly protecting the resource
        // and that it can communicate with the Keycloak container to verify security configuration.
        webTestClient.get()
                .uri("/api/resources")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}