package com.sequenceiq.cloudbreak.core.flow2.cluster.upgrade;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.sequenceiq.cloudbreak.api.endpoint.v4.common.DetailedStackStatus;
import com.sequenceiq.cloudbreak.api.endpoint.v4.common.Status;
import com.sequenceiq.cloudbreak.message.NotificationEventType;
import com.sequenceiq.cloudbreak.core.flow2.stack.CloudbreakFlowMessageService;
import com.sequenceiq.cloudbreak.domain.view.StackView;
import com.sequenceiq.cloudbreak.message.Msg;
import com.sequenceiq.cloudbreak.service.StackUpdater;
import com.sequenceiq.cloudbreak.service.cluster.ClusterService;
import com.sequenceiq.cloudbreak.util.StackUtil;

@Service
public class ClusterUpgradeService {
    @Inject
    private CloudbreakFlowMessageService flowMessageService;

    @Inject
    private ClusterService clusterService;

    @Inject
    private StackUpdater stackUpdater;

    @Inject
    private StackUtil stackUtil;

    public void upgradeCluster(long stackId) {
        clusterService.updateClusterStatusByStackId(stackId, Status.UPDATE_IN_PROGRESS);
        flowMessageService.fireEventAndLog(stackId, Msg.CLUSTER_UPGRADE, NotificationEventType.UPDATE_IN_PROGRESS);
    }

    public void clusterUpgradeFinished(StackView stack) {
        Long stackId = stack.getId();
        clusterService.updateClusterStatusByStackId(stackId, Status.START_REQUESTED);
        stackUpdater.updateStackStatus(stackId, DetailedStackStatus.AVAILABLE, "Ambari is successfully upgraded.");
        flowMessageService.fireEventAndLog(stackId, Msg.CLUSTER_UPGRADE_FINISHED, NotificationEventType.AVAILABLE, stackUtil.extractClusterManagerIp(stack));
    }

    public void handleUpgradeClusterFailure(long stackId, String errorReason) {
        clusterService.updateClusterStatusByStackId(stackId, Status.UPDATE_FAILED, errorReason);
        stackUpdater.updateStackStatus(stackId, DetailedStackStatus.AVAILABLE);
        flowMessageService.fireEventAndLog(stackId, Msg.CLUSTER_UPGRADE_FAILED, NotificationEventType.UPDATE_FAILED, errorReason);
    }
}
