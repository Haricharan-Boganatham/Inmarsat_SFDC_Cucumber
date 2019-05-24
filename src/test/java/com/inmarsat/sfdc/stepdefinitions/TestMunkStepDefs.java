package com.inmarsat.sfdc.stepdefinitions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cucumber.listener.Reporter;
import com.inmarsat.sfdc.framework.DriverManager;
import com.inmarsat.sfdc.pages.TestMunkPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;

public class TestMunkStepDefs extends MasterStepDefs {

	static Logger log = Logger.getLogger(TestMunkStepDefs.class);

	@SuppressWarnings("rawtypes")
	AppiumDriver driver = DriverManager.getAppiumDriver();

	@Given("^Application is Launched$")
	public void application_is_Launched() throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement logo = driver.findElement(TestMunkPage.txtUsername);
		wait.until(ExpectedConditions.visibilityOf(logo));
		log.info("Application launched sucesfully");
		Reporter.addStepLog("Application launched sucesfully");
	}

	@When("^I SignIn with valid username as (.*) and password as (.*)$")
	public void i_SignIn_with_valid_username_as_and_password_as(String userName, String password) throws Throwable {

		driver.findElement(TestMunkPage.txtUsername).sendKeys(userName);
		driver.findElement(TestMunkPage.txtPassword).sendKeys(password);

		driver.findElement(TestMunkPage.btnSignIn).click();
	}

	@Then("^Home Page of the application is displayed$")
	public void home_Page_of_the_application_is_displayed() throws Throwable {

		if (driver.findElement(TestMunkPage.lblHome).isDisplayed()) {
			log.info("Home page displayed sucesfully");
			Reporter.addStepLog("Home page displayed sucesfully");
		}
	}

	@And("^Perform Operations$")
	public void perform_Operations() throws Throwable {
		driver.findElement(TestMunkPage.btnSkip).click();
		driver.findElement(TestMunkPage.btnSecond).click();
		driver.findElement(TestMunkPage.btnAlert).click();
		driver.findElement(TestMunkPage.btnDismiss).click();
		driver.findElement(TestMunkPage.btnTable).click();
		driver.findElement(TestMunkPage.btnSection).click();
		driver.findElement(TestMunkPage.btnBack).click();
		driver.findElement(TestMunkPage.btnHome).click();
	}

}
