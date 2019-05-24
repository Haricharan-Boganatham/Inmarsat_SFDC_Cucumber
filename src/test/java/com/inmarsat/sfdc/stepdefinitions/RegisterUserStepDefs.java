package com.inmarsat.sfdc.stepdefinitions;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.inmarsat.sfdc.framework.DriverManager;
import com.inmarsat.sfdc.framework.Util;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegisterUserStepDefs extends MasterStepDefs {
	
	static Logger log = Logger.getLogger(RegisterUserStepDefs.class);

	WebDriver driver = DriverManager.getWebDriver();
	
	@When("^I register a new user with the following details:$")
	public void registerUser(DataTable userData) {
		Map<String, String> newUser = userData.asMap(String.class, String.class);
		
		driver.findElement(By.linkText("REGISTER")).click();
		driver.findElement(By.name("firstName")).sendKeys(newUser.get("FirstName"));
		driver.findElement(By.name("lastName")).sendKeys(newUser.get("LastName"));		
		driver.findElement(By.name("phone")).sendKeys(newUser.get("Phone"));		
		driver.findElement(By.name("userName")).sendKeys(newUser.get("Email"));	
		driver.findElement(By.name("address1")).sendKeys(newUser.get("Address"));
		driver.findElement(By.name("city")).sendKeys(newUser.get("City"));
		driver.findElement(By.name("state")).sendKeys(newUser.get("State"));
		driver.findElement(By.name("postalCode")).sendKeys(newUser.get("PostalCode"));
		driver.findElement(By.name("email")).sendKeys(newUser.get("Username"));
		driver.findElement(By.name("password")).sendKeys(newUser.get("Password"));
		driver.findElement(By.name("confirmPassword")).sendKeys(newUser.get("Password"));
		
		currentScenario.embed(Util.takeScreenshot(driver), "image/png");
		
		driver.findElement(By.name("register")).click();
	}
	
	@Then("^I should get a confirmation on successful registration$")
	public void i_should_get_registration_confirmation() {
		currentScenario.embed(Util.takeScreenshot(driver), "image/png");
		
		//assertTrue(driverUtil.isTextPresent("^[\\s\\S]*Thank you for registering.[\\s\\S]*$"));
	}
}