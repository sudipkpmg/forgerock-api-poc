package gov.tn.dhs.forgerock.service;

import gov.tn.dhs.forgerock.config.AppProperties;
import gov.tn.dhs.forgerock.exception.ServiceErrorException;
import gov.tn.dhs.forgerock.model.ClientError;
import gov.tn.dhs.forgerock.model.SimpleMessage;
import gov.tn.dhs.forgerock.util.JsonUtil;
import org.apache.camel.Exchange;

public abstract class BaseService {

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

}
