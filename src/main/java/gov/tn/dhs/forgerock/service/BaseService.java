package gov.tn.dhs.forgerock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.tn.dhs.forgerock.config.AppProperties;
import gov.tn.dhs.forgerock.exception.ServiceErrorException;
import gov.tn.dhs.forgerock.model.ClientError;
import gov.tn.dhs.forgerock.model.SimpleMessage;
import gov.tn.dhs.forgerock.util.JsonUtil;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

public abstract class BaseService {

    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    public static final String OPENIDM_USERNAME_HEADER = "X-OpenIDM-Username";
    public static final String OPENIDM_PASSWORD_HEADER = "X-OpenIDM-Password";

    protected final AppProperties appProperties;

    public BaseService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    protected abstract void process(Exchange exchange) throws Exception;

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

    protected HttpsURLConnection doGet(String url) throws IOException {
        logger.info("trying to access [{}]", url);
        RetryConfig config = RetryConfig.ofDefaults();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("GetBearer", config);
        Supplier<HttpsURLConnection> supplier = () -> doGetWithRetry(url);
        Supplier<HttpsURLConnection> httpsURLConnectionSupplier = Retry.decorateSupplier(retry, supplier);
        HttpsURLConnection connection = httpsURLConnectionSupplier.get();
        if (connection == null) {
            logger.error("\"tried accessing [{}] ... could not get connection to ForgeRock service\"", url);
            throw new IOException("could not get connection to ForgeRock service");
        }
        logger.info("obtained access to [{}]", url);
        return connection;
    }

    protected HttpsURLConnection doGetWithRetry(String url)  {
        try {
            URL connectionUrl = new URL(url);
            long t1= System.currentTimeMillis();
            HttpsURLConnection connection = (HttpsURLConnection) connectionUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty(OPENIDM_USERNAME_HEADER, "openidm-admin");
            connection.setRequestProperty(OPENIDM_PASSWORD_HEADER, "openidm-admin");
            connection.connect();
            long t2 = System.currentTimeMillis();
            long timeDiff = t2 - t1;
            logger.info("call to URL [{}] took {} ms", url, timeDiff);
            return connection;
        } catch (IOException e) {
            return null;
        }
    }

    protected String getResponseContent(HttpsURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }


    protected JsonNode getJsonFromResponse(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        return jsonNode;
    }



}
