package com.datastax.powertools.api;

/*
 *
 * @author Sebastián Estévez on 6/11/19.
 *
 */


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AWSSubnet {

    @JsonProperty("AvailabilityZone")
    private String availabilityZone;

    @JsonProperty("AvailableIpAddressCount")
    private String availabileIpAddressCount;

    @JsonProperty("DefaultForAz")
    private boolean defaultForAz;

    @JsonProperty("VpcId")
    private String vpcId;

    @JsonProperty("State")
    private String state;

    @JsonProperty("MapPublicIpOnLaunch")
    private boolean mapPublicIpOnLaunch;

    @JsonProperty("SubnetId")
    private String subnetId;

    @JsonProperty("CidrBlock")
    private String cidrBlock;

    @JsonProperty("AssignIpv6AddressOnCreation")
    private boolean assignIpv6AddressOnCreation;

    public AWSSubnet() {
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
    }

    public String getAvailabileIpAddressCount() {
        return availabileIpAddressCount;
    }

    public void setAvailabileIpAddressCount(String availabileIpAddressCount) {
        this.availabileIpAddressCount = availabileIpAddressCount;
    }

    public boolean isDefaultForAz() {
        return defaultForAz;
    }

    public void setDefaultForAz(boolean defaultForAz) {
        this.defaultForAz = defaultForAz;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isMapPublicIpOnLaunch() {
        return mapPublicIpOnLaunch;
    }

    public void setMapPublicIpOnLaunch(boolean mapPublicIpOnLaunch) {
        this.mapPublicIpOnLaunch = mapPublicIpOnLaunch;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getCidrBlock() {
        return cidrBlock;
    }

    public void setCidrBlock(String cidrBlock) {
        this.cidrBlock = cidrBlock;
    }

    public boolean isAssignIpv6AddressOnCreation() {
        return assignIpv6AddressOnCreation;
    }

    public void setAssignIpv6AddressOnCreation(boolean assignIpv6AddressOnCreation) {
        this.assignIpv6AddressOnCreation = assignIpv6AddressOnCreation;
    }
}
