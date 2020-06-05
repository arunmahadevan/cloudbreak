package com.sequenceiq.freeipa.service.freeipa.user.model;

import java.util.Objects;
import java.util.Optional;

public class FmsUser {

    private String name;

    private String firstName;

    private String lastName;

    private Optional<String> azureObjectId = Optional.empty();

    public String getName() {
        return name;
    }

    public FmsUser withName(String name) {
        this.name = name;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public FmsUser withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public FmsUser withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Optional<String> getAzureObjectId() {
        return azureObjectId;
    }

    public FmsUser withAzureObjectId(Optional<String> azureObjectId) {
        this.azureObjectId = azureObjectId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FmsUser other = (FmsUser) o;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.firstName, other.firstName)
                && Objects.equals(this.lastName, other.lastName)
                && Objects.equals(this.azureObjectId, other.azureObjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, firstName, lastName, azureObjectId);
    }

    @Override
    public String toString() {
        return "FmsUser{"
                + "name='" + name + '\''
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", azureObjectId='" + azureObjectId + '\''
                + '}';
    }
}
