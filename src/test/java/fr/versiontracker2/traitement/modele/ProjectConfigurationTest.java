package fr.versiontracker2.traitement.modele;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProjectConfigurationTest {

    @Test
    public void gettersAndSettersTest() {

        List<String> trackedDependencies = new ArrayList<>();
        trackedDependencies.add("td1");
        trackedDependencies.add("td2");

        List<Dependency> dependencies = new ArrayList<>();
        Dependency d1 = new Dependency();
        dependencies.add(d1);
        Dependency d2 = new Dependency();
        dependencies.add(d2);

        ProjectConfiguration pc = new ProjectConfiguration();
        pc.setName("Name");
        pc.setFileUrl("FileUrl");
        pc.setPackageManager("PackageManager");
        pc.setDependencies(dependencies);
        pc.setTrackedDependencies(trackedDependencies);

        assertEquals(trackedDependencies, pc.getTrackedDependencies());
        assertEquals(dependencies, pc.getDependencies());
        assertEquals("Name", pc.getName());
        assertEquals("FileUrl", pc.getFileUrl());
        assertEquals("PackageManager", pc.getPackageManager());
    }
}
