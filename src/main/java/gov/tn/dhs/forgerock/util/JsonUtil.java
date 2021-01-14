package gov.tn.dhs.forgerock.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    public static String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{}";
        try {
            jsonString = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
        }
        return jsonString;
    }

    public static String getProperty(JsonNode jsonNode, String name) {
        return (jsonNode.get(name) != null) ? jsonNode.get(name).asText() : null;
    }

}
