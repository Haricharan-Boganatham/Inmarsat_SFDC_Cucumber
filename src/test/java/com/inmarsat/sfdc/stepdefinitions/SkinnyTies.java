package com.inmarsat.sfdc.stepdefinitions;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cucumber.listener.Reporter;
import com.inmarsat.sfdc.framework.DriverManager;
import com.inmarsat.sfdc.pages.TiesPages;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class SkinnyTies extends MasterStepDefs {

	static Logger log = Logger.getLogger(SkinnyTies.class);


	WebDriver driver = DriverManager.getWebDriver();

	@Given("^I Launch the Application$")
	public void i_Launh_the_Application() throws Throwable {

		driver.get(properties.getProperty("ApplicationUrlRWD"));

		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement logo = driver.findElement(TiesPages.collections);
		wait.until(ExpectedConditions.visibilityOf(logo));
		log.info("Application launched sucesfully");
		Reporter.addStepLog("Application launched sucesfully");
	}

	@And("^Navigate to Collections and select the tie$")
	public void navigate_to_Collections_and_select_the_tie() throws Throwable {
		driver.findElement(By.xpath("//*[@id='nav']/ol/li[1]/a")).click();
		driver.findElement(TiesPages.arrivals).click();
	}

	@Then("^I select the Quantity$")
	public void i_select_the_Quantity() throws Throwable {
		driver.findElement(TiesPages.select_tie).click();
		driver.findElement(TiesPages.quantity).click();
	}

	@And("^provide details and checkout with all details (.*) (.*) (.*) (.*)$")
	public void provide_details_and_checkout_with_all_details_John_abc_gmail_com_Is_shipping_option_available_for_India(
			String name, String email, String phone, String message) throws Throwable {

		PauseScript(3);
		driver.findElement(TiesPages.add_to_cart).click();
		PauseScript(3);
		driver.findElement(TiesPages.checkout).click();

		TiesPages tiesPage = new TiesPages(driver);
		tiesPage.provideDetails(name, email, phone, message);
	}

}
