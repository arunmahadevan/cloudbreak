package com.sequenceiq.freeipa.flow.freeipa.upscale;

import com.sequenceiq.flow.core.FlowState;

public enum UpscaleState implements FlowState {
    INIT_STATE,
    UPSCALE_STARTING_STATE,
    UPSCALE_ADD_INSTANCES_STATE,
    UPSCALE_VALIDATE_INSTANCES_STATE,
    UPSCALE_EXTEND_METADATA_STATE,
    UPSCALE_SAVE_METADATA_STATE,
    UPSCALE_TLS_SETUP_STATE,
    UPSCALE_BOOTSTRAPPING_MACHINES_STATE,
    UPSCALE_COLLECTING_HOST_METADATA_STATE,
    UPSCALE_RECORD_HOSTNAMES_STATE,
    UPSCALE_FREEIPA_INSTALL_STATE,
    UPSCALE_UPDATE_CLUSTERPROXY_REGISTRATION_STATE,
    UPSCALE_FREEIPA_POST_INSTALL_STATE,
    UPSCALE_UPDATE_METADATA_STATE,
    UPSCALE_FINISHED_STATE,
    UPSCALE_FAIL_STATE,
    FINAL_STATE;
}
