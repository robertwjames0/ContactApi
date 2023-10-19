package com.example.ContactApi.ContactApi.Services;

import com.example.ContactApi.ContactApi.Model.Contact;

import java.util.List;

public interface IContactService {
    Contact GetById(int id) throws Exception;
    Contact UpdateContact(Contact contact) throws Exception;
    void DeleteContact(int id) throws Exception;
    List<Contact> GetAll(int orgId, int offset, int limit) throws Exception;

}
