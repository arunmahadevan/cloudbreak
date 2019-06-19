package com.sequenceiq.it.cloudbreak.cloud.v4;

import java.util.Map;

import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.base.parameter.stack.StackV4ParameterBase;
import com.sequenceiq.cloudbreak.common.mappable.CloudPlatform;
import com.sequenceiq.it.cloudbreak.dto.ClusterTestDto;
import com.sequenceiq.it.cloudbreak.dto.ImageSettingsTestDto;
import com.sequenceiq.it.cloudbreak.dto.InstanceTemplateV4TestDto;
import com.sequenceiq.it.cloudbreak.dto.NetworkV4TestDto;
import com.sequenceiq.it.cloudbreak.dto.PlacementSettingsTestDto;
import com.sequenceiq.it.cloudbreak.dto.StackAuthenticationTestDto;
import com.sequenceiq.it.cloudbreak.dto.VolumeV4TestDto;
import com.sequenceiq.it.cloudbreak.dto.credential.CredentialTestDto;
import com.sequenceiq.it.cloudbreak.dto.environment.EnvironmentNetworkTestDto;
import com.sequenceiq.it.cloudbreak.dto.environment.EnvironmentTestDto;
import com.sequenceiq.it.cloudbreak.dto.imagecatalog.ImageCatalogTestDto;
import com.sequenceiq.it.cloudbreak.dto.sdx.SdxTestDto;
import com.sequenceiq.it.cloudbreak.dto.stack.StackTestDtoBase;

public interface CloudProvider {

    String availabilityZone();

    String region();

    String location();

    ImageCatalogTestDto imageCatalog(ImageCatalogTestDto imageCatalog);

    ImageSettingsTestDto imageSettings(ImageSettingsTestDto imageSettings);

    InstanceTemplateV4TestDto template(InstanceTemplateV4TestDto template);

    VolumeV4TestDto attachedVolume(VolumeV4TestDto volume);

    NetworkV4TestDto network(NetworkV4TestDto network);

    StackTestDtoBase stack(StackTestDtoBase stack);

    ClusterTestDto cluster(ClusterTestDto cluster);

    SdxTestDto sdx(SdxTestDto sdx);

    String getSubnetCIDR();

    String getAccessCIDR();

    Map<String, String> getTags();

    String getClusterShape();

    CloudPlatform getCloudPlatform();

    CredentialTestDto credential(CredentialTestDto credential);

    EnvironmentTestDto environment(EnvironmentTestDto environment);

    EnvironmentNetworkTestDto environmentNetwork(EnvironmentNetworkTestDto environmentNetwork);

    PlacementSettingsTestDto placement(PlacementSettingsTestDto placement);

    StackAuthenticationTestDto stackAuthentication(StackAuthenticationTestDto stackAuthenticationEntity);

    Integer gatewayPort(StackTestDtoBase stackEntity);

    String getBlueprintName();

    StackV4ParameterBase stackParameters();
}
