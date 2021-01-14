package gov.tn.dhs.forgerock.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import gov.tn.dhs.forgerock.util.JsonUtil;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EffectiveAssignment {

    @JsonProperty("_id")
    private String id;

    private String name;

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ArrayList<EffectiveAssignment> getEffectiveAssignments(JsonNode userNode) {
        ArrayNode effectiveAssignmentsNode = (ArrayNode) userNode.get("effectiveAssignments");
        ArrayList<EffectiveAssignment> effectiveAssignments = new ArrayList<>();
        for (JsonNode effectiveAssignmentNode : effectiveAssignmentsNode) {
            String id = JsonUtil.getProperty(effectiveAssignmentNode, "_id");
            String name = JsonUtil.getProperty(effectiveAssignmentNode, "name");
            String description = JsonUtil.getProperty(effectiveAssignmentNode, "description");
            EffectiveAssignment effectiveAssignment = new EffectiveAssignment();
            effectiveAssignment.setId(id);
            effectiveAssignment.setName(name);
            effectiveAssignment.setDescription(description);
            effectiveAssignments.add(effectiveAssignment);
        }
        return effectiveAssignments;
    }

}
