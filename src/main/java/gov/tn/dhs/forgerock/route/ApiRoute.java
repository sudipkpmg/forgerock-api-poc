package gov.tn.dhs.forgerock.route;

import com.fasterxml.jackson.core.JsonParseException;
import gov.tn.dhs.forgerock.config.AppProperties;
import gov.tn.dhs.forgerock.exception.ServiceErrorException;
import gov.tn.dhs.forgerock.model.GetRoleRequest;
import gov.tn.dhs.forgerock.model.RoleInfo;
import gov.tn.dhs.forgerock.model.GetUserRequest;
import gov.tn.dhs.forgerock.model.SimpleMessage;
import gov.tn.dhs.forgerock.service.*;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Configuration
public class ApiRoute extends RouteBuilder {

    private final ListUsersService listUsersService;
    private final GetUserService getUserService;
    private final ListRolesService listRolesService;
    private final GetRoleService getRoleService;

    private final AppProperties appProperties;

    public ApiRoute(
            ListUsersService listUsersService,
            GetUserService getUserService,
            ListRolesService listRolesService,
            GetRoleService getRoleService,
            AppProperties appProperties
    ) {
        this.listUsersService = listUsersService;
        this.getUserService = getUserService;
        this.listRolesService = listRolesService;
        this.getRoleService = getRoleService;
        this.appProperties = appProperties;
    }

    @Override
    public void configure() {

        onException(JsonParseException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody(constant("{}"))
        ;

        onException(Exception.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody(simple("${exception.message}"))
        ;

        onException(ServiceErrorException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, simple("${exception.code}"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody(simple("${exception.message}"))
        ;

        restConfiguration()
                .enableCORS(true)
                .apiProperty("cors", "true") // cross-site
                .component("servlet")
                .port(appProperties.getServerPort())
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        rest()
                .post("/list_users")
                .outType(List.class)
                .to("direct:listUsersService")
        ;
        from("direct:listUsersService")
                .log("received request to list all users ...")
                .bean(listUsersService)
                .endRest()
        ;

        rest()
                .post("/get_user")
                .type(GetUserRequest.class)
                .outType(List.class)
                .to("direct:SearchService")
        ;
        from("direct:SearchService")
                .log("received request to search ...")
                .bean(getUserService)
                .endRest()
        ;

        rest()
                .post("/list_roles")
                .outType(List.class)
                .to("direct:listRolesService")
        ;
        from("direct:listRolesService")
                .log("received request to list all roles ...")
                .bean(listRolesService)
                .endRest()
        ;

        rest()
                .post("/get_role")
                .type(GetRoleRequest.class)
                .outType(RoleInfo.class)
                .to("direct:RoleService")
        ;
        from("direct:RoleService")
                .log("received request to get role ...")
                .bean(getRoleService)
                .endRest()
        ;

        rest()
                .get("/")
                .to("direct:runningStatus")
        ;
        from("direct:runningStatus")
                .log("runStatus property value is " + appProperties.getRunStatus())
                .process(exchange -> exchange.getIn().setBody(new SimpleMessage(appProperties.getRunStatus()), SimpleMessage.class))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader("Content-Type", constant("application/json"))
                .endRest()
        ;

    }

}
