package gov.tn.dhs.forgerock.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import gov.tn.dhs.forgerock.util.JsonUtil;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {

    private String userType;
    
    private String userName;
    
    private String givenName;
    
    private String initial;
    
    private String sn;
    
    private String suffix;
    
    private String nickname;
    
    private String mail;
    
    private String edisonId;
    
    private String accountStatus;
    
    private String mobile;
    
    private String primaryWorkGroup;
    
    private String subGroup;
    
    private String title;
    
    private String position;
    
    private String supervisorId;
    
    private String agencyName;
    
    private String contract;
    
    private String buildingName;

    private List effectiveRoles;

    private List effectiveAssignments;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

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

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getEdisonId() {
        return edisonId;
    }

    public void setEdisonId(String edisonId) {
        this.edisonId = edisonId;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPrimaryWorkGroup() {
        return primaryWorkGroup;
    }

    public void setPrimaryWorkGroup(String primaryWorkGroup) {
        this.primaryWorkGroup = primaryWorkGroup;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    private String postalAddress;
    
    private String city;
    
    private String state;
    
    private String postalCode;
    
    private String county;
    
    private String telephoneNumber;

    public List getEffectiveRoles() {
        return effectiveRoles;
    }

    public void setEffectiveRoles(List effectiveRoles) {
        this.effectiveRoles = effectiveRoles;
    }

    public List getEffectiveAssignments() {
        return effectiveAssignments;
    }

    public void setEffectiveAssignments(List effectiveAssignments) {
        this.effectiveAssignments = effectiveAssignments;
    }

    public static UserInfo getUserInfo(JsonNode userNode) {

        String userType = JsonUtil.getProperty(userNode, "userType");
        String userName = JsonUtil.getProperty(userNode, "userName");
        String givenName = JsonUtil.getProperty(userNode, "givenName");
        String initial = JsonUtil.getProperty(userNode, "initial");
        String sn = JsonUtil.getProperty(userNode, "sn");
        String suffix = JsonUtil.getProperty(userNode, "suffix");
        String nickname = JsonUtil.getProperty(userNode, "nickname");
        String mail = JsonUtil.getProperty(userNode, "mail");
        String edisonId = JsonUtil.getProperty(userNode, "edisonId");
        String accountStatus = JsonUtil.getProperty(userNode, "accountStatus");
        String mobile = JsonUtil.getProperty(userNode, "mobile");
        String primaryWorkGroup = JsonUtil.getProperty(userNode, "primaryWorkGroup");
        String subGroup = JsonUtil.getProperty(userNode, "subGroup");
        String title = JsonUtil.getProperty(userNode, "title");
        String position = JsonUtil.getProperty(userNode, "position");
        String supervisorId = JsonUtil.getProperty(userNode, "supervisorId");
        String agencyName = JsonUtil.getProperty(userNode, "agencyName");
        String contract = JsonUtil.getProperty(userNode, "contract");
        String buildingName = JsonUtil.getProperty(userNode, "buildingName");

        UserInfo userInfo = new UserInfo();

        userInfo.setUserType(userType);
        userInfo.setUserName (userName);
        userInfo.setGivenName(givenName);
        userInfo.setInitial(initial);
        userInfo.setSn(sn);
        userInfo.setSuffix(suffix);
        userInfo.setUserType(userType);
        userInfo.setNickname(nickname);
        userInfo.setMail(mail);
        userInfo.setEdisonId(edisonId);
        userInfo.setAccountStatus(accountStatus);
        userInfo.setMobile(mobile);
        userInfo.setPrimaryWorkGroup(primaryWorkGroup);
        userInfo.setSubGroup(subGroup);
        userInfo.setTitle(title);
        userInfo.setPosition(position);
        userInfo.setSupervisorId(supervisorId);
        userInfo.setAgencyName(agencyName);
        userInfo.setContract(contract);
        userInfo.setBuildingName(buildingName);

        List<EffectiveRole> effectiveRoles = EffectiveRole.getEffectiveRoles(userNode);
        userInfo.setEffectiveRoles(effectiveRoles);
        List<EffectiveAssignment> effectiveAssignments = EffectiveAssignment.getEffectiveAssignments(userNode);
        userInfo.setEffectiveAssignments(effectiveAssignments);

        return userInfo;
    }
}
