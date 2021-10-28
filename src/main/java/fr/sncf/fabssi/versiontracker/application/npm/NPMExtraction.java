package fr.sncf.fabssi.versiontracker.application.npm;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@JsonIgnoreProperties
public class NPMExtraction {

    public static String getVersionWithJackson(String valueUrl, String trackedDependency) throws Exception {

        URL fileURL;
        if (valueUrl.matches("http.*")) {
            fileURL = new URL(valueUrl);
        }else {
            fileURL = new URL("file:///" + valueUrl);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            PackageInfos packageInfos = objectMapper.readValue(fileURL, PackageInfos.class);
            return findVersionNPM(trackedDependency, packageInfos);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
    }

    public static String findVersionNPM(String trackedDependency, PackageInfos packageInfos) {
        Map<String, String> dependenciesMap = packageInfos.getDependencies();
        String versionNPM = null;

        if (dependenciesMap.containsKey(trackedDependency)) {
            versionNPM = dependenciesMap.get(trackedDependency);

        }else   {
            versionNPM = "n'existe pas";
        }

        return versionNPM;
    }

}


