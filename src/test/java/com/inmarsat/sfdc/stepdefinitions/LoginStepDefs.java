package com.inmarsat.sfdc.stepdefinitions;

import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.inmarsat.sfdc.framework.DriverManager;
import com.inmarsat.sfdc.framework.Util;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import com.inmarsat.sfdc.pages.*;

public class LoginStepDefs extends MasterStepDefs {

	static Logger log = Logger.getLogger(LoginStepDefs.class);
	WebDriver driver = DriverManager.getWebDriver();
	LoginSalesforce login;

	@When("^I login using the valid username (.*) and the valid password (.*)$")
	public void i_login_using_valid_username_valid_password(String username, String password) {
		login = new LoginSalesforce(driver);
		login.entercred(username, password);
	}

	@Then("^I Can see the Inmarsat Home Page$")
	public void i_Can_see_the_Inmarsat_Home_Page() {
		login = new LoginSalesforce(driver);
		login.getHomepage_Title();
	}

	@Then("^The application should stay on the login page, and not log me in")
	public void application_should_stay_on_login_page() {
		currentScenario.embed(Util.takeScreenshot(driver), "image/png");

		assertTrue(driver.getTitle().contains("Sign-on") || driver.getTitle().contains("Welcome"));
	}

}
