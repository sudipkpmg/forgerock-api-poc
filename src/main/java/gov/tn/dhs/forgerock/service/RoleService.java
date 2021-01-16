package gov.tn.dhs.forgerock.service;

import com.fasterxml.jackson.databind.JsonNode;
import gov.tn.dhs.forgerock.config.AppProperties;
import gov.tn.dhs.forgerock.model.GetRoleRequest;
import gov.tn.dhs.forgerock.model.RoleInfo;
import org.apache.camel.Exchange;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RoleService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleService(AppProperties appProperties) {
        super(appProperties);
    }

    public void process(Exchange exchange) {
        GetRoleRequest getRoleRequest = exchange.getIn().getBody(GetRoleRequest.class);
        String roleId = getRoleRequest.getId();
        String urlOverHttps = appProperties.getBaseurl() + "role/" + roleId;
        try {
            HttpResponse response = doGet(urlOverHttps);
            int statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode) {
                case 200: {
                    JsonNode jsonNode = getJsonFromResponse(response);
                    RoleInfo roleInfo = RoleInfo.getRoleInfo(jsonNode);
                    setupResponse(exchange, "200", roleInfo);
                    break;
                }
                case 404: {
                    setupError("404", "Role not found");
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



