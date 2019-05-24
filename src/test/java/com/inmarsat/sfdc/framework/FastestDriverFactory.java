package com.inmarsat.sfdc.framework;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class FastestDriverFactory {

	private static Properties mobileProperties;

	private FastestDriverFactory() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the {@link AppiumDriver} object based on the
	 * parameters passed
	 * 
	 * @param executionPlatform
	 *            The {@link MobileExecutionPlatform} to be used for the test
	 *            execution
	 * @param deviceName
	 *            The device name to be used for the test execution
	 * @param host
	 *            The URL of the Mobile Min Cloud to be used for the test
	 *            execution
	 * @param mobileVersion
	 *            The Mobile device version to be used for the test execution
	 * @return The corresponding {@link AppiumDriver} object
	 */
	@SuppressWarnings("rawtypes")
	public static AppiumDriver getMintAppiumDriver(SeleniumTestParameters testParameters) {

		AppiumDriver driver = null;
		mobileProperties = Settings.getInstance();
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("username", mobileProperties.getProperty("MintUsername"));
		desiredCapabilities.setCapability("password", mobileProperties.getProperty("MintPassword"));
		desiredCapabilities.setCapability("serviceReqId", mobileProperties.getProperty("MintServiceRequestId"));
		try {
			switch (testParameters.getMobileExecutionPlatform()) {

			case ANDROID:

				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				// desiredCapabilities.setCapability("deviceID",testParameters.getDeviceName());
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());
				desiredCapabilities.setCapability("app", mobileProperties.getProperty("MintAndroidApplicationName"));

				try {
					driver = new AndroidDriver(new URL(mobileProperties.getProperty("mintHost")), desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;

			case IOS:

				desiredCapabilities.setCapability("platformName", "iOS");
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				// desiredCapabilities.setCapability("deviceID",testParameters.getDeviceName());
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());
				desiredCapabilities.setCapability("app", mobileProperties.getProperty("MintiOSApplicationName"));
				try {
					driver = new IOSDriver(new URL(mobileProperties.getProperty("mintHost")), desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;

			case WEB_ANDROID:

				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				// desiredCapabilities.setCapability("deviceID",testParameters.getDeviceName());
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());
				desiredCapabilities.setCapability("browserName", "Chrome");
				try {
					driver = new AndroidDriver(new URL(mobileProperties.getProperty("mintHost")), desiredCapabilities);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;

			case WEB_IOS:

				desiredCapabilities.setCapability("platformName", "iOS");
				desiredCapabilities.setCapability("deviceName", testParameters.getDeviceName());
				// desiredCapabilities.setCapability("deviceID",testParameters.getDeviceName());
				desiredCapabilities.setCapability("platformVersion", testParameters.getMobileOSVersion());

				desiredCapabilities.setCapability("browserName", "Safari");
				try {
					driver = new IOSDriver(new URL(mobileProperties.getProperty("mintHost")), desiredCapabilities);

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
	 * Function to return the {@link RemoteWebDriver} object based on the
	 * parameters passed
	 * 
	 * @param browser
	 *            The {@link Browser} to be used for the test execution
	 * @param browserVersion
	 *            The browser version to be used for the test execution
	 * @param platform
	 *            The {@link Platform} to be used for the test execution
	 * @param remoteUrl
	 *            The URL of the remote machine to be used for the test
	 *            execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getRemoteWebDriver(Browser browser, String browserVersion, Platform platform,
			String remoteUrl, String testcaseName) {

		mobileProperties = Settings.getInstance();

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("username", mobileProperties.getProperty("FastestUserame"));
		desiredCapabilities.setCapability("password", mobileProperties.getProperty("FastestPassword"));
		desiredCapabilities.setCapability("servicerequestid", mobileProperties.getProperty("FastestServiceRequestId"));
		desiredCapabilities.setCapability("packagename", testcaseName);
		desiredCapabilities.setBrowserName(browser.getValue());

		if (browserVersion != null) {
			desiredCapabilities.setVersion(browserVersion);
		}
		if (platform != null) {
			desiredCapabilities.setPlatform(platform);
		}

		desiredCapabilities.setJavascriptEnabled(true); // Pre-requisite for
														// remote execution

		URL url = getUrl(remoteUrl);

		return new RemoteWebDriver(url, desiredCapabilities);
	}

	private static URL getUrl(String remoteUrl) {
		URL url = null;
		try {
			url = new URL(remoteUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

}