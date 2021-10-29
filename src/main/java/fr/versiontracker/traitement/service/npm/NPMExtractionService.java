package fr.versiontracker.traitement.service.npm;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.versiontracker.traitement.modele.Dependency;
import fr.versiontracker.traitement.modele.NPMDependency;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@JsonIgnoreProperties
@Service
public class NPMExtractionService {

    public Dependency getVersionWithJackson(String valueUrl, String trackedDependency) throws Exception {

        URL fileURL;
        if (valueUrl.matches("http.*")) {
            fileURL = new URL(valueUrl);
        }else {
            fileURL = new URL("file:///" + valueUrl);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            NPMDependency npmDependency = objectMapper.readValue(fileURL, NPMDependency.class);
            Dependency dependency = new Dependency();
            dependency.setName(trackedDependency);
            dependency.setVersion(findVersionNPM(trackedDependency, npmDependency));
            return dependency;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
    }

    public String findVersionNPM(String trackedDependency, NPMDependency npmDependency) {
        Map<String, String> dependenciesMap = npmDependency.getDependencies();
        String versionNPM = null;

        if (dependenciesMap.containsKey(trackedDependency)) {
            versionNPM = dependenciesMap.get(trackedDependency);

        }else   {
            versionNPM = "n'existe pas";
        }

        return versionNPM;
    }

}


