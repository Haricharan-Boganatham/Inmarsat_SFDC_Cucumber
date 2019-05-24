package com.inmarsat.sfdc.stepdefinitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inmarsat.sfdc.framework.APIReusuableLibrary.ASSERT_RESPONSE;
import com.inmarsat.sfdc.framework.APIReusuableLibrary.COMPARISON;
import com.inmarsat.sfdc.framework.APIReusuableLibrary.SERVICEFORMAT;
import com.inmarsat.sfdc.framework.APIReusuableLibrary.SERVICEMETHOD;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;

public class FetchUserDetailsStepDefs extends MasterStepDefs {

	ValidatableResponse response;
	String uri = properties.getProperty("Endpoint2");
	String uri1 = properties.getProperty("Endpoint3");
	Map<String, String> headersMap;
	String postBodyContent;

	@Given("^the required inputs are ready to validate users$")
	public void the_required_inputs_are_ready_to_validate_users() throws Throwable {

		postBodyContent = apiDriver.readInput(properties.getProperty("InputTemplate3"));

	}

	@When("^user request the users data end point with value \"([^\"]*)\" and \"([^\"]*)\"$")
	public void user_request_the_users_data_end_point_with_value(String name, String job) throws Throwable {

		postBodyContent = apiDriver.updateContent(postBodyContent, "update_name", name);
		postBodyContent = apiDriver.updateContent(postBodyContent, "update_job", job);

		response = apiDriver.sendNReceive(uri, SERVICEMETHOD.POST, SERVICEFORMAT.JSON, postBodyContent, headersMap,
				201);
	}

	@Then("^user should get the entered name  \"([^\"]*)\" and \"([^\"]*)\"$")
	public void user_should_get_the_entered_name(String name, String job) throws Throwable {

		apiDriver.assertIt(uri, response, ASSERT_RESPONSE.TAG, "name", name, COMPARISON.IS_EQUALS);
		apiDriver.assertIt(uri, response, ASSERT_RESPONSE.TAG, "job", job, COMPARISON.IS_EQUALS);
	}

	@And("^assert json body,header,tag$")
	public void assert_json_body_header_tag() throws Throwable {
		String expectedResponse = apiDriver.readInput(properties.getProperty("OutputTemplate3"));
		apiDriver.assertIt(uri, response, ASSERT_RESPONSE.TAG, "name", "murug", COMPARISON.IS_EQUALS);
		apiDriver.assertIt(uri, response, ASSERT_RESPONSE.HEADER, "", "application/json;", COMPARISON.IS_EXISTS);
		apiDriver.assertIt(uri, response, ASSERT_RESPONSE.BODY, "", expectedResponse, COMPARISON.IS_EQUALS);
	}

	@Given("^the required inputs are ready to validate list$")
	public void the_required_inputs_are_ready_to_validate_list() throws Throwable {

	}

	@When("^user request the users data end point$")
	public void user_request_the_users_data_end_point() throws Throwable {

		response = apiDriver.sendNReceive(uri1, SERVICEMETHOD.GET, headersMap, 200);
	}

	@Then("^user should get the expected response$")
	public void user_should_get_the_expected_response() throws Throwable {

	}

	@Then("^assert json list of values$")
	public void assert_json_list_of_values() throws Throwable {
		Object expectedList = new ArrayList<String>();
		expectedList = getList();
		apiDriver.assertIt(uri, response, ASSERT_RESPONSE.LIST, "MRData.CircuitTable.Circuits.circuitId", expectedList,
				COMPARISON.IS_EQUALS);

	}

	public List<String> getList() {
		List<String> sampleList = new ArrayList<String>();
		sampleList.add("albert_park");
		sampleList.add("americas");
		sampleList.add("bahrain");
		sampleList.add("BAK");
		sampleList.add("catalunya");
		sampleList.add("hungaroring");
		sampleList.add("interlagos");
		sampleList.add("marina_bay");
		sampleList.add("monaco");
		sampleList.add("monza");
		sampleList.add("red_bull_ring");
		sampleList.add("rodriguez");
		sampleList.add("sepang");
		sampleList.add("shanghai");
		sampleList.add("silverstone");
		sampleList.add("sochi");
		sampleList.add("spa");
		sampleList.add("suzuka");
		sampleList.add("villeneuve");
		sampleList.add("yas_marina");
		return sampleList;
	}

}
