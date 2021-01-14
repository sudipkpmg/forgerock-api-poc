package gov.tn.dhs.forgerock.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.tn.dhs.forgerock.config.AppProperties;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ForgeRockUtil {

    public static final String OPENIDM_USERNAME_HEADER = "X-OpenIDM-Username";
    public static final String OPENIDM_PASSWORD_HEADER = "X-OpenIDM-Password";

    private static AppProperties appProperties;

    public ForgeRockUtil(AppProperties appProperties) {
        ForgeRockUtil.appProperties = appProperties;
    }

    public static HttpResponse doGet(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet getMethod = new HttpGet(url);
        getMethod.addHeader(OPENIDM_USERNAME_HEADER, appProperties.getOpenidmUsername());
        getMethod.addHeader(OPENIDM_PASSWORD_HEADER, appProperties.getOpenidmPassword());
        HttpResponse response = httpClient.execute(getMethod);
        return response;
    }

    public static JsonNode getJsonFromResponse(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String jsonString = EntityUtils.toString(entity);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        return jsonNode;
    }

}
