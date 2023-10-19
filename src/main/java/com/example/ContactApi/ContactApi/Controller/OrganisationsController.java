package com.example.ContactApi.ContactApi.Controller;

import com.example.ContactApi.ContactApi.Model.Organisation;
import com.example.ContactApi.ContactApi.Services.IOrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class OrganisationsController {

    @Autowired
    private IOrganisationService organisationService;

    @GetMapping("/organisations/all")
    public List<Organisation> GetAll(@RequestParam(value = "offset") int offset,
                                     @RequestParam(value = "limit")  int limit ){
        try{
            return organisationService.GetAll(offset, limit);
        } catch (Exception e){
            //exception passed for debug purposes - would remove in production to not expose DB info
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organisation not found", e);
        }
    }

    @GetMapping("/organisations/byId")
    public Organisation GetById(@RequestParam(value = "id") int id) {
        try{
            var result = organisationService.GetById(id);
            if(result == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organisation not found");
            }
            return result;
        } catch (Exception e){
            //exception passed for debug purposes - would remove in production to not expose DB info
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organisation not found", e);
        }
    }
    @PostMapping("/organisations/update")
    public Organisation Update(@RequestBody Organisation organisation) {
        try{
            return organisationService.UpdateOrganisation(organisation);
        } catch (Exception e){
            //exception passed for debug purposes - would remove in production to not expose DB info
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating organisation", e);
        }
    }
    @GetMapping("/organisations/delete")
    public void Delete(@RequestParam(value = "id") int id) {
        try{
            organisationService.DeleteOrganisation(id);
        } catch (Exception e){
            //exception passed for debug purposes - would remove in production to not expose DB info
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating organisation", e);
        }
    }
}