package com.sequenceiq.cloudbreak.auth.altus;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.cloudera.thunderhead.service.usermanagement.UserManagementProto;
import com.google.common.annotations.VisibleForTesting;
import com.sequenceiq.cloudbreak.logger.MDCUtils;

@Service
public class EntitlementService {

    @VisibleForTesting
    static final String CDP_AZURE = "CDP_AZURE";

    @VisibleForTesting
    static final String CDP_BASE_IMAGE = "CDP_BASE_IMAGE";

    @VisibleForTesting
    static final String CDP_AUTOMATIC_USERSYNC_POLLER = "CDP_AUTOMATIC_USERSYNC_POLLER";

    @VisibleForTesting
    static final String CDP_FREEIPA_HA = "CDP_FREEIPA_HA";

    @VisibleForTesting
    static final String CLOUDERA_INTERNAL_ACCOUNT = "CLOUDERA_INTERNAL_ACCOUNT";

    @VisibleForTesting
    static final String CDP_FMS_CLUSTER_PROXY = "CDP_FMS_CLUSTER_PROXY";

    @VisibleForTesting
    static final String CDP_CLOUD_STORAGE_VALIDATION = "CDP_CLOUD_STORAGE_VALIDATION";

    @VisibleForTesting
    static final String CDP_RAZ = "CDP_RAZ";

    @VisibleForTesting
    static final String CDP_RUNTIME_UPGRADE = "CDP_RUNTIME_UPGRADE";

    @VisibleForTesting
    static final String CDP_FREEIPA_DL_EBS_ENCRYPTION = "CDP_FREEIPA_DL_EBS_ENCRYPTION";

    @Inject
    private GrpcUmsClient umsClient;

    public boolean azureEnabled(String actorCrn, String accountId) {
        return isEntitlementRegistered(actorCrn, accountId, CDP_AZURE);
    }

    public boolean baseImageEnabled(String actorCrn, String accountId) {
        return isEntitlementRegistered(actorCrn, accountId, CDP_BASE_IMAGE);
    }

    public boolean automaticUsersyncPollerEnabled(String actorCrn, String accountId) {
        return isEntitlementRegistered(actorCrn, accountId, CDP_AUTOMATIC_USERSYNC_POLLER);
    }

    public boolean freeIpaHaEnabled(String actorCrn, String accountID) {
        return isEntitlementRegistered(actorCrn, accountID, CDP_FREEIPA_HA);
    }

    public boolean internalTenant(String actorCrn, String accountId) {
        return isEntitlementRegistered(actorCrn, accountId, CLOUDERA_INTERNAL_ACCOUNT);
    }

    public boolean fmsClusterProxyEnabled(String actorCrn, String accountId) {
        return isEntitlementRegistered(actorCrn, accountId, CDP_FMS_CLUSTER_PROXY);
    }

    public boolean cloudStorageValidationEnabled(String actorCrn, String accountId) {
        return isEntitlementRegistered(actorCrn, accountId, CDP_CLOUD_STORAGE_VALIDATION);
    }

    public boolean razEnabled(String actorCrn, String accountId) {
        return isEntitlementRegistered(actorCrn, accountId, CDP_RAZ);
    }

    public boolean runtimeUpgradeEnabled(String actorCrn, String accountId) {
        return isEntitlementRegistered(actorCrn, accountId, CDP_RUNTIME_UPGRADE);
    }

    public boolean freeIpaDlEbsEncryptionEnabled(String actorCrn, String accountId) {
        return isEntitlementRegistered(actorCrn, accountId, CDP_FREEIPA_DL_EBS_ENCRYPTION);
    }

    public List<String> getEntitlements(String actorCrn, String accountId) {
        return getAccount(actorCrn, accountId).getEntitlementsList()
                .stream()
                .map(e -> e.getEntitlementName().toUpperCase())
                .collect(Collectors.toList());
    }

    private UserManagementProto.Account getAccount(String actorCrn, String accountId) {
        return umsClient.getAccountDetails(actorCrn, accountId, MDCUtils.getRequestId());
    }

    private boolean isEntitlementRegistered(String actorCrn, String accountId, String entitlement) {
        return getEntitlements(actorCrn, accountId)
                .stream()
                .anyMatch(e -> e.equalsIgnoreCase(entitlement));
    }

}
