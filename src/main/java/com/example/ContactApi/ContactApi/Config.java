package com.example.ContactApi.ContactApi;

import com.example.ContactApi.ContactApi.Services.ContactService;
import com.example.ContactApi.ContactApi.Services.IContactService;
import com.example.ContactApi.ContactApi.Services.IOrganisationService;
import com.example.ContactApi.ContactApi.Services.OrganisationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public IContactService iContactService(){

        return new ContactService();
    }
    @Bean
    public IOrganisationService iOrganisationService(){
        return new OrganisationService();
    }
}
