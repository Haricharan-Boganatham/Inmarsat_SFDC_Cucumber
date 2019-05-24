package com.inmarsat.sfdc.stepdefinitions;

import static org.testng.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.inmarsat.sfdc.framework.DriverManager;
import com.inmarsat.sfdc.framework.Util;

import cucumber.api.java.en.Given;

public class GeneralStepDefs extends MasterStepDefs {
	
	static Logger log = Logger.getLogger(GeneralStepDefs.class);

	WebDriver driver = DriverManager.getWebDriver();

	@Given("^I am in the login page of the application$")
	public void i_am_in_login_page() {
		driver.get(properties.getProperty("ApplicationUrl"));
		
		currentScenario.embed(Util.takeScreenshot(driver), "image/png");
		driver.manage().window().maximize();
		
		//assertTrue(driver.getTitle().contains("Welcome") ||
						//driver.getTitle().contains("Sign-on"));
	}
}