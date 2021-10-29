package fr.versiontracker.traitement.service.maven;

import fr.versiontracker.api.ressource.TrackedDependencyInfo;
import fr.versiontracker.traitement.modele.MavenDependency;
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
import java.util.Properties;

import org.xml.sax.SAXException;

@Service
public class MavenExtractionService {

    public MavenDependency extractMavenDependencyFrom(String valueTrackedDependency) {
        MavenDependency mavenDependency = new MavenDependency();
        String[] retourSplits = valueTrackedDependency.split("\\|");
        mavenDependency.setGroupId(retourSplits[0]);
        mavenDependency.setArtifactId(retourSplits[1]);
        return mavenDependency;
    }

    public String getVersionWithPP3(String valueUrl, MavenDependency mavenDependency) throws IOException, XmlPullParserException, URISyntaxException, ParserConfigurationException, SAXException {

        Model fileModel = null;
        URL fileURL;

        if (valueUrl.matches("http.*")) {

            WebClient webClient = WebClient.create();
            String pomContent = webClient.get()
                    .uri(valueUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
            fileModel = mavenXpp3Reader.read(new StringReader(pomContent));

        } else {
            MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
            fileModel = mavenXpp3Reader.read(new FileReader(valueUrl));

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
                if (dependencyList.getVersion() != null) {
                    versionFound = dependencyList.getVersion();

                    if (versionFound.startsWith("$")) {
                        String versionClean = versionFound.replaceAll("[\\$\\{\\}]", "");
                        Properties properties = fileModel.getProperties();
                        versionFound = properties.getProperty(versionClean);
                        return versionFound;
                    }

                    return versionFound;
                }
            }
        }
        versionFound = "Pas de version pour cet artifact";
        return versionFound;
    }


}





