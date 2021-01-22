package gov.tn.dhs.forgerock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import gov.tn.dhs.forgerock.config.AppProperties;
import gov.tn.dhs.forgerock.model.UserInfo;
import org.apache.camel.Exchange;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
//        String urlOverHttps = appProperties.getBaseurl() + "user?_queryFilter=true";
        String urlOverHttps = appProperties.getBaseurl() + "user?_queryId=query-all";
        try {
            HttpResponse response = doGet(urlOverHttps);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                JsonNode jsonNode = getJsonFromResponse(response);
                ArrayNode resultNode = (ArrayNode) jsonNode.get("result");
                List<UserInfo> userInfoList = new ArrayList<>();
                for (JsonNode userNode : resultNode) {
                    UserInfo userInfo = UserInfo.getUserInfo(userNode);
                    userInfoList.add(userInfo);
                }
                setupResponse(exchange, "200", userInfoList);
            } else {
                String reasonPhrase = response.getStatusLine().getReasonPhrase();
                String code = Integer.toString(response.getStatusLine().getStatusCode());
                String responseContent = EntityUtils.toString(response.getEntity());
                logger.info("response: {}", responseContent);
                setupError(code, reasonPhrase);
            }
        } catch (IOException e) {
            setupError("500", "Service error");
        }

    }

}



