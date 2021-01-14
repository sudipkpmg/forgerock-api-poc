package gov.tn.dhs.forgerock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import gov.tn.dhs.forgerock.config.AppProperties;
import gov.tn.dhs.forgerock.model.SearchRequest;
import gov.tn.dhs.forgerock.model.UserInfo;
import gov.tn.dhs.forgerock.util.ForgeRockUtil;
import org.apache.camel.Exchange;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    public SearchService(AppProperties appProperties) {
        super(appProperties);
    }

    public void process(Exchange exchange) {
        SearchRequest searchRequest = exchange.getIn().getBody(SearchRequest.class);
        String queryFilter = searchRequest.getQueryFilter();
        logger.info("queryFilter = [{}]", queryFilter);
        String urlOverHttps = appProperties.getBaseurl() + "user?_queryFilter=" + queryFilter;
        try {
            HttpResponse response = ForgeRockUtil.doGet(urlOverHttps);
            int statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode) {
                case 200: {
                    JsonNode jsonNode = ForgeRockUtil.getJsonFromResponse(response);
                    ArrayNode resultNode = (ArrayNode) jsonNode.get("result");
                    List<UserInfo> userInfoList = new ArrayList<>();
                    for (JsonNode userNode : resultNode) {
                        UserInfo userInfo = UserInfo.getUserInfo(userNode);
                        userInfoList.add(userInfo);
                    }
                    setupResponse(exchange, "200", userInfoList);
                    break;
                }
                case 404: {
                    setupError("404", "User not found");
                    break;
                }
                default: {
                    String reasonPhrase = response.getStatusLine().getReasonPhrase();
                    String code = Integer.toString(response.getStatusLine().getStatusCode());
                    setupError(code, reasonPhrase);
                    break;
                }
            }
        } catch (IOException e) {
            setupError("500", "Service error");
        }
    }

}



