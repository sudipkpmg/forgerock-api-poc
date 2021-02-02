package gov.tn.dhs.forgerock.service;

import com.fasterxml.jackson.databind.JsonNode;
import gov.tn.dhs.forgerock.config.AppProperties;
import gov.tn.dhs.forgerock.model.GetRoleRequest;
import gov.tn.dhs.forgerock.model.RoleInfo;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

@Service
public class GetRoleService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(GetRoleService.class);

    public GetRoleService(AppProperties appProperties) {
        super(appProperties);
    }

    public void process(Exchange exchange) {
        GetRoleRequest getRoleRequest = exchange.getIn().getBody(GetRoleRequest.class);
        String roleId = getRoleRequest.getId();
        if (roleId == null) {
            setupError("400", "No query parameter provided");
        }
        String urlOverHttps = appProperties.getBaseurl() + "role/" + roleId;
        try {
            HttpsURLConnection connection = doGet(urlOverHttps);
            int statusCode = connection.getResponseCode();
            switch (statusCode) {
                case 200: {
                    String jsonString = getResponseContent(connection);
                    logger.info("JSON response received: {}", jsonString);
                    JsonNode jsonNode = getJsonFromResponse(jsonString);
                    RoleInfo roleInfo = RoleInfo.getRoleInfo(jsonNode);
                    setupResponse(exchange, "200", roleInfo);
                    break;
                }
                case 404: {
                    setupError("404", "Role not found");
                    break;
                }
                default: {
                    setupError("500", "Service error");
                    break;
                }
            }
        } catch (IOException e) {
            setupError("500", "Service error");
        }
    }

}



