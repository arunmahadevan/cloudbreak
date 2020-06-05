package com.sequenceiq.sdx.api.model;

import java.util.Map;

public class SetRangerCloudIdentityMappingRequest {

    private Map<String, String> azureUserMapping;

    private Map<String, String> azureGroupMapping;

    public Map<String, String> getAzureUserMapping() {
        return azureUserMapping;
    }

    public void setAzureUserMapping(Map<String, String> azureUserMapping) {
        this.azureUserMapping = azureUserMapping;
    }

    public Map<String, String> getAzureGroupMapping() {
        return azureGroupMapping;
    }

    public void setAzureGroupMapping(Map<String, String> azureGroupMapping) {
        this.azureGroupMapping = azureGroupMapping;
    }

}