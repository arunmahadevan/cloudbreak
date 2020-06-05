package com.sequenceiq.freeipa.service.freeipa.user.model;

import java.util.Objects;
import java.util.Optional;

public class FmsGroup {

    private String name;

    private Optional<String> azureObjectId = Optional.empty();

    public FmsGroup withName(String name) {
        this.name = name;
        return this;
    }

    public FmsGroup withAzureObjectId(Optional<String> azureObjectId) {
        this.azureObjectId = azureObjectId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getAzureObjectId() {
        return azureObjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FmsGroup other = (FmsGroup) o;

        return Objects.equals(this.name, other.name) &&
                Objects.equals(this.azureObjectId, other.azureObjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, azureObjectId);
    }

    @Override
    public String toString() {
        return "FmsGroup{" +
                "name='" + name + '\'' +
                ", azureObjectId=" + azureObjectId +
                '}';
    }
}