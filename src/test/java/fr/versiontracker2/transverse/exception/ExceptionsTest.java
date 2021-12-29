package fr.versiontracker2.transverse.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExceptionsTest {

    @Test
    public void nonReadableApplicationConfigurationException() {

        NonReadableApplicationConfigurationException e1 = new NonReadableApplicationConfigurationException();
        assertNotNull(e1);
        NonReadableApplicationConfigurationException e2 = new NonReadableApplicationConfigurationException("Test", new Exception("Test1"));
        assertNotNull(e2);
        assertEquals("Test", e2.getMessage());
        assertEquals("Test1", e2.getCause().getMessage());

        NonReadableApplicationConfigurationException e3 = new NonReadableApplicationConfigurationException("Test3");
        assertNotNull(e3);
        assertEquals("Test3", e3.getMessage());

        NonReadableApplicationConfigurationException e4 = new NonReadableApplicationConfigurationException(new Exception("Test4"));
        assertNotNull(e4);
        assertEquals("Test4", e4.getCause().getMessage());
    }

    @Test
    public void nonReadableDependencyFileException() {
        NonReadableDependencyFileException e1 = new NonReadableDependencyFileException();
        assertNotNull(e1);
        NonReadableDependencyFileException e2 = new NonReadableDependencyFileException("Test", new Exception("Test1"));
        assertNotNull(e2);
        assertEquals("Test", e2.getMessage());
        assertEquals("Test1", e2.getCause().getMessage());

        NonReadableDependencyFileException e3 = new NonReadableDependencyFileException("Test3");
        assertNotNull(e3);
        assertEquals("Test3", e3.getMessage());

        NonReadableDependencyFileException e4 = new NonReadableDependencyFileException(new Exception("Test4"));
        assertNotNull(e4);
        assertEquals("Test4", e4.getCause().getMessage());
    }
}
