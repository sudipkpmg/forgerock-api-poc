package gov.tn.dhs.forgerock.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.tn.dhs.forgerock.config.AppProperties;
import gov.tn.dhs.forgerock.service.ListUsersService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ForgeRockUtil {

    public static final String OPENIDM_USERNAME_HEADER = "X-OpenIDM-Username";
    public static final String OPENIDM_PASSWORD_HEADER = "X-OpenIDM-Password";

    private static final Logger logger = LoggerFactory.getLogger(ForgeRockUtil.class);

    private static AppProperties appProperties;

    public ForgeRockUtil(AppProperties appProperties) {
        ForgeRockUtil.appProperties = appProperties;
    }

    public static HttpResponse doGet(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet getMethod = new HttpGet(url);
        getMethod.addHeader(OPENIDM_USERNAME_HEADER, appProperties.getOpenidmUsername());
        getMethod.addHeader(OPENIDM_PASSWORD_HEADER, appProperties.getOpenidmPassword());
        logger.info("calling URL [{}]", url);
        long t1 = System.currentTimeMillis();
        HttpResponse response = httpClient.execute(getMethod);
        long t2 = System.currentTimeMillis();
        logger.info("received response from call");
        logger.info("call to URL [{}] took {} ms", url, Long.toString(t2-t1));
        return response;
    }

    public static JsonNode getJsonFromResponse(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String jsonString = EntityUtils.toString(entity);
        logger.info("JSON response received: {}", jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        return jsonNode;
    }

}
