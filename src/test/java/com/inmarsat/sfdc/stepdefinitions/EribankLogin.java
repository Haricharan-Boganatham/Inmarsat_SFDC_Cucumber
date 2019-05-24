package com.inmarsat.sfdc.stepdefinitions;

import org.apache.log4j.Logger;

import com.cucumber.listener.Reporter;
import com.inmarsat.sfdc.framework.DriverManager;
import com.inmarsat.sfdc.pages.EriBankPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;

public class EribankLogin extends MasterStepDefs {

	static Logger log = Logger.getLogger(EribankLogin.class);
	@SuppressWarnings("rawtypes")
	AppiumDriver driver = DriverManager.getAppiumDriver();

	@Given("^I launch eribank$")
	public void i_launch_eribank() throws Throwable {
		if (driver.findElement(EriBankPage.userName).isDisplayed()) {
			log.info("Launched the Application");
			Reporter.addStepLog("Launched the Application");
		}
	}

	@When("^I enter \"([^\"]*)\" and \"([^\"]*)\"$")
	public void i_enter_and(String userName, String password) throws Throwable {
		driver.findElement(EriBankPage.userName).sendKeys(userName);
		driver.findElement(EriBankPage.password).sendKeys(password);
	}

	@Then("^I click LOGIN$")
	public void i_click_LOGIN() throws Throwable {
		driver.findElement(EriBankPage.login).click();
	}

	@Then("^I click LOGOUT$")
	public void i_click_LOGOUT() throws Throwable {
		driver.findElement(EriBankPage.logout).click();
	}
}
