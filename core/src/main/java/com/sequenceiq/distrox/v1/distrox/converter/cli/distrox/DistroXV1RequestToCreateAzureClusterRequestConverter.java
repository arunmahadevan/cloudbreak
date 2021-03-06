package com.sequenceiq.distrox.v1.distrox.converter.cli.distrox;

import static com.sequenceiq.cloudbreak.util.NullUtil.doIfNotNull;
import static com.sequenceiq.cloudbreak.util.NullUtil.getIfNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.cloudera.cdp.datahub.model.AttachedVolumeRequest;
import com.cloudera.cdp.datahub.model.CreateAzureClusterRequest;
import com.cloudera.cdp.datahub.model.DatahubResourceTagRequest;
import com.cloudera.cdp.datahub.model.ImageRequest;
import com.cloudera.cdp.datahub.model.InstanceGroupRequest;
import com.cloudera.cdp.datahub.model.VolumeEncryptionRequest;
import com.sequenceiq.cloudbreak.common.mappable.CloudPlatform;
import com.sequenceiq.distrox.api.v1.distrox.model.DistroXV1Request;
import com.sequenceiq.distrox.api.v1.distrox.model.image.DistroXImageV1Request;
import com.sequenceiq.distrox.api.v1.distrox.model.instancegroup.InstanceGroupV1Request;
import com.sequenceiq.distrox.api.v1.distrox.model.instancegroup.template.InstanceTemplateV1Request;
import com.sequenceiq.distrox.api.v1.distrox.model.instancegroup.template.volume.VolumeV1Request;
import com.sequenceiq.distrox.api.v1.distrox.model.tags.TagsV1Request;

@Component
public class DistroXV1RequestToCreateAzureClusterRequestConverter implements DistroXV1RequestToCliRequestConverter {

    @Override
    public CloudPlatform supportedPlatform() {
        return CloudPlatform.AZURE;
    }

    @Override
    public CreateAzureClusterRequest convert(DistroXV1Request source) {
        CreateAzureClusterRequest request = new CreateAzureClusterRequest();
        request.setClusterName(source.getName());
        request.setClusterTemplateName(source.getCluster().getBlueprintName());
        request.setEnvironmentName(source.getEnvironmentName());
        request.setImage(convertImageRequest(source.getImage()));
        request.setInstanceGroups(convertInstanceGroups(source.getInstanceGroups()));
        if (source.getNetwork() != null && source.getNetwork().getAzure() != null) {
            request.setSubnetId(source.getNetwork().getAzure().getSubnetId());
        }
        request.setTags(getIfNotNull(source.getTags(), this::getTags));
        return request;
    }

    private List<InstanceGroupRequest> convertInstanceGroups(Set<InstanceGroupV1Request> source) {
        List<InstanceGroupRequest> instanceGroups = new ArrayList<>();
        doIfNotNull(source, s -> s.forEach(ig -> {
            InstanceGroupRequest instanceGroup = new InstanceGroupRequest();
            instanceGroup.setAttachedVolumeConfiguration(convertAttachedVolumeConfiguration(ig.getTemplate().getAttachedVolumes()));
            instanceGroup.setInstanceGroupName(ig.getName());
            instanceGroup.setInstanceGroupType(ig.getType().name());
            instanceGroup.setInstanceType(ig.getTemplate().getInstanceType());
            instanceGroup.setNodeCount(ig.getNodeCount());
            instanceGroup.setRecipeNames(getIfNotNull(ig.getRecipeNames(), List::copyOf));
            instanceGroup.setRecoveryMode(ig.getRecoveryMode().name());
            instanceGroup.setRootVolumeSize(ig.getTemplate().getRootVolume().getSize());
            instanceGroup.setVolumeEncryption(convertVolumeEncryption(ig.getTemplate()));
            instanceGroups.add(instanceGroup);
        }));
        return instanceGroups;
    }

    private List<AttachedVolumeRequest> convertAttachedVolumeConfiguration(Set<VolumeV1Request> source) {
        List<AttachedVolumeRequest> attachedVolumes = new ArrayList<>();
        source.forEach(volume -> {
            AttachedVolumeRequest attachedVolume = new AttachedVolumeRequest();
            attachedVolume.setVolumeCount(volume.getCount());
            attachedVolume.setVolumeSize(volume.getSize());
            attachedVolume.setVolumeType(volume.getType());
            attachedVolumes.add(attachedVolume);
        });
        return attachedVolumes;
    }

    private VolumeEncryptionRequest convertVolumeEncryption(InstanceTemplateV1Request source) {
        VolumeEncryptionRequest encryptionRequest = new VolumeEncryptionRequest();
        encryptionRequest.setEnableEncryption(source.getAzure().getEncrypted());
        source.getAzure().getManagedDisk();
        source.getAzure().getPrivateId();
//        encryptionRequest.setEncryptionKey();
        return encryptionRequest;
    }

    private ImageRequest convertImageRequest(DistroXImageV1Request source) {
        ImageRequest imageRequest = new ImageRequest();
        imageRequest.setId(source.getId());
        imageRequest.setCatalogName(source.getCatalog());
        return imageRequest;
    }

    private List<DatahubResourceTagRequest> getTags(TagsV1Request source) {
        List<DatahubResourceTagRequest> tags = new ArrayList<>();
        doIfNotNull(getIfNotNull(source, TagsV1Request::getUserDefined), userDefinedTags ->
                userDefinedTags.forEach((k, v) -> {
                    DatahubResourceTagRequest tag = new DatahubResourceTagRequest();
                    tag.setKey(k);
                    tag.setValue(v);
                    tags.add(tag);
                }));
        return tags;
    }
}
