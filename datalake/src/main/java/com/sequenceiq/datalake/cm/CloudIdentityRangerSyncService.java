package com.sequenceiq.datalake.cm;

import com.cloudera.api.swagger.client.ApiException;
import com.sequenceiq.datalake.entity.SdxCluster;
import com.sequenceiq.datalake.service.sdx.SdxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
public class CloudIdentityRangerSyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudIdentityRangerSyncService.class);

    @Inject
    private ClouderaManagerRangerUtil clouderaManagerRangerUtil;

    @Inject
    private SdxService sdxService;

    public void setAzureCloudIdentityMapping(String environmentCrn, Map<String, String> azureUserMapping, Map<String, String> azureGroupMapping) {
        LOGGER.info("Setting Azure cloud id mappings for environment = {}", environmentCrn);
        List<SdxCluster> sdxClusters = sdxService.listSdxByEnvCrn(environmentCrn);
        if (sdxClusters.isEmpty()) {
            LOGGER.info("Environment has no datalake clusters to sync");
        }
        sdxClusters.forEach(sdxCluster -> {
            String stackCrn = sdxCluster.getStackCrn();
            LOGGER.info("Updating azure cloud id mappings for datalake stack crn = {}, environment = {}", stackCrn, environmentCrn);
            try {
                clouderaManagerRangerUtil.setAzureCloudIdentityMapping(stackCrn, azureUserMapping, azureGroupMapping);
            } catch (ApiException e) {
                throw new RuntimeException("Encountered api exception", e);
            }
        });
    }
}
