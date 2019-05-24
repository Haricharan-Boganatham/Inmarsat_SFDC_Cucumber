package com.inmarsat.sfdc.framework;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SauceLabsDriverFactory {

	private static Properties mobileProperties;

	private SauceLabsDriverFactory() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the Saucelabs DesktopCloud {@link RemoteWebDriver}
	 * object based on the parameters passed
	 * 
	 * @param sauceUrl
	 *            The Saucelabs URL to be used for the test execution
	 * @param browser
	 *            Browser {@link Browser}
	 * @param platformName
	 *            The platform to be used for the test execution (Windows, Mac,
	 *            etc.)
	 * @param version
	 *            The browser version to be used for the test execution
	 * @param browserName
	 *            The {@link Browser} to be used for the test execution
	 * 
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getSauceRemoteWebDriver(String sauceURL, Browser browser, String browserVersion,
			Platform platformName, SeleniumTestParameters testParameters) {
		WebDriver driver = null;
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("platform", platformName);
		desiredCapabilities.setCapability("version", browserVersion);
		desiredCapabilities.setCapability("browserName", browser);
		// desiredCapabilities.setCapability("screen-resolution","800x600");
		desiredCapabilities.setCapability("name", testParameters.getScenarioName());
		try {
			driver = new RemoteWebDriver(new URL(sauceURL), desiredCapabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return driver;
	}

	/**
	 * Function to return the object for AppiumDriver {@link AppiumDriver}
	 * object
	 * 
	 * @param testParameters
	 *            Test parameters{@link SeleniumTestParameters}
	 * 
	 * @return Instance of the {@link AppiumDriver} object
	 */
	@SuppressWarnings("rawtypes")
	public static AppiumDriver getSauceAppiumDriver(SeleniumTestParameters testParameters) {

		AppiumDriver driver = null;

		mobileProperties = Settings.getInstance();

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		try {
			switch (testParameters.getMobileExecutionPlatform()) {

			case ANDROID:
				desiredCapabilities.setCapability("appiumVersion",
						mobileProperties.getProperty("SaucelabAppiumDriverVersion"));
				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				desiredCapabilities.setCapability("app", mobileProperties.getProperty("SauceAndroidIdentifier"));
				desiredCapabilities.setCapability("name", testParameters.getScenarioName());
				try {
					driver = new AndroidDriver(new URL(mobileProperties.getProperty("SauceHost")), desiredCapabilities);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

				break;

			case IOS:

				desiredCapabilities.setCapability("appiumVersion",
						mobileProperties.getProperty("SaucelabAppiumDriverVersion"));
				desiredCapabilities.setCapability("platformName", "ios");
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				desiredCapabilities.setCapability("name", testParameters.getScenarioName());
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());
				desiredCapabilities.setCapability("app", mobileProperties.getProperty("SauceIosBundleID"));

				try {
					driver = new IOSDriver(new URL(mobileProperties.getProperty("SauceHost")), desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;

			case WEB_ANDROID:
				desiredCapabilities.setCapability("appiumVersion",
						mobileProperties.getProperty("SaucelabAppiumDriverVersion"));
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				desiredCapabilities.setCapability("deviceOrientation", "portrait");
				desiredCapabilities.setCapability("browserName", "chrome");
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());
				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("name", testParameters.getScenarioName());

				try {
					driver = new AndroidDriver(new URL(mobileProperties.getProperty("SauceHost")), desiredCapabilities);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;

			case WEB_IOS:
				desiredCapabilities.setCapability("appiumVersion",
						mobileProperties.getProperty("SaucelabAppiumDriverVersion"));
				desiredCapabilities.setCapability("platformName", "ios");
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				desiredCapabilities.setCapability("name", testParameters.getScenarioName());
				desiredCapabilities.setCapability("browserName", "Safari");
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());

				try {
					driver = new IOSDriver(new URL(mobileProperties.getProperty("SauceHost")), desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;

			default:

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return driver;

	}

	/**
	 * Function to return the object for AppiumDriver {@link AppiumDriver}
	 * object
	 * 
	 * @param testParameters
	 *            Test parameters{@link SeleniumTestParameters}
	 * 
	 * @return Instance of the {@link AppiumDriver} object
	 */
	@SuppressWarnings("rawtypes")
	public static AppiumDriver getTestObjectAppiumDriver(SeleniumTestParameters testParameters) {

		AppiumDriver driver = null;

		mobileProperties = Settings.getInstance();

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		try {
			switch (testParameters.getMobileExecutionPlatform()) {

			case ANDROID:
				desiredCapabilities.setCapability("testobject_api_key",
						mobileProperties.getProperty("AccessKeyAndroid"));
				desiredCapabilities.setCapability("appiumVersion",
						mobileProperties.getProperty("TestObjectAppiumVersion"));
				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				desiredCapabilities.setCapability("testobject_suite_name", testParameters.getScenarioName());
				desiredCapabilities.setCapability("testobject_test_name", testParameters.getScenarioName());
				desiredCapabilities.setCapability("noReset", "true");
				desiredCapabilities.setCapability("testobject_app_id",
						mobileProperties.getProperty("TestObjectAndroidAppKey"));

				try {
					driver = new AndroidDriver(new URL(mobileProperties.getProperty("TestObjectHost")), desiredCapabilities);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

				break;

			case IOS:

				desiredCapabilities.setCapability("testobject_api_key", mobileProperties.getProperty("AccessKeyiOS"));
				desiredCapabilities.setCapability("appiumVersion",
						mobileProperties.getProperty("TestObjectAppiumVersion"));
				desiredCapabilities.setCapability("platformName", "iOS");
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());
				desiredCapabilities.setCapability("testobject_suite_name", testParameters.getScenarioName());
				desiredCapabilities.setCapability("testobject_test_name", testParameters.getScenarioName());
				desiredCapabilities.setCapability("noReset", "true");
				desiredCapabilities.setCapability("testobject_app_id",
						mobileProperties.getProperty("TestObjectiOSAppKey"));

				try {
					driver = new IOSDriver(new URL(mobileProperties.getProperty("TestObjectHost")), desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;

			case WEB_ANDROID:
				desiredCapabilities.setCapability("appiumVersion",
						mobileProperties.getProperty("TestObjectAppiumVersion"));
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				desiredCapabilities.setCapability("deviceOrientation", "portrait");
				desiredCapabilities.setCapability("browserName", "Chrome");
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());
				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("testobject_suite_name", testParameters.getScenarioName());
				desiredCapabilities.setCapability("testobject_test_name", testParameters.getScenarioName());

				try {
					driver = new AndroidDriver(new URL(mobileProperties.getProperty("TestObjectHost")), desiredCapabilities);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;

			case WEB_IOS:
				desiredCapabilities.setCapability("appiumVersion",
						mobileProperties.getProperty("TestObjectAppiumVersion"));
				desiredCapabilities.setCapability("platformName", "iOS");
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				desiredCapabilities.setCapability("browserName", "Safari");
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());
				desiredCapabilities.setCapability("testobject_suite_name", testParameters.getScenarioName());
				desiredCapabilities.setCapability("testobject_test_name", testParameters.getScenarioName());

				try {
					driver = new IOSDriver(new URL(mobileProperties.getProperty("TestObjectHost")), desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;

			default:

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return driver;

	}

}
