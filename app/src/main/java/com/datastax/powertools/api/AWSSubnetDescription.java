package com.datastax.powertools.api;

/*
 *
 * @author Sebastián Estévez on 6/11/19.
 *
 */


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AWSSubnetDescription {
    @JsonProperty("Subnets")
    private List<AWSSubnet> subnets;

    public AWSSubnetDescription() {
    }

    public List<AWSSubnet> getSubnets() {
        return subnets;
    }

    public void setSubnets(List<AWSSubnet> subnets) {
        this.subnets = subnets;
    }
}
