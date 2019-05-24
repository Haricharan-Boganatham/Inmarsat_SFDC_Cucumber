package com.inmarsat.sfdc.runners;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.cucumber.listener.ExtentProperties;
import com.cucumber.listener.Reporter;
import com.github.mkolisnyk.cucumber.reporting.CucumberDetailedResults;
import com.github.mkolisnyk.cucumber.reporting.CucumberResultsOverview;
import com.github.mkolisnyk.cucumber.runner.ExtendedCucumberOptions;
import com.inmarsat.sfdc.framework.Settings;
import com.inmarsat.sfdc.framework.TimeStamp;
import com.inmarsat.sfdc.framework.Util;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@ExtendedCucumberOptions(

		jsonReport = "target/cucumber-report/Smoke/cucumber.json", jsonUsageReport = "target/cucumber-report/Smoke/cucumber-usage.json", outputFolder = "target/cucumber-report/Smoke", detailedReport = true, detailedAggregatedReport = true, overviewReport = true, usageReport = true)

/**
 * Please notice that com.inmarsat.sfdc.stepDefinations.CukeHooks class is in
 * the same package as the steps definitions. It has two methods that are
 * executed before or after scenario. I'm using it to delete cookies and take a
 * screenshot if scenario fails.
 */
@CucumberOptions(

		features = "src/test/resources/features", glue = { "com.inmarsat.sfdc.stepdefinitions" }, tags = {
				"@Feature1" }, monochrome = true, plugin = { "pretty", "pretty:target/cucumber-report/Smoke/pretty.txt",
						"html:target/cucumber-report/Smoke", "json:target/cucumber-report/Smoke/cucumber.json",
						"junit:target/cucumber-report/Smoke/cucumber-junitreport.xml",
						"ru.yandex.qatools.allure.cucumberjvm.AllureReporter",
						"com.cucumber.listener.ExtentCucumberFormatter:" })

public class RunCucumberTests_Smoke extends AbstractTestNGCucumberTests {

	static String resultFolder;
	Properties properties = Settings.getInstance();

	@BeforeSuite
	private void beforeSuite() {

		if ((Boolean.parseBoolean(properties.getProperty("SaveReports")))) {
			resultFolder = TimeStamp.getInstance();
		}
	}

	@BeforeClass
	private void beforeClass(ITestContext context) {

		System.out.println(context.getCurrentXmlTest().getParameter("ExecutionMode"));
		ExtentProperties extentProperties = ExtentProperties.INSTANCE;
		extentProperties.setReportPath("target/ExtentReport/InmarsatTestExecutionReport.html");
		new File("target/ExtentReport/screenshots").mkdir();
	}

	@AfterClass
	private void afterClass() {
		Properties properties = Settings.getInstance();
		Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
		Reporter.setSystemInfo("Project Name", properties.getProperty("ProjectName"));
		Reporter.setSystemInfo("user", System.getProperty("user.name"));
		Reporter.setSystemInfo("os", "Windows 7");
	}

	@AfterTest
	private void afterTest() {
		generateCustomReports();
	}

	@AfterSuite()
	private void afterSuite() {
		if ((Boolean.parseBoolean(properties.getProperty("SaveReports")))) {
			copyReportsFolder();
		}
	}

	private void generateCustomReports() {

		CucumberResultsOverview overviewReports = new CucumberResultsOverview();
		overviewReports.setOutputDirectory("target/cucumber-report/Smoke");
		overviewReports.setOutputName("cucumber-results");
		overviewReports.setSourceFile("target/cucumber-report/Smoke/cucumber.json");
		try {
			overviewReports.executeFeaturesOverviewReport();
		} catch (Exception e) {
			e.printStackTrace();
		}

		CucumberDetailedResults detailedResults = new CucumberDetailedResults();
		detailedResults.setOutputDirectory("target/cucumber-report/Smoke");
		detailedResults.setOutputName("cucumber-results");
		detailedResults.setSourceFile("target/cucumber-report/Smoke/cucumber.json");
		detailedResults.setScreenShotLocation("./Smoke");
		try {
			detailedResults.executeDetailedResultsReport(false, true);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void copyReportsFolder() {

		File sourceCucumber = new File(Util.getTargetPath());
		File sourceExtent = new File(Util.getTargetExtentReportPath());
		File destination = new File(resultFolder);
		try {
			FileUtils.copyDirectory(sourceCucumber, destination);
			FileUtils.copyDirectory(sourceExtent, destination);
			// try {
			// FileUtils.cleanDirectory(sourceCucumber);
			// } catch (Exception e) {
			//
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TimeStamp.reportPathWithTimeStamp = null;
	}

}