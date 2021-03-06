package com.sequenceiq.periscope.controller;


import static com.sequenceiq.cloudbreak.api.endpoint.v4.common.StackType.WORKLOAD;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.sequenceiq.periscope.api.model.AutoscaleClusterState;
import com.sequenceiq.periscope.api.model.ScalingStatus;
import com.sequenceiq.periscope.api.model.StateJson;
import com.sequenceiq.periscope.domain.Cluster;
import com.sequenceiq.periscope.domain.History;
import com.sequenceiq.periscope.model.NameOrCrn;
import com.sequenceiq.periscope.monitor.handler.CloudbreakCommunicator;
import com.sequenceiq.periscope.notification.HttpNotificationSender;
import com.sequenceiq.periscope.service.AutoscaleRestRequestThreadLocalService;
import com.sequenceiq.periscope.service.ClusterService;
import com.sequenceiq.periscope.service.HistoryService;
import com.sequenceiq.periscope.service.NotFoundException;

@Component
public class AutoScaleClusterCommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoScaleClusterCommonService.class);

    @Inject
    private ClusterService clusterService;

    @Inject
    private HistoryService historyService;

    @Inject
    private CloudbreakCommunicator cloudbreakCommunicator;

    @Inject
    private HttpNotificationSender notificationSender;

    @Inject
    private AutoscaleRestRequestThreadLocalService restRequestThreadLocalService;

    public List<Cluster> getClusters() {
        return clusterService.findAllByUser(restRequestThreadLocalService.getCloudbreakUser());
    }

    public List<Cluster> getDistroXClusters() {
        return clusterService.findDistroXByWorkspace(restRequestThreadLocalService.getRequestedWorkspaceId());
    }

    public Cluster getCluster(Long clusterId) {
        return clusterService.findById(clusterId);
    }

    @Retryable(value = NotFoundException.class, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public Cluster getClusterByStackCrn(String stackCrn) {
        return getClusterByCrnOrName(NameOrCrn.ofCrn(stackCrn));
    }

    @Retryable(value = NotFoundException.class, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public Cluster getClusterByStackName(String stackName) {
        return getClusterByCrnOrName(NameOrCrn.ofName(stackName));
    }

    public void deleteCluster(Long clusterId) {
        clusterService.removeById(clusterId);
    }

    public Cluster setState(Long clusterId, StateJson stateJson) {
        Cluster cluster = clusterService.setState(clusterId, stateJson.getState());
        createHistoryAndNotification(cluster);
        return cluster;
    }

    public Cluster setAutoscaleState(Long clusterId, AutoscaleClusterState autoscaleState) {
        return setAutoscaleState(clusterId, autoscaleState.isEnableAutoscaling());
    }

    public Cluster setAutoscaleState(Long clusterId, Boolean enableAutoScaling) {
        Cluster cluster = clusterService.setAutoscaleState(clusterId, enableAutoScaling);
        createHistoryAndNotification(cluster);
        return cluster;
    }

    private void createHistoryAndNotification(Cluster cluster) {
        History history;
        history = cluster.isAutoscalingEnabled()
                ? historyService.createEntry(ScalingStatus.ENABLED, "Autoscaling has been enabled for the cluster.", 0, cluster)
                : historyService.createEntry(ScalingStatus.DISABLED, "Autoscaling has been disabled for the cluster.", 0, cluster);
        notificationSender.send(cluster, history);
    }

    public void deleteAlertsForClusterCrn(String stackCrn) {
        Cluster cluster = getClusterByCrnOrName(NameOrCrn.ofCrn(stackCrn));
        clusterService.deleteAlertsForCluster(cluster.getId());
    }

    public void deleteAlertsForClusterName(String stackName) {
        Cluster cluster = getClusterByCrnOrName(NameOrCrn.ofName(stackName));
        clusterService.deleteAlertsForCluster(cluster.getId());
    }

    protected Cluster getClusterByCrnOrName(NameOrCrn nameOrCrn) {
        return nameOrCrn.hasName() ?
                clusterService.findOneByStackNameAndWorkspaceId(nameOrCrn.getName(), restRequestThreadLocalService.getRequestedWorkspaceId())
                        .orElseGet(() -> syncCBClusterByName(nameOrCrn.getName())) :
                clusterService.findOneByStackCrnAndWorkspaceId(nameOrCrn.getCrn(), restRequestThreadLocalService.getRequestedWorkspaceId())
                        .orElseGet(() -> syncCBClusterByCrn(nameOrCrn.getCrn()));
    }

    protected Cluster syncCBClusterByCrn(String stackCrn) {
        return Optional.ofNullable(cloudbreakCommunicator.getAutoscaleClusterByCrn(stackCrn))
                .filter(stack -> WORKLOAD.equals(stack.getStackType()))
                .map(stack -> clusterService.create(stack))
                .orElseThrow(NotFoundException.notFound("cluster", stackCrn));
    }

    protected Cluster syncCBClusterByName(String stackName) {
        return Optional.ofNullable(cloudbreakCommunicator.getAutoscaleClusterByName(stackName))
                .filter(stack -> WORKLOAD.equals(stack.getStackType()))
                .map(stack -> clusterService.create(stack))
                .orElseThrow(NotFoundException.notFound("cluster", stackName));
    }
}
