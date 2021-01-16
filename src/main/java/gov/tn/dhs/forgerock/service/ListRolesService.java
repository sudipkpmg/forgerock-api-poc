package gov.tn.dhs.forgerock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import gov.tn.dhs.forgerock.config.AppProperties;
import gov.tn.dhs.forgerock.model.RoleInfo;
import org.apache.camel.Exchange;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListRolesService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(ListRolesService.class);

    public ListRolesService(AppProperties appProperties) {
        super(appProperties);
    }

    public void process(Exchange exchange) {
        String urlOverHttps = appProperties.getBaseurl() + "role?_queryFilter=true";
        try {
            HttpResponse response = doGet(urlOverHttps);
            int statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode) {
                case 200: {
                    JsonNode jsonNode = getJsonFromResponse(response);
                    ArrayNode resultNode = (ArrayNode) jsonNode.get("result");
                    List<RoleInfo> roleInfoList = new ArrayList<>();
                    for (JsonNode roleNode : resultNode) {
                        RoleInfo roleInfo = RoleInfo.getRoleInfo(roleNode);
                        roleInfoList.add(roleInfo);
                    }
                    setupResponse(exchange, "200", roleInfoList);
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



