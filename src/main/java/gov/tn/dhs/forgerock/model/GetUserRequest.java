package gov.tn.dhs.forgerock.model;

import java.util.StringJoiner;

public class GetUserRequest {

    private String userName;
    private String givenName;
    private String sn;
    private String mail;
    private String userType;
    private String accountStatus;
    private String edisonId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getEdisonId() {
        return edisonId;
    }

    public void setEdisonId(String edisonId) {
        this.edisonId = edisonId;
    }

    public String getQueryFilter() {
        StringJoiner stringJoiner = new StringJoiner("+and+");
        addCondition(stringJoiner, "userName", userName);
        addCondition(stringJoiner, "givenName", givenName);
        addCondition(stringJoiner, "sn", sn);
        addCondition(stringJoiner, "mail", mail);
        addCondition(stringJoiner, "userType", userType);
        addCondition(stringJoiner, "accountStatus", accountStatus);
        addCondition(stringJoiner, "edisonId", edisonId);
        return stringJoiner.toString();
    }

    private void addCondition(StringJoiner stringJoiner, String fieldName, String fieldValue) {
        if ( (fieldValue != null) && (! fieldValue.trim().isEmpty()) ) {
            stringJoiner.add(fieldName + "+eq+%22" + fieldValue + "%22");
        }
    }

}
