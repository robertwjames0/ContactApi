package com.example.ContactApi.ContactApi.Model;

import java.util.Objects;

public class Organisation {

    private int organisationId;
    private String name;
    private String address;
    private String url;

    public Organisation(int organisationId, String name, String address, String url) {
        this.organisationId = organisationId;
        this.name = name;
        this.address = address;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organisation that = (Organisation) o;
        return organisationId == that.organisationId && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organisationId, name, address, url);
    }

    public int getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(int organisationId) {
        this.organisationId = organisationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
