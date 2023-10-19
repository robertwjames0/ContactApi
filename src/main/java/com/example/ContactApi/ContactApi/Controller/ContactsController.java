package com.example.ContactApi.ContactApi.Controller;

import com.example.ContactApi.ContactApi.Model.Organisation;
import com.example.ContactApi.ContactApi.Services.IContactService;
import com.example.ContactApi.ContactApi.Model.Contact;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ContactsController {

    @Autowired
    private IContactService contactService;

    @GetMapping("/contacts/all")
    public List<Contact> GetAll(@RequestParam(value = "orgId") int orgId,
                                     @RequestParam(value = "offset") int offset,
                                     @RequestParam(value = "limit")  int limit ){
        try{
            return contactService.GetAll(orgId, offset, limit);
        } catch (Exception e){
            //exception passed for debug purposes - would remove in production to not expose DB info
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organisation not found", e);
        }
    }
    @GetMapping("/contacts/byId")
    public Contact GetById(@RequestParam(value = "id") int id) {
        try{
            var result = contactService.GetById(id);
            if(result == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
            }
            return result;
        } catch (Exception e){
            //exception passed for debug purposes - would remove in production to not expose DB info
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found", e);
        }
    }
    @PostMapping("/contacts/update")
    public Contact Update(@RequestBody Contact contact) {
        try{
            return contactService.UpdateContact(contact);
        } catch (Exception e){
            //exception passed for debug purposes - would remove in production to not expose DB info
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating contact", e);
        }
    }
    @GetMapping("/contacts/delete")
    public void Delete(@RequestParam(value = "id") int id) {
        try{
            contactService.DeleteContact(id);
        } catch (Exception e){
            //exception passed for debug purposes - would remove in production to not expose DB info
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating contact", e);
        }
    }
}