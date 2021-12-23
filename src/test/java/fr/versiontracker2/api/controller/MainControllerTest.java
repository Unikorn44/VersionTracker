// https://spring.io/guides/gs/testing-web/
// https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-server-htmlunit
// https://dzone.com/articles/spring-test-thymeleaf-viewspackage fr.versiontracker2.api.controller;

package fr.versiontracker2.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    /*Interface to provide configuration for a web application*/
    @Autowired
    private WebApplicationContext wac;

    /* module de test intégré à Spring pour les modules REST*/
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void indexTest() {
        try {
            this.mockMvc.perform(get("/index"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Ca aurait du passer (allumer RushServer, avant) !");
        }
    }

}
