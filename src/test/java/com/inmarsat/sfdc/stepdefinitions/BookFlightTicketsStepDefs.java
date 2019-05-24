package com.inmarsat.sfdc.stepdefinitions;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.inmarsat.sfdc.framework.DriverManager;
import com.inmarsat.sfdc.framework.Util;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class BookFlightTicketsStepDefs extends MasterStepDefs {

	static Logger log = Logger.getLogger(BookFlightTicketsStepDefs.class);
	WebDriver driver = DriverManager.getWebDriver();

	private int passengerCount;

	@Given("^I search for flights using the following criteria:$")
	public void i_search_for_flights(DataTable searchCriteriaData) {
		List<Map<String, String>> searchCriteria = searchCriteriaData.asMaps(String.class, String.class);
		Map<String, String> searchCriteria1 = searchCriteria.get(0);

		passengerCount = Integer.parseInt(searchCriteria1.get("PassengerCount"));
		driver.findElement(By.name("passCount")).sendKeys(searchCriteria1.get("PassengerCount"));
		driver.findElement(By.name("fromPort")).sendKeys(searchCriteria1.get("FromPort"));
		driver.findElement(By.name("fromMonth")).sendKeys(searchCriteria1.get("FromMonth"));
		driver.findElement(By.name("fromDay")).sendKeys(searchCriteria1.get("FromDate"));
		driver.findElement(By.name("toPort")).sendKeys(searchCriteria1.get("ToPort"));
		driver.findElement(By.name("toMonth")).sendKeys(searchCriteria1.get("ToMonth"));
		driver.findElement(By.name("toDay")).sendKeys(searchCriteria1.get("ToDate"));
		driver.findElement(By.name("airline")).sendKeys(searchCriteria1.get("Airline"));

		currentScenario.embed(Util.takeScreenshot(driver), "image/png");

		driver.findElement(By.name("findFlights")).click();
	}

	@And("^I select the first available flight$")
	public void i_select_first_available_flight() {
		currentScenario.embed(Util.takeScreenshot(driver), "image/png");

		driver.findElement(By.name("reserveFlights")).click();
	}

	@And("^I book the tickets using the following passenger details:$")
	public void i_book_tickets(DataTable passengerInfoData) {
		List<Map<String, String>> passengerInfo = passengerInfoData.asMaps(String.class, String.class);

		for (int i = 0; i < passengerCount; i++) {
			Map<String, String> currentPassengerInfo = passengerInfo.get(i);

			driver.findElement(By.name("passFirst" + i)).sendKeys(currentPassengerInfo.get("FirstName"));
			driver.findElement(By.name("passLast" + i)).sendKeys(currentPassengerInfo.get("LastName"));
		}
	}

	@And("^I use the following credit card details:$")
	public void i_use_credit_card(DataTable creditCardInfoData) {
		List<Map<String, String>> creditCardInfo = creditCardInfoData.asMaps(String.class, String.class);
		Map<String, String> creditCardInfo1 = creditCardInfo.get(0);

		driver.findElement(By.name("creditCard")).sendKeys(creditCardInfo1.get("CreditCardType"));
		driver.findElement(By.name("creditnumber")).sendKeys(creditCardInfo1.get("CreditCardNumber"));

		currentScenario.embed(Util.takeScreenshot(driver), "image/png");

		driver.findElement(By.name("buyFlights")).click();
	}

	@Then("^I should get a booking confirmation with a confirmation number$")
	public void i_should_get_booking_confirmation() {
		currentScenario.embed(Util.takeScreenshot(driver), "image/png");

		//assertTrue(driverUtil.isTextPresent("^[\\s\\S]*Your itinerary has been booked![\\s\\S]*$"));

		WebElement flightConfirmation = driver.findElement(By.cssSelector("font > font > b > font"));

		String flightConfirmationNumber = flightConfirmation.getText();
		flightConfirmationNumber = flightConfirmationNumber.split("#")[1].trim();
		currentScenario.write("The confirmation number is: " + flightConfirmationNumber);

		// driver.findElement(By.xpath("//a/img")).click();
	}
}