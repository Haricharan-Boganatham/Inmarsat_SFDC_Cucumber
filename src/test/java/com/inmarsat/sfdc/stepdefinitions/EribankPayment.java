package com.inmarsat.sfdc.stepdefinitions;

import org.apache.log4j.Logger;

import com.inmarsat.sfdc.framework.DriverManager;
import com.inmarsat.sfdc.pages.EriBankPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;

public class EribankPayment extends MasterStepDefs {

	static Logger log = Logger.getLogger(EribankPayment.class);
	@SuppressWarnings("rawtypes")
	AppiumDriver driver = DriverManager.getAppiumDriver();

	@When("^I click Make Payment$")
	public void i_click_Make_Payment() throws Throwable {
		driver.findElement(EriBankPage.makePayment).click();
	}

	@When("^I enter Phone number as \"([^\"]*)\" name as \"([^\"]*)\" Amount as \"([^\"]*)\"$")
	public void i_enter_Phone_number_as_name_as_Amount_as(String phone,
			String name, String amount) throws Throwable {
		driver.findElement(EriBankPage.phone).sendKeys(phone);
		driver.findElement(EriBankPage.name).sendKeys(name);
		driver.findElement(EriBankPage.amount).sendKeys(amount);
	}

	@Then("^I select country as \"([^\"]*)\"$")
	public void i_select_country_as(String country) throws Throwable {
		driver.findElement(EriBankPage.country).sendKeys(country + "\n");
	}

	@Then("^click Send Payment$")
	public void click_Send_Payment() throws Throwable {
		driver.findElement(EriBankPage.sendPayment).click();
		driver.findElement(EriBankPage.confirmPayment).click();
		driver.navigate().back();
	}
}
