package fr.versiontracker.traitement.service.maven;

import fr.versiontracker.api.ressource.TrackedDependencyInfo;
import fr.versiontracker.traitement.modele.MavenDependency;
import fr.versiontracker.transverse.exception.NonReadableDependencyFileException;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import java.net.URISyntaxException;
import java.net.URL;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.xml.sax.SAXException;

@Service
@Slf4j
public class MavenExtractionService {

    public static final String N_EXISTE_PAS = "n'existe pas";

    public MavenDependency extractMavenDependencyFrom(String valueTrackedDependency) {
        MavenDependency mavenDependency = new MavenDependency();
        String[] retourSplits = valueTrackedDependency.split("\\|");
        mavenDependency.setGroupId(retourSplits[0]);
        mavenDependency.setArtifactId(retourSplits[1]);
        return mavenDependency;
    }

    public String getVersionWithPP3(String valueUrl, MavenDependency mavenDependency) throws NonReadableDependencyFileException {

        Model fileModel = null;

        if (valueUrl.matches("http.*")) {

            WebClient webClient = WebClient.create();
            String pomContent = webClient.get()
                    .uri(valueUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
            try {
                fileModel = mavenXpp3Reader.read(new StringReader(pomContent));
            } catch (XmlPullParserException | IOException e) {
                log.error("Impossible de lire le pom du projet " + valueUrl,e);
                throw new NonReadableDependencyFileException("Impossible de lire le pom du projet " + valueUrl, e);
            }
        } else {
            MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
            try {
                fileModel = mavenXpp3Reader.read(new FileReader(valueUrl));
            } catch (XmlPullParserException | IOException e) {
                log.error("Impossible de lire le pom du projet " + valueUrl,e);
                throw new NonReadableDependencyFileException("Impossible de lire le pom du projet " + valueUrl, e);
            }
        }
        List<Dependency> dependencyLists = fileModel.getDependencies();
        return retrieveVersion(fileModel, dependencyLists, mavenDependency.getGroupId(), mavenDependency.getArtifactId());
    }

    private String retrieveVersion(Model fileModel, List<Dependency> dependencyLists, String groupId, String artifactId) {

        String versionFound = null;

        for (Dependency dependencyList : dependencyLists) {

            String foundedArtifactId = dependencyList.getArtifactId();
            String foundedGroupId = dependencyList.getGroupId();

            if (foundedGroupId.equals(groupId) && foundedArtifactId.equals(artifactId)) {
                versionFound = Optional.ofNullable(dependencyList.getVersion()).orElse(N_EXISTE_PAS);
                if (versionFound.startsWith("$")) {
                    String versionClean = versionFound.replaceAll("[\\$\\{\\}]", "");
                    Properties properties = fileModel.getProperties();
                    versionFound = properties.getProperty(versionClean);
                }
            }
        }

        versionFound = Optional.ofNullable(versionFound).orElse(N_EXISTE_PAS);
        return versionFound;
    }

}





