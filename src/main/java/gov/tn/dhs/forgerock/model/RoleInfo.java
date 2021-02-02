package gov.tn.dhs.forgerock.model;

//import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import gov.tn.dhs.forgerock.util.JsonUtil;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleInfo {

    @JsonProperty("-id")
    private String id;

    @JsonProperty("_rev")
    private String rev;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("condition")
    private String condition;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public static RoleInfo getRoleInfo(JsonNode userNode) {

        String id = JsonUtil.getProperty(userNode, "_id");
        String rev = JsonUtil.getProperty(userNode, "_rev");
        String name = JsonUtil.getProperty(userNode, "name");
        String description = JsonUtil.getProperty(userNode, "description");
        String condition = JsonUtil.getProperty(userNode, "condition");

        RoleInfo roleInfo = new RoleInfo();

        roleInfo.setId(id);
        roleInfo.setRev(rev);
        roleInfo.setName(name);
        roleInfo.setDescription(description);
        roleInfo.setCondition(condition);

        return roleInfo;
    }

}
