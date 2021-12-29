package fr.versiontracker2.transverse.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class WebClientConfigurationTest {

    @Autowired
    RestTemplate restTemplate;

    @Test
    void restTemplateTest() {
        assertNotNull(restTemplate);
    }
}
