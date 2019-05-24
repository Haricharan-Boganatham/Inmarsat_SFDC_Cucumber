package com.inmarsat.sfdc.framework;

import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.internal.BaseTestMethod;

/**
 * Will be called before every TestNG Method This Implements
 * IInvokedMethodListener {@link IInvokedMethodListener}
 * 
 * @author Inmarsat
 */
public class WebDriverListener implements IInvokedMethodListener {

	static Logger log = Logger.getLogger(WebDriverListener.class);

	private static Properties properties = Settings.getInstance();;

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

		SeleniumTestParameters testParameters = new SeleniumTestParameters();

		log.debug("BEGINNING: com.inmarsat.sfdc.supportLibraries.WebDriverListener-beforeInvocation");
		if (method.isTestMethod()) {
			try {
				setDefaultTestParameters(method, testParameters);
				DriverManager.setTestParameters(testParameters);

			} catch (Exception ex) {
				log.error(ex.getMessage());
				ex.printStackTrace();
			}
		}
		log.debug("END: com.inmarsat.sfdc.supportLibraries.WebDriverListener-beforeInvocation");
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {

		log.debug("BEGINNING: WebDriverListener.afterInvocation");
		/*
		 * change the name of the test method that will appear in the report to
		 * one that will contain very handy when analysing results.
		 */
		if (method.isTestMethod()) {
			try {
				BaseTestMethod bm = (BaseTestMethod) testResult.getMethod();
				Field f = bm.getClass().getSuperclass().getDeclaredField("m_methodName");
				f.setAccessible(true);
				String newTestName = testResult.getTestContext().getCurrentXmlTest().getName() + " - "
						+ bm.getMethodName() + " - ";
				log.info("Renaming test method name from: '" + bm.getMethodName() + "' to: '" + newTestName);
				f.set(bm, newTestName);
			} catch (Exception ex) {
				log.info("afterInvocation exception:\n" + ex.getMessage());
				ex.printStackTrace();
			}
		}
		log.debug("END: WebDriverListener.afterInvocation");
	}

