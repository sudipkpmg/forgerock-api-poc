package gov.tn.dhs.forgerock.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import gov.tn.dhs.forgerock.util.JsonUtil;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EffectiveRole {

    @JsonProperty("_ref")
    private String ref;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public static ArrayList<EffectiveRole> getEffectiveRoles(JsonNode userNode) {
        ArrayNode effectiveRolesNode = (ArrayNode) userNode.get("effectiveRoles");
        ArrayList<EffectiveRole> effectiveRoles = new ArrayList<>();
        for (JsonNode effectiveRoleNode : effectiveRolesNode) {
            String _ref = JsonUtil.getProperty(effectiveRoleNode, "_ref");
            EffectiveRole effectiveRole = new EffectiveRole();
            effectiveRole.setRef(_ref);
            effectiveRoles.add(effectiveRole);
        }
        return effectiveRoles;
    }

}
