package com.datastax.powertools;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by sebastianestevez on 3/26/18.
 */
public class MultiCloudServiceConfig extends Configuration{

    @NotEmpty
    @JsonProperty
    private String secretsPath = "conf";

}
