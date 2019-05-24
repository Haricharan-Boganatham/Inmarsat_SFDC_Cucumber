package com.inmarsat.sfdc.framework;

import io.appium.java_client.AppiumDriver;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.experitest.selenium.MobileWebDriver;

/**
 * DriverFactory which will create respective driver Object
 * 
 * @author Inmarsat
 */
public class DriverFactory {

	static Logger log = Logger.getLogger(DriverFactory.class);
	private static Properties mobileProperties = Settings.getInstance();

	/**
	 * Function to return the object for AppiumDriver {@link AppiumDriver}
	 * object
	 * 
	 * @param testParameters
	 * 
	 * @return Instance of the {@link AppiumDriver} object
	 */
	@SuppressWarnings("rawtypes")
	public static AppiumDriver createAppiumInstance(SeleniumTestParameters testParameters) {

		AppiumDriver driver = null;
		try {
			switch (testParameters.getExecutionMode()) {

			case MOBILE:

				driver = AppiumDriverFactory.getAppiumDriver(testParameters);
				break;

			case SAUCELABS:

				driver = SauceLabsDriverFactory.getSauceAppiumDriver(testParameters);
				break;

			case TESTOBJECT:

				driver = SauceLabsDriverFactory.getTestObjectAppiumDriver(testParameters);
				break;

			case FASTEST:

				driver = FastestDriverFactory.getMintAppiumDriver(testParameters);
				break;

			default:
				throw new Exception("Unhandled Execution Mode!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
		return driver;
	}

	/**
	 * Function to return the object for WebDriver {@link WebDriver} object
	 * 
	 * @param testParameters
	 * 
	 * @return Instance of the {@link WebDriver} object
	 */
	public static WebDriver createWebDriverInstance(SeleniumTestParameters testParameters) {
		WebDriver driver = null;
		try {
			switch (testParameters.getExecutionMode()) {

			case LOCAL:
				driver = WebDriverFactory.getWebDriver(testParameters.getBrowser());
				break;

			case GRID:
				driver = WebDriverFactory.getRemoteWebDriver(testParameters.getBrowser(),
						testParameters.getBrowserVersion(), testParameters.getPlatform(),
						mobileProperties.getProperty("RemoteUrl"));
			case MOBILE:

				driver = AppiumDriverFactory.getRemoteWebDriver(testParameters);
				break;

			case SAUCELABS:
				driver = SauceLabsDriverFactory.getSauceRemoteWebDriver(mobileProperties.getProperty("SauceHost"),
						testParameters.getBrowser(), testParameters.getBrowserVersion(), testParameters.getPlatform(),
						testParameters);
				break;

			default:
				throw new Exception("Unhandled Execution Mode!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
		return driver;
	}

	/**
	 * Function to return the object for SeeTestDriver {@link MobileWebDriver}
	 * object
	 * 
	 * @param testParameters
	 * 
	 * @return Instance of the {@link MobileWebDriver} object
	 */
	public static MobileWebDriver createInstanceSeetestDriver(SeleniumTestParameters testParameters) {
		MobileWebDriver driver = null;
		try {
			switch (testParameters.getExecutionMode()) {

			case SEETEST:
				driver = SeeTestDriverFactory.getSeeTestDriver(mobileProperties.getProperty("SeeTestHost", "localhost"),
						Integer.parseInt(testParameters.getSeeTestPort()),
						mobileProperties.getProperty("SeeTestProjectBaseDirectory"),
						mobileProperties.getProperty("SeeTestReportType", "xml"), "report",
						"Test Name from Driver Init", testParameters.getMobileExecutionPlatform(),
						mobileProperties.getProperty("SeeTestAndroidApplicationName"),
						mobileProperties.getProperty("SeeTestiOSApplicationName"),
						mobileProperties.getProperty("SeeTestAndroidWebApplicationName"),
						mobileProperties.getProperty("SeeTestiOSWebApplicationName"), testParameters.getDeviceName());
				break;

			default:
				throw new Exception("Unhandled Execution Mode!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
		return driver;
	}

}