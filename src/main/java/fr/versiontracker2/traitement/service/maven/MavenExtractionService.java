package fr.versiontracker2.traitement.service.maven;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.versiontracker2.traitement.modele.MavenDependency;
import fr.versiontracker2.transverse.exception.NonReadableDependencyFileException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MavenExtractionService {

    private static final String LECTURE_IMPOSSIBLE_POM = "Impossible de lire le pom du projet ";

	public static final String N_EXISTE_PAS = "n'existe pas";

    @Autowired
    private WebClient webClient;

    /**
     * Extrait les MavenDependency de la variable valueTrackedDependency
     * @param valueTrackedDependency
     * @return MavenDependency
     */
    public MavenDependency extractMavenDependencyFrom(String valueTrackedDependency) {
        MavenDependency mavenDependency = new MavenDependency();
        String[] retourSplits = valueTrackedDependency.split("\\|");
        mavenDependency.setGroupId(retourSplits[0]);
        mavenDependency.setArtifactId(retourSplits[1]);
        return mavenDependency;
    }

    /**
     * Extrait les informations de la variable Dependency File
     * @param valueUrl
     * @param mavenDependency
     * @param authorizedClient
     * @return informations sur les dépendances
     * @throws NonReadableDependencyFileException
     */
    public String getVersionWithPP3(String valueUrl, MavenDependency mavenDependency, OAuth2AuthorizedClient authorizedClient) throws NonReadableDependencyFileException {

        Model fileModel = null;

        if (valueUrl.matches("http.*")) {

            String pomContent = webClient.get()
                    .uri(valueUrl)
                    .attributes(oauth2AuthorizedClient(authorizedClient))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
            try {
                fileModel = mavenXpp3Reader.read(new StringReader(pomContent));
            } catch (XmlPullParserException | IOException e) {
                log.error(LECTURE_IMPOSSIBLE_POM + valueUrl,e);
                throw new NonReadableDependencyFileException(LECTURE_IMPOSSIBLE_POM + valueUrl, e);
            }
        } else {
            MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
            try {
                fileModel = mavenXpp3Reader.read(new FileReader(valueUrl));
            } catch (XmlPullParserException | IOException e) {
                log.error(LECTURE_IMPOSSIBLE_POM + valueUrl,e);
                throw new NonReadableDependencyFileException(LECTURE_IMPOSSIBLE_POM + valueUrl, e);
            }
        }
        List<Dependency> dependencyLists = fileModel.getDependencies();
        return retrieveVersion(fileModel, dependencyLists, mavenDependency.getGroupId(), mavenDependency.getArtifactId());
    }

    /**
     * Reçoit la variable "retrieveVersion"
     * @param fileModel
     * @param dependencyLists
     * @param groupId
     * @param artifactId
     * @return la version trouvée
     */
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





