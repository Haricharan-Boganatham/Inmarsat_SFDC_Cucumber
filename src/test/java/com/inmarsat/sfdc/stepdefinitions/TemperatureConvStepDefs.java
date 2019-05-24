package com.inmarsat.sfdc.stepdefinitions;

import java.util.Map;

import com.inmarsat.sfdc.framework.APIReusuableLibrary.ASSERT_RESPONSE;
import com.inmarsat.sfdc.framework.APIReusuableLibrary.COMPARISON;
import com.inmarsat.sfdc.framework.APIReusuableLibrary.SERVICEFORMAT;
import com.inmarsat.sfdc.framework.APIReusuableLibrary.SERVICEMETHOD;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;

public class TemperatureConvStepDefs extends MasterStepDefs {

	ValidatableResponse response;
	String uri = properties.getProperty("Endpoint1");
	Map<String, String> headersMap;
	String postBodyContentFtoC;
	String postBodyContentCtoF;
	String celciusFromResponse;

	@Given("^the required inputs are ready$")
	public void the_required_inputs_are_ready() throws Throwable {
		headersMap = headers.getHeaders2();
		postBodyContentFtoC = apiDriver.readInput(properties.getProperty("InputTemplate1"));
	}

	@When("^user request the temp conversion end point with Fahrenheit value \"([^\"]*)\"$")
	public void user_request_the_temp_conversion_end_point_with_Fahrenheit_value(String Fahrenheit) throws Throwable {

		postBodyContentFtoC = apiDriver.updateContent(postBodyContentFtoC, "update_fahrenheit", Fahrenheit);
		response = apiDriver.sendNReceive(uri, SERVICEMETHOD.POST, SERVICEFORMAT.XML, postBodyContentFtoC, headersMap,
				200);

	}

	@Then("^user should get the celcius value \"([^\"]*)\"$")
	public void user_should_get_the_celcius_value(String celcius) throws Throwable {

		celciusFromResponse = apiDriver.extractValue(response, "//FahrenheitToCelsiusResult/text()");
		apiDriver.assertIt(uri, response, ASSERT_RESPONSE.TAG, "//FahrenheitToCelsiusResult/text()", celcius,
				COMPARISON.IS_EQUALS);

	}

	@Then("^assert xml body,header,tag$")
	public void assert_xml_body_header_tag() throws Throwable {
		String expectedResponse = apiDriver.readInput(properties.getProperty("OutputTemplate1"));
		expectedResponse = apiDriver.updateContent(expectedResponse, "update_celsius", celciusFromResponse);
		apiDriver.assertIt(uri, response, ASSERT_RESPONSE.BODY, "", expectedResponse, COMPARISON.IS_EQUALS);
		apiDriver.assertIt(uri, response, ASSERT_RESPONSE.TAG, "//FahrenheitToCelsiusResult/text()",
				celciusFromResponse, COMPARISON.IS_EQUALS);
		apiDriver.assertIt(uri, response, ASSERT_RESPONSE.HEADER, "", "text/xml;", COMPARISON.IS_EXISTS);
	}

	@Given("^user upadte the celcius value$")
	public void user_upadte_the_celcius_value() throws Throwable {

		postBodyContentCtoF = apiDriver.readInput(properties.getProperty("InputTemplate2"));
		postBodyContentCtoF = apiDriver.updateContent(postBodyContentCtoF, "update_celsius", celciusFromResponse);
	}

	@Then("^user request the temp conversion end point with Celcius value$")
	public void user_request_the_temp_conversion_end_point_with_Celcius_value() throws Throwable {
		response = apiDriver.sendNReceive(uri, SERVICEMETHOD.POST, SERVICEFORMAT.XML, postBodyContentCtoF, headersMap,
				200);

	}

	@Then("^user should get the Fahrenheit value \"([^\"]*)\"$")
	public void user_should_get_the_Fahrenheit_value(String arg1) throws Throwable {
		apiDriver.assertIt(uri, response, ASSERT_RESPONSE.TAG, "//CelsiusToFahrenheitResult/text()", "50",
				COMPARISON.IS_EQUALS);

	}

}
