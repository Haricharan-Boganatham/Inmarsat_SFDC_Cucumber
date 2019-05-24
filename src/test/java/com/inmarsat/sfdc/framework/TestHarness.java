package com.inmarsat.sfdc.framework;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.experitest.selenium.MobileWebDriver;

import cucumber.api.Scenario;
import io.appium.java_client.AppiumDriver;

public class TestHarness {

	static Logger log = Logger.getLogger(TestHarness.class);
	private Properties properties = Settings.getInstance();

	/**
	 * Constructor to initialize the {@link TestHarness} object
	 */
	public TestHarness() {
		log.info("Starting test execution");
	}

	@SuppressWarnings("rawtypes")
	public void invokeDriver(Scenario scenario, SeleniumTestParameters currentTestParameters) {
		WebDriver driver;
		AppiumDriver appiumDriver;
		log.info("Running the Scenario : " + scenario.getName() + " in " + currentTestParameters.getExecutionMode());

		switch (currentTestParameters.getExecutionMode()) {

		case API:
			break;
		case LOCAL:
		case GRID:
			driver = DriverFactory.createWebDriverInstance(currentTestParameters);
			DriverManager.setWebDriver(driver);
			break;
			
		case MOBILE:

			if (currentTestParameters.getMobileToolName().equals(ToolName.APPIUM)) {
				appiumDriver = DriverFactory.createAppiumInstance(currentTestParameters);
				DriverManager.setAppiumDriver(appiumDriver);
			} else if (currentTestParameters.getMobileToolName().equals(ToolName.REMOTEDRIVER)) {
				driver = DriverFactory.createWebDriverInstance(currentTestParameters);
				DriverManager.setWebDriver(driver);
			}
			break;

		case SAUCELABS:
		case FASTEST:
		case PERFECTO:
			if (currentTestParameters.getMobileToolName().equals(ToolName.APPIUM)) {
				appiumDriver = DriverFactory.createAppiumInstance(currentTestParameters);
				DriverManager.setAppiumDriver(appiumDriver);
			} else if (currentTestParameters.getMobileToolName().equals(ToolName.REMOTEDRIVER)) {
				driver = DriverFactory.createWebDriverInstance(currentTestParameters);
				DriverManager.setWebDriver(driver);
			}
			break;

		case TESTOBJECT:
			appiumDriver = DriverFactory.createAppiumInstance(currentTestParameters);
			DriverManager.setAppiumDriver(appiumDriver);
			break;

		case SEETEST:
			MobileWebDriver seetestDriver = DriverFactory.createInstanceSeetestDriver(currentTestParameters);
			DriverManager.setSeetestDriver(seetestDriver);
			break;
			
		default:
			break;
		}
	}

	public void downloadAddtionalReports(SeleniumTestParameters testParameters, Scenario scenario) {
		if (Boolean.valueOf(properties.getProperty("PerfectoReportGeneration"))
				&& testParameters.getExecutionMode().equals(ExecutionMode.PERFECTO)) {
			capturePerfectoReports(scenario, testParameters, testParameters.isMobileExecution());
		}

	}

	@SuppressWarnings("rawtypes")
	public void closeRespestiveDriver(SeleniumTestParameters testParameters, Scenario scenario) {

		if (testParameters.isMobileExecution()) {
			if (testParameters.getExecutionMode().equals(ExecutionMode.SEETEST)) {
				MobileWebDriver driver = DriverManager.getSeetestDriver();
				driver.client.releaseDevice("*", true, false, true);
				driver.client.releaseClient();
				driver.quit();
			} else {
				AppiumDriver driver = DriverManager.getAppiumDriver();
				if (driver != null) {
					driver.quit();
				}
			}
		} else if (!testParameters.isAPIExecution()) {
			WebDriver driver = DriverManager.getWebDriver();
			if (driver != null) {
				driver.quit();
			}
		}

	}

	/**
	 * Embed a screenshot in test report if test is marked as failed And Update
	 * Task in JIRA
	 * 
	 * @param testParameters
	 * 
	 * @throws IOException
	 */
	public void updateDefectInJira(Scenario scenario, SeleniumTestParameters testParameters) throws IOException {

		if (Boolean.valueOf(properties.getProperty("TrackIssuesInJira"))) {
			if (scenario.isFailed()) {
				try {
					if (testParameters.isMobileExecution()) {
						if (testParameters.getExecutionMode() == ExecutionMode.SEETEST) {

							byte[] screenshot = Util.takeScreenshot(DriverManager.getSeetestDriver());
							scenario.embed(screenshot, "image/png");
						} else {
							byte[] screenshot = Util.takeScreenshot(DriverManager.getAppiumDriver());
							scenario.embed(screenshot, "image/png");
						}
					} else {
						byte[] screenshot = Util.takeScreenshot(DriverManager.getWebDriver());
						scenario.embed(screenshot, "image/png");
					}

					File filePath = ((TakesScreenshot) DriverManager.getWebDriver()).getScreenshotAs(OutputType.FILE);
					RestApiForJira.createLog(scenario.getName(), scenario.getName(), filePath.toString());

				} catch (WebDriverException somePlatformsDontSupportScreenshots) {
					somePlatformsDontSupportScreenshots.printStackTrace();
					log.error(somePlatformsDontSupportScreenshots.getMessage());
				}
			}
		}
	}

	private String getFileName(Browser browser, String deviceName) {
		String fileName = "";
		if (browser == null) {
			fileName = deviceName;
		} else if (deviceName == null) {
			fileName = browser.toString();
		} else {
			fileName = "report";
		}
		return fileName;
	}

	@SuppressWarnings("rawtypes")
	private void capturePerfectoReports(Scenario scenario, SeleniumTestParameters testParameters,
			boolean isMobileExecution) {
		if (isMobileExecution) {
			try {
				AppiumDriver driver = DriverManager.getAppiumDriver();
				driver.close();
				String Udid;

				if (!(driver.getCapabilities().getCapability("model") == null)) {
					Udid = driver.getCapabilities().getCapability("model").toString();
				} else {
					Udid = driver.getCapabilities().getCapability("deviceName").toString();
				}
				PerfectoLabUtils.downloadReport(driver, "pdf",
						Util.getResultsPath() + Util.getFileSeparator() + scenario.getName() + "_" + Udid);
			} catch (IOException e) {
				e.printStackTrace();
				log.error("Problem while downloading Perfecto Report for " + scenario.getName());

			}
		} else {
			WebDriver driver = DriverManager.getWebDriver();
			driver.close();

			Map<String, Object> params = new HashMap<String, Object>();
			((RemoteWebDriver) driver).executeScript("mobile:execution:close", params);
			params.clear();

			try {
				PerfectoLabUtils.downloadReport((RemoteWebDriver) driver, "pdf",
						Util.getResultsPath() + Util.getFileSeparator() + scenario.getName() + "_"
								+ getFileName(testParameters.getBrowser(), testParameters.getDeviceName()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
