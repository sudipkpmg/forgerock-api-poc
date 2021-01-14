package gov.tn.dhs.forgerock.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("box")
public class AppProperties {

    @Value("${server.port}")
    private String serverPort;

    @Value("${runstatus}")
    private String runStatus;

    @Value("${openidm.baseurl}")
    private String baseurl;

    @Value("${openidm.username}")
    private String openidmUsername;

    @Value("${openidm.password}")
    private String openidmPassword;

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public String getOpenidmUsername() {
        return openidmUsername;
    }

    public void setOpenidmUsername(String openidmUsername) {
        this.openidmUsername = openidmUsername;
    }

    public String getOpenidmPassword() {
        return openidmPassword;
    }

    public void setOpenidmPassword(String openidmPassword) {
        this.openidmPassword = openidmPassword;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }
}
