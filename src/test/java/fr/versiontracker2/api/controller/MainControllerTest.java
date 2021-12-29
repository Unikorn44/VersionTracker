// https://spring.io/guides/gs/testing-web/
// https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-server-htmlunit
// https://dzone.com/articles/spring-test-thymeleaf-viewspackage fr.versiontracker2.api.controller;

package fr.versiontracker2.api.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    /**
     * Injection de dépendance du contexte Spring pour une application web
     */
    @Autowired
    private WebApplicationContext wac;

    /**
     *  Injection de dépendance du module de test intégré à Spring pour les modules Spring MVC
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Test de la page index
     */
    @Test
    public void indexTest() {
        try {
            MvcResult result = this.mockMvc.perform(get("/index"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"))
                    .andReturn();
            assertTrue(result.getResponse().getContentAsString().contains("Bienvenue"));
            assertTrue(result.getResponse().getContentAsString().contains("XMLRush1.json"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Ca aurait du passer (allumer RushServer, avant) !");
        }
    }
}
