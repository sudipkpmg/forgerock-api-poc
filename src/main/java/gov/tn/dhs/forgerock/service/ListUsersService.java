package gov.tn.dhs.forgerock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import gov.tn.dhs.forgerock.config.AppProperties;
import gov.tn.dhs.forgerock.model.UserInfo;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListUsersService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(ListUsersService.class);

    public ListUsersService(AppProperties appProperties) {
        super(appProperties);
    }

    public void process(Exchange exchange) {
        String urlOverHttps = appProperties.getBaseurl() + "user?_queryId=query-all";
        logger.info("trying [{}]", urlOverHttps);
        int connectionRetries = 1;
        boolean resultNotObtained = true;
        while ((connectionRetries <= appProperties.getServiceCallRetryAttempLimit()) && resultNotObtained) {
            try {
                HttpsURLConnection connection = doGet(urlOverHttps);
                int statusCode = connection.getResponseCode();
                if (statusCode == 200) {
                    logger.info("obtained 200 result in attempt {}", connectionRetries);
                    resultNotObtained = false;
                    String jsonString = getResponseContent(connection);
                    logger.info("JSON response received: {}", jsonString);
                    JsonNode jsonNode = getJsonFromResponse(jsonString);
                    ArrayNode resultNode = (ArrayNode) jsonNode.get("result");
                    List<UserInfo> userInfoList = new ArrayList<>();
                    for (JsonNode userNode : resultNode) {
                        UserInfo userInfo = UserInfo.getUserInfo(userNode);
                        userInfoList.add(userInfo);
                    }
                    if (userInfoList.isEmpty()) {
                        setupError(Integer.toString(statusCode), "No user found");
                    } else {
                        setupResponse(exchange, "200", userInfoList);
                    }
                } else {
                    logger.info("obtained {} result in attempt {}", statusCode, connectionRetries);
                    connectionRetries++;
                    if (connectionRetries > appProperties.getServiceCallRetryAttempLimit()) {
                        if (statusCode == 404) {
                            setupError("404", "No user found");
                        } else {
                            setupError("500", "Service error");
                        }
                    }
                }
            } catch (IOException e) {
                setupError("500", "Service error");
            }
        }
    }

}



