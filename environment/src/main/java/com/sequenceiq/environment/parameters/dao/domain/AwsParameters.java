package com.sequenceiq.environment.parameters.dao.domain;

import com.sequenceiq.environment.parameters.dao.converter.S3GuardTableCreationConverter;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("AWS")
public class AwsParameters extends BaseParameters {

    @Column(name = "s3guard_dynamo_table_name")
    private String s3guardTableName;

    @Column(name = "s3guard_dynamo_table_creation")
    @Convert(converter = S3GuardTableCreationConverter.class)
    private S3GuardTableCreation s3guardTableCreation;

    @Column(name = "freeipa_spot_percentage")
    private Integer freeIpaSpotPercentage;

    public String getS3guardTableName() {
        return s3guardTableName;
    }

    public void setS3guardTableName(String s3guardTableName) {
        this.s3guardTableName = s3guardTableName;
    }

    public S3GuardTableCreation getS3guardTableCreation() {
        return s3guardTableCreation;
    }

    public void setS3guardTableCreation(S3GuardTableCreation s3guardTableCreation) {
        this.s3guardTableCreation = s3guardTableCreation;
    }

    public int getFreeIpaSpotPercentage() {
        return Objects.requireNonNullElse(freeIpaSpotPercentage, 0);
    }

    public void setFreeIpaSpotPercentage(Integer freeIpaSpotPercentage) {
        this.freeIpaSpotPercentage = Objects.requireNonNullElse(freeIpaSpotPercentage, 0);
    }
}
