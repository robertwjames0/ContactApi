create table Contacts (ContactId int NOT NULL IDENTITY(1,1) PRIMARY KEY, [name] varchar(255), email varchar(255), phoneNumber varchar(255))

insert into Contacts ([name], email, phoneNumber) values ('contact 1', 'contact_1_email@example.com', '01 555-5555'), 
('contact 2', 'contact_2_email@example.com', '02 555-5555'),
('contact 3', 'contact_3_email@example.com', '03 555-5555')

create table Organisations (OrganisationId int NOT NULL IDENTITY(1,1) PRIMARY KEY, [name] varchar(255), [address] varchar(255), url varchar(255))
insert into Organisations ([name], [address], url) values 
('org 1', 'org 1 address', 'org1.example.com'), 
('org 2', 'org 2 address', 'org2.example.com'),
('org 3', 'org 3 address', 'org3.example.com') 

create table ContactToOrganisation (
	ContactToOrganisationId int NOT NULL IDENTITY (1,1) PRIMARY KEY, 
	ContactId int NOT NULL REFERENCES Contacts(ContactId), 
	OrganisationId int NOT NULL REFERENCES Organisations(OrganisationId)
)
insert into ContactToOrganisation (contactId, organisationId) values
(1, 1),
(1, 2),
(1, 3),
(2, 2)

