package com.example.ContactApi.ContactApi.Services;

import com.example.ContactApi.ContactApi.Model.Organisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;


public class OrganisationService implements IOrganisationService {
    private static final Logger log = LoggerFactory.getLogger(OrganisationService.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Organisation GetById(int id) throws Exception {
        var orgs = jdbcTemplate.query(
                        "SELECT organisationId, name, address, url " +
                                "FROM organisations " +
                                "WHERE organisationId = ?",
                        (rs, rowNum) -> new Organisation(
                                rs.getInt("organisationId"),
                                rs.getString("name"),
                                rs.getString("address"),
                                rs.getString("url")
                                ),
                id);
        if(orgs.isEmpty()){
            return null;
        }
        return orgs.get(0);
    }

    @Override
    public Organisation UpdateOrganisation(Organisation org) throws Exception {

        var existingOrg = GetById(org.getOrganisationId());

        if(existingOrg == null){
            var sql = "INSERT INTO organisations ([name], [address], url) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, org.getName());
                ps.setString(2, org.getAddress());
                ps.setString(3, org.getUrl());
                return ps;
            }, keyHolder);

            org.setOrganisationId(keyHolder.getKey().intValue());
        } else {
            if(!existingOrg.equals(org)){
                jdbcTemplate.update("UPDATE organisations SET [name] = ?, [address] = ?, url = ? WHERE organisationId = ?",
                        org.getName(), org.getAddress(), org.getUrl(), org.getOrganisationId());
            }
        }
        return org;
    }
    @Override
    public void DeleteOrganisation(int id) throws Exception {
        jdbcTemplate.update("DELETE FROM ContactToOrganisation WHERE organisationId = ?", id);
        jdbcTemplate.update("DELETE FROM organisations WHERE organisationId = ?", id);
    }

    @Override
    public List<Organisation> GetAll(int offset, int limit) throws Exception {
        return jdbcTemplate.query(
                "SELECT organisationId, name, address, url " +
                        "FROM organisations " +
                        "ORDER BY OrganisationId " +
                        "OFFSET ? ROWS " +
                        "FETCH NEXT ? ROWS ONLY ",
                (rs, rowNum) -> new Organisation(
                        rs.getInt("organisationId"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("url")
                ),
                offset, limit);
    }
}
