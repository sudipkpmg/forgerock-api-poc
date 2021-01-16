package gov.tn.dhs.forgerock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.tn.dhs.forgerock.config.AppProperties;
import gov.tn.dhs.forgerock.exception.ServiceErrorException;
import gov.tn.dhs.forgerock.model.ClientError;
import gov.tn.dhs.forgerock.model.SimpleMessage;
import gov.tn.dhs.forgerock.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class BaseService {

    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    public static final String OPENIDM_USERNAME_HEADER = "X-OpenIDM-Username";
    public static final String OPENIDM_PASSWORD_HEADER = "X-OpenIDM-Password";

    protected final AppProperties appProperties;

    public BaseService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    protected abstract void process(Exchange exchange);

    protected void setupResponse(Exchange exchange, String code, Object response) {
        exchange.getIn().setBody(response, response.getClass());
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, code);
        exchange.getIn().setHeader("Content-Type", "application/json");
        exchange.getIn().setHeader("Accept", "application/json");
    }

    protected void setupResponse(Exchange exchange, String code, Object response, Class clazz) {
        exchange.getIn().setBody(response, clazz);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, code);
        exchange.getIn().setHeader("Content-Type", "application/json");
        exchange.getIn().setHeader("Accept", "application/json");
    }

    protected void setupOctetStreamResponse(Exchange exchange, String code, byte[] data) {
        exchange.getIn().setBody(data, byte[].class);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, code);
        exchange.getIn().setHeader("Content-Type", "application/octet-stream");
    }

    protected void setupMessage(Exchange exchange, String code, String message) {
        SimpleMessage simpleMessage = new SimpleMessage(message);
        exchange.getIn().setBody(simpleMessage, SimpleMessage.class);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, code);
        exchange.getIn().setHeader("Content-Type", "application/json");
    }

    protected void setupError(String code, String message) {
        ClientError clientError = new ClientError(code, message);
        throw new ServiceErrorException(code, JsonUtil.toJson(clientError));
    }

    protected HttpResponse doGet(String url) throws IOException {
        long t1;
        HttpResponse response;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet getMethod = new HttpGet(url);
            getMethod.addHeader(OPENIDM_USERNAME_HEADER, appProperties.getOpenidmUsername());
            getMethod.addHeader(OPENIDM_PASSWORD_HEADER, appProperties.getOpenidmPassword());
            logger.info("calling URL [{}]", url);
            t1 = System.currentTimeMillis();
            response = httpClient.execute(getMethod);
        }
        long t2 = System.currentTimeMillis();
        long timeDiff = t2 - t1;
        logger.info("received response from call");
        logger.info("call to URL [{}] took {} ms", url, timeDiff);
        return response;
    }

    protected JsonNode getJsonFromResponse(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String jsonString = EntityUtils.toString(entity);
        logger.info("JSON response received: {}", jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        return jsonNode;
    }



}
