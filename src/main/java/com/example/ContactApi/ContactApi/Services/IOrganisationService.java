package com.example.ContactApi.ContactApi.Services;

import com.example.ContactApi.ContactApi.Model.Organisation;

import java.util.List;

public interface IOrganisationService {
    Organisation GetById(int id) throws Exception;

    Organisation UpdateOrganisation(Organisation org) throws Exception;

    void DeleteOrganisation(int id) throws Exception;
    List<Organisation> GetAll(int offset, int limit) throws Exception;
}