	private void setDefaultTestParameters(IInvokedMethod method, SeleniumTestParameters testParameters) {

		try {
			String executionMode = method.getTestMethod().getXmlTest().getLocalParameters().get("ExecutionMode");
			String toolName = method.getTestMethod().getXmlTest().getLocalParameters().get("ToolName");

			isMobileExecution(executionMode, toolName, testParameters);
			isAPIExecution(executionMode, toolName, testParameters);

			switch (executionMode) {

			case "API":

				testParameters.setExecutionMode(ExecutionMode.valueOf(executionMode));

				break;

			case "LOCAL":

				testParameters.setExecutionMode(ExecutionMode.valueOf(executionMode));

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserName") == null) {
					testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));

				} else {
					testParameters.setBrowser(Browser
							.valueOf(method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserName")));
				}

				break;

			case "GRID":

				testParameters.setExecutionMode(ExecutionMode.valueOf(executionMode));

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserName") == null) {
					testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));

				} else {
					testParameters.setBrowser(Browser
							.valueOf(method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserName")));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserVersion") == null) {
					testParameters.setBrowserVersion(properties.getProperty("DefaultBrowserVersion"));

				} else {
					testParameters.setBrowserVersion(
							method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserVersion"));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("Platform") == null) {
					testParameters.setPlatform(Platform.valueOf(properties.getProperty("DefaultPlatform")));

				} else {
					testParameters.setBrowserVersion(
							method.getTestMethod().getXmlTest().getLocalParameters().get("Platform"));
				}

				break;
			case "MOBILE":

				testParameters.setExecutionMode(ExecutionMode.valueOf(executionMode));

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("ToolName") == null) {
					testParameters.setMobileToolName(ToolName.valueOf(properties.getProperty("DefaultToolName")));
				} else {
					String mobileToolName = method.getTestMethod().getXmlTest().getLocalParameters().get("ToolName");
					testParameters.setMobileToolName((ToolName.valueOf(mobileToolName)));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("MobileExecutionPlatform") == null) {
					testParameters.setMobileExecutionPlatform(
							MobileExecutionPlatform.valueOf(properties.getProperty("DefaultMobileExecutionPlatform")));
				} else {
					String mobileExecutionPlatform = method.getTestMethod().getXmlTest().getLocalParameters()
							.get("MobileExecutionPlatform");
					testParameters
							.setMobileExecutionPlatform((MobileExecutionPlatform.valueOf(mobileExecutionPlatform)));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName") == null) {
					testParameters.setDeviceName(properties.getProperty("DefaultDeviceName"));

				} else {
					testParameters
							.setDeviceName(method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName"));
				}

				break;

			case "PERFECTO":

				testParameters.setExecutionMode(ExecutionMode.valueOf(executionMode));

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("ToolName") == null) {
					testParameters.setMobileToolName(ToolName.valueOf(properties.getProperty("DefaultToolName")));
				} else {
					String mobileToolName = method.getTestMethod().getXmlTest().getLocalParameters().get("ToolName");
					testParameters.setMobileToolName((ToolName.valueOf(mobileToolName)));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("MobileExecutionPlatform") == null) {
					testParameters.setMobileExecutionPlatform(
							MobileExecutionPlatform.valueOf(properties.getProperty("DefaultMobileExecutionPlatform")));
				} else {
					String mobileExecutionPlatform = method.getTestMethod().getXmlTest().getLocalParameters()
							.get("MobileExecutionPlatform");
					testParameters
							.setMobileExecutionPlatform((MobileExecutionPlatform.valueOf(mobileExecutionPlatform)));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName") == null) {
					testParameters.setDeviceName(properties.getProperty("DefaultDeviceName"));

				} else {
					testParameters
							.setDeviceName(method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName"));
				}

				// For One Lab Integration - Desktop Browser Execution

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserName") == null) {
					testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));

				} else {
					testParameters.setBrowser(Browser
							.valueOf(method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserName")));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserVersion") == null) {
					testParameters.setBrowserVersion(properties.getProperty("DefaultBrowserVersion"));

				} else {
					testParameters.setBrowserVersion(
							method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserVersion"));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("Platform") == null) {
					testParameters.setPlatform(Platform.valueOf(properties.getProperty("DefaultPlatform")));

				} else {
					testParameters.setBrowserVersion(
							method.getTestMethod().getXmlTest().getLocalParameters().get("Platform"));
				}

				break;

			case "SEETEST":

				testParameters.setExecutionMode(ExecutionMode.valueOf(executionMode));
				testParameters.setMobileToolName(ToolName.valueOf("DEFAULT"));

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("MobileExecutionPlatform") == null) {
					testParameters.setMobileExecutionPlatform(
							MobileExecutionPlatform.valueOf(properties.getProperty("DefaultMobileExecutionPlatform")));
				} else {
					String mobileExecutionPlatform = method.getTestMethod().getXmlTest().getLocalParameters()
							.get("MobileExecutionPlatform");
					testParameters
							.setMobileExecutionPlatform((MobileExecutionPlatform.valueOf(mobileExecutionPlatform)));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName") == null) {
					testParameters.setDeviceName(properties.getProperty("DefaultDeviceName"));

				} else {
					testParameters
							.setDeviceName(method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName"));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("SeeTestPort") == null) {
					testParameters.setSeeTestPort(properties.getProperty("SeeTestDefaultPort"));

				} else {
					testParameters.setSeeTestPort(
							method.getTestMethod().getXmlTest().getLocalParameters().get("SeeTestPort"));
				}

				break;

			case "SAUCELABS":

				testParameters.setExecutionMode(ExecutionMode.valueOf(executionMode));

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("ToolName") == null) {
					testParameters.setMobileToolName(ToolName.valueOf(properties.getProperty("DefaultToolName")));
				} else {
					String mobileToolName = method.getTestMethod().getXmlTest().getLocalParameters().get("ToolName");
					testParameters.setMobileToolName((ToolName.valueOf(mobileToolName)));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("MobileExecutionPlatform") == null) {
					testParameters.setMobileExecutionPlatform(
							MobileExecutionPlatform.valueOf(properties.getProperty("DefaultMobileExecutionPlatform")));
				} else {
					String mobileExecutionPlatform = method.getTestMethod().getXmlTest().getLocalParameters()
							.get("MobileExecutionPlatform");
					testParameters
							.setMobileExecutionPlatform((MobileExecutionPlatform.valueOf(mobileExecutionPlatform)));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName") == null) {
					testParameters.setDeviceName(properties.getProperty("DefaultDeviceName"));

				} else {
					testParameters
							.setDeviceName(method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName"));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("MobileOsVersion") == null) {
					testParameters.setmobileOSVersion(properties.getProperty("DefautMobileOsVersion"));

				} else {
					testParameters.setmobileOSVersion(
							method.getTestMethod().getXmlTest().getLocalParameters().get("MobileOsVersion"));
				}

				// For Sauce Labs Browser based execution

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserName") == null) {
					testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));

				} else {
					testParameters.setBrowser(Browser
							.valueOf(method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserName")));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserVersion") == null) {
					testParameters.setBrowserVersion(properties.getProperty("DefaultBrowserVersion"));

				} else {
					testParameters.setBrowserVersion(
							method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserVersion"));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("Platform") == null) {
					testParameters.setPlatform(Platform.valueOf(properties.getProperty("DefaultPlatform")));

				} else {
					testParameters.setBrowserVersion(
							method.getTestMethod().getXmlTest().getLocalParameters().get("Platform"));
				}

				break;

			case "FASTEST":

				testParameters.setExecutionMode(ExecutionMode.valueOf(executionMode));

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("ToolName") == null) {
					testParameters.setMobileToolName(ToolName.valueOf(properties.getProperty("DefaultToolName")));
				} else {
					String mobileToolName = method.getTestMethod().getXmlTest().getLocalParameters().get("ToolName");
					testParameters.setMobileToolName((ToolName.valueOf(mobileToolName)));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("MobileExecutionPlatform") == null) {
					testParameters.setMobileExecutionPlatform(
							MobileExecutionPlatform.valueOf(properties.getProperty("DefaultMobileExecutionPlatform")));
				} else {
					String mobileExecutionPlatform = method.getTestMethod().getXmlTest().getLocalParameters()
							.get("MobileExecutionPlatform");
					testParameters
							.setMobileExecutionPlatform((MobileExecutionPlatform.valueOf(mobileExecutionPlatform)));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName") == null) {
					testParameters.setDeviceName(properties.getProperty("DefaultDeviceName"));

				} else {
					testParameters
							.setDeviceName(method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName"));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("MobileOsVersion") == null) {
					testParameters.setmobileOSVersion(properties.getProperty("DefautMobileOsVersion"));

				} else {
					testParameters.setmobileOSVersion(
							method.getTestMethod().getXmlTest().getLocalParameters().get("MobileOsVersion"));
				}

				// For FASTEST Browser based execution

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserName") == null) {
					testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));

				} else {
					testParameters.setBrowser(Browser
							.valueOf(method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserName")));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserVersion") == null) {
					testParameters.setBrowserVersion(properties.getProperty("DefaultBrowserVersion"));

				} else {
					testParameters.setBrowserVersion(
							method.getTestMethod().getXmlTest().getLocalParameters().get("BrowserVersion"));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("Platform") == null) {
					testParameters.setPlatform(Platform.valueOf(properties.getProperty("DefaultPlatform")));

				} else {
					testParameters.setBrowserVersion(
							method.getTestMethod().getXmlTest().getLocalParameters().get("Platform"));
				}

				break;

			case "TESTOBJECT":

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("ToolName") == null) {
					testParameters.setMobileToolName(ToolName.valueOf(properties.getProperty("DefaultToolName")));
				} else {
					String mobileToolName = method.getTestMethod().getXmlTest().getLocalParameters().get("ToolName");
					testParameters.setMobileToolName((ToolName.valueOf(mobileToolName)));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("MobileExecutionPlatform") == null) {
					testParameters.setMobileExecutionPlatform(
							MobileExecutionPlatform.valueOf(properties.getProperty("DefaultMobileExecutionPlatform")));
				} else {
					String mobileExecutionPlatform = method.getTestMethod().getXmlTest().getLocalParameters()
							.get("MobileExecutionPlatform");
					testParameters
							.setMobileExecutionPlatform((MobileExecutionPlatform.valueOf(mobileExecutionPlatform)));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName") == null) {
					testParameters.setDeviceName(properties.getProperty("DefaultDeviceName"));

				} else {
					testParameters
							.setDeviceName(method.getTestMethod().getXmlTest().getLocalParameters().get("DeviceName"));
				}

				if (method.getTestMethod().getXmlTest().getLocalParameters().get("MobileOsVersion") == null) {
					testParameters.setmobileOSVersion(properties.getProperty("DefautMobileOsVersion"));

				} else {
					testParameters.setmobileOSVersion(
							method.getTestMethod().getXmlTest().getLocalParameters().get("MobileOsVersion"));
				}

				break;

			default:
				testParameters.setExecutionMode(ExecutionMode.valueOf(properties.getProperty("DefaultExecutionMode")));
				if (method.getTestMethod().getXmlTest().getLocalParameters().get("BrowerName") == null) {
					testParameters.setBrowser(Browser
							.valueOf(method.getTestMethod().getXmlTest().getLocalParameters().get("BrowerName")));
				} else {
					testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));
				}
			}

		} catch (Exception ex) {
			log.error(
					"Error at settings TestParameters , please check the TestNG XML suite File and pass valid key & values"
							+ ex.getMessage());
		}
	}

	private void isAPIExecution(String executionMode, String toolName, SeleniumTestParameters testParameters) {

		testParameters.setAPIExecution(false);
		if (executionMode.equals(ExecutionMode.API.toString())) {
			testParameters.setAPIExecution(true);
		}
	}

	private void isMobileExecution(String executionMode, String toolName, SeleniumTestParameters testParameters) {

		testParameters.setMobileExecution(false);
		if (!(toolName == null)) {
			if (toolName.equals(ToolName.APPIUM.toString()) || executionMode.equals(ExecutionMode.SEETEST.toString())) {
				testParameters.setMobileExecution(true);
			}
		}
	}

}