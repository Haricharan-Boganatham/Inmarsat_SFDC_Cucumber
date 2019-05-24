Feature: Login 
	As a user, I want to be able to login to the application
	when I present valid credentials

Background:
	Given I am in the login page of the application

@completed1
Scenario Outline: Login with valid username and valid password
	When I login using the valid username <username> and the valid password <password>
	Then I Can see the Inmarsat Home Page

	
	Examples:
	|username	|password	|
	|madhu.sekharmsp@inmarsat.com.test	|Chennai@01		|