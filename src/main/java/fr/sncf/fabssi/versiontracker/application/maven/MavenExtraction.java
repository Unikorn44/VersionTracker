package fr.sncf.fabssi.versiontracker.application.maven;

import fr.sncf.fabssi.versiontracker.configuration.TrackedDependencyInfo;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.web.reactive.function.client.WebClient;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import java.net.URISyntaxException;
import java.net.URL;

import java.util.List;
import java.util.Properties;

import org.xml.sax.SAXException;

public class MavenExtraction {


    public static MavenDependency extractMavenDependencyFrom(String valueTrackedDependency) {
        MavenDependency mavenDependency = new MavenDependency();
        String[] retourSplits = valueTrackedDependency.split("\\|");
        mavenDependency.setGroupId(retourSplits[0]);
        mavenDependency.setArtifactId(retourSplits[1]);
        return mavenDependency;
    }

    public static void GetVersionWithPP3(TrackedDependencyInfo newTrackedDependencyInfo, String valueUrl, MavenDependency mavenDependency) throws IOException, XmlPullParserException, URISyntaxException, ParserConfigurationException, SAXException {

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
        String versionFound = retrieveVersion(fileModel, dependencyLists, mavenDependency.getGroupId(), mavenDependency.getArtifactId());

        newTrackedDependencyInfo.setVersion(versionFound);
    }

    private static String retrieveVersion(Model fileModel, List<Dependency> dependencyLists, String groupId, String artifactId) {

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




