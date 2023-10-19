package com.example.ContactApi.ContactApi.Services;

import com.example.ContactApi.ContactApi.Model.Contact;
import com.example.ContactApi.ContactApi.Model.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;


public class ContactService implements IContactService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Contact> GetAll(int orgId, int offset, int limit) throws Exception {
        var contacts = jdbcTemplate.query(
                "SELECT c.ContactId, c.name, c.email, c.phoneNumber " +
                        "FROM contacts c " +
                        "JOIN contactToOrganisation cto on cto.contactId = c.contactId " +
                        "WHERE cto.organisationId = ? " +
                        "ORDER BY c.contactId " +
                        "OFFSET ? ROWS " +
                        "FETCH NEXT ? ROWS ONLY",
                (rs, rowNum) -> new Contact(
                        rs.getInt("contactId"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phoneNumber")
                ),
                orgId, offset, limit);
        if(contacts.isEmpty()){
            return null;
        }

        return contacts;
    }

    public Contact GetById(int id) throws Exception {
        var contacts = jdbcTemplate.query(
                        "SELECT ContactId, name, email, phoneNumber " +
                                "FROM contacts " +
                                "WHERE contactId = ?",
                        (rs, rowNum) -> new Contact(
                                rs.getInt("contactId"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("phoneNumber")
                                ),
                id);
        if(contacts.isEmpty()){
            return null;
        }

        var contact = contacts.get(0);

        var orgs = jdbcTemplate.query(
                "SELECT o.organisationId, o.name, o.address, o.url " +
                        "FROM ContactToOrganisation cto " +
                        "JOIN Organisations o on cto.organisationId = o.organisationId " +
                        "WHERE cto.contactId = ?",
                (rs, rowNum) -> new Organisation(
                        rs.getInt("organisationId"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("url")
                ),
                id);

        if(!orgs.isEmpty()){
            contact.setOrganisations(orgs);
        }

        return contact;
    }

    private void AddOrgMappings(List<Organisation> orgs, int contactId){
        //trusting org ids are valid here - will throw foreign key exception if they don't exist
        var mapValues = orgs.stream()
                .map(n -> "(" + contactId + "," + n.getOrganisationId() + ") ")
                .collect(Collectors.joining(","));

        jdbcTemplate.update("INSERT INTO ContactToOrganisation (contactId, organisationId) VALUES "
                + mapValues); //trailing comma from earlier
    }

    public Contact UpdateContact(Contact contact) throws Exception {

        var existingContact = GetById(contact.getContactId());

        if(existingContact == null){
            var sql = "INSERT INTO Contacts ([name], email, phoneNumber) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, contact.getName());
                ps.setString(2, contact.getEmail());
                ps.setString(3, contact.getPhoneNumber());
                return ps;
            }, keyHolder);

            contact.setContactId(keyHolder.getKey().intValue());
            if(!contact.getOrganisations().isEmpty()){
                AddOrgMappings(contact.getOrganisations(), contact.getContactId());
            }
            return contact;

        } else {
            if(!existingContact.equals(contact)){
                jdbcTemplate.update("UPDATE Contacts SET [name] = ?, email = ?, phoneNumber = ? WHERE ContactId = ?",
                        contact.getName(), contact.getEmail(), contact.getPhoneNumber(), contact.getContactId());
            }
            var newOrgs = contact.getOrganisations();
            //this section will run multiple queries but should be OK for performance as number of changed
            // orgs should not be very high for a single contact
            //remove deleted orgs
            for (var existingOrg : existingContact.getOrganisations()) {
                if(newOrgs != null && !newOrgs.isEmpty()
                        && newOrgs.stream().noneMatch(x -> existingOrg.getOrganisationId() == x.getOrganisationId())){
                    jdbcTemplate.update("DELETE FROM ContactToOrganisation WHERE ContactId = ? AND OrganisationId = ?",
                            contact.getContactId(), existingOrg.getOrganisationId());
                }
            }
            //add new orgs
            if(newOrgs != null){
                var toAdd = newOrgs.stream()
                        .filter(x -> existingContact.getOrganisations().stream()
                                .noneMatch(y -> y.getOrganisationId() == x.getOrganisationId()))
                        .toList();
                if(!toAdd.isEmpty()){
                    AddOrgMappings(toAdd, contact.getContactId());
                }
            }
            return contact;
        }
    }
    public void DeleteContact(int id) throws Exception {
        jdbcTemplate.update("DELETE FROM ContactToOrganisation WHERE ContactId = ?", id);
        jdbcTemplate.update("DELETE FROM Contacts WHERE ContactId = ?", id);
    }
}
