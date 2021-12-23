package fr.versiontracker2.traitement.service.npm;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.versiontracker2.traitement.modele.Dependency;
import fr.versiontracker2.traitement.modele.NPMDependency;
import fr.versiontracker2.transverse.exception.NonReadableDependencyFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@JsonIgnoreProperties
@Service
@Slf4j
public class NPMExtractionService {

    public static final String URL_DES_DEPENDANCES_INVALIDES = "Url des dépendances invalides";
    public static final String IMPOSSIBLE_DE_LIRE_LES_DEPENDANCES = "Impossible de lire les dépendances";

    public Dependency getVersionWithJackson(String valueUrl, String trackedDependency) throws NonReadableDependencyFileException {

        URL fileURL;
        try {
            if (valueUrl.matches("http.*")) {
                fileURL = new URL(valueUrl);
            } else {
                fileURL = new URL("file:///" + valueUrl);
            }
        } catch (MalformedURLException e) {
            log.error(URL_DES_DEPENDANCES_INVALIDES, e);
            throw new NonReadableDependencyFileException(URL_DES_DEPENDANCES_INVALIDES, e);
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
            log.error(IMPOSSIBLE_DE_LIRE_LES_DEPENDANCES, e);
            throw new NonReadableDependencyFileException(IMPOSSIBLE_DE_LIRE_LES_DEPENDANCES, e);
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


