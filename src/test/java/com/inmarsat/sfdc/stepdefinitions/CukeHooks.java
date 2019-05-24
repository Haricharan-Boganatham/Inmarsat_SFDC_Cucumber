package com.inmarsat.sfdc.stepdefinitions;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.cucumber.listener.Reporter;
import com.inmarsat.sfdc.framework.DriverManager;
import com.inmarsat.sfdc.framework.Settings;
import com.inmarsat.sfdc.framework.TestHarness;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CukeHooks extends MasterStepDefs {

	static Logger log = Logger.getLogger(CukeHooks.class);

	static Properties propertiesFile = Settings.getInstance();
	private TestHarness testHarness;

	@Before
	public void setUp(Scenario scenario) {

		testHarness = new TestHarness();
		Reporter.addScenarioLog(scenario.getName());
		currentScenario = scenario;
		properties = propertiesFile;
		PauseScript(2);
		currentTestParameters = DriverManager.getTestParameters();
		currentTestParameters.setScenarioName(scenario.getName());
		testHarness.invokeDriver(scenario, currentTestParameters);
	}

	@After
	public void tearDown(Scenario scenario) throws IOException {

		testHarness.updateDefectInJira(scenario, currentTestParameters);
		testHarness.downloadAddtionalReports(currentTestParameters, scenario);
		testHarness.closeRespestiveDriver(currentTestParameters, scenario);
	}

}