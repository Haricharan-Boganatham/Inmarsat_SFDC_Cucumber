package com.inmarsat.sfdc.stepdefinitions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.inmarsat.sfdc.framework.APIReusuableLibrary;
import com.inmarsat.sfdc.framework.DriverFactory;
import com.inmarsat.sfdc.framework.HeadersForAPI;
import com.inmarsat.sfdc.framework.SeleniumTestParameters;

import cucumber.api.Scenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;

public abstract class MasterStepDefs {

	static Logger log = Logger.getLogger(DriverFactory.class);

	protected static Scenario currentScenario;
	protected static SeleniumTestParameters currentTestParameters;
	protected static Properties properties;
	protected APIReusuableLibrary apiDriver = new APIReusuableLibrary();
	protected HeadersForAPI headers = new HeadersForAPI();

	Map<String, Object> perfectoCommand = new HashMap<>();
	private static HttpURLConnection httpURLConnect;
	int responseStatus;
	int responseCode;

	/**
	 * All reusuable Appium Functions with Perfecto
	 */

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param context
	 *            - Context of App like NATIVE_APP or WEB
	 * @param appName
	 *            - Name of the App as displayed in Mobile
	 */
	@SuppressWarnings("rawtypes")
	protected void openApp(final String context, final String appName, AppiumDriver driver) {
		if (context.equals("NATIVE_APP")) {
			final Map<String, Object> perfectoCommand = new HashMap<>();
			perfectoCommand.put("name", appName);
			driver.executeScript("mobile:application:open", perfectoCommand);
		}
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param context
	 *            - Context of App like NATIVE_APP or WEB
	 * @param appName
	 *            - Identifier of the App.
	 */
	@SuppressWarnings("rawtypes")
	protected void openAppWithIdentifier(final String context, final String identifer, AppiumDriver driver) {
		if (context.equals("NATIVE_APP")) {
			perfectoCommand.put("identifier", identifer);
			driver.executeScript("mobile:application:open", perfectoCommand);
			perfectoCommand.clear();
		}
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param type
	 *            - Type of report like pdf
	 */
	protected byte[] downloadReport(final String type, RemoteWebDriver driver) throws IOException {
		final String command = "mobile:report:download";
		final Map<String, String> params = new HashMap<>();
		params.put("type", type);
		final String report = (String) (driver).executeScript(command, params);
		final byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report);
		return reportBytes;
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 */
	@SuppressWarnings("rawtypes")
	protected byte[] downloadWTReport(AppiumDriver driver) {
		final String reportUrl = (String) driver.getCapabilities().getCapability("windTunnelReportUrl");
		String returnString = "<html><head><META http-equiv=\"refresh\" content=\"0;URL=";
		returnString = returnString + reportUrl + "\"></head><body /></html>";

		return returnString.getBytes();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param context
	 *            - Context of App like NATIVE_APP or WEB
	 * @param appName
	 *            - Name of the App.
	 */
	@SuppressWarnings("rawtypes")
	protected void closeApp(final String context, final String appName, AppiumDriver driver) {
		if (context.equals("NATIVE_APP")) {
			perfectoCommand.put("name", appName);
			try {
				driver.executeScript("mobile:application:close", perfectoCommand);
			} catch (final WebDriverException e) {
			}
		}
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param context
	 *            - Context of App like NATIVE_APP or WEB
	 * @param appName
	 *            - Identifier of the App.
	 */
	@SuppressWarnings("rawtypes")
	protected void closeAppWithIdentifier(final String context, final String bundleId, AppiumDriver driver) {
		if (context.equals("NATIVE_APP")) {
			perfectoCommand.put("identifier", bundleId);
			try {
				driver.executeScript("mobile:application:close", perfectoCommand);
			} catch (final WebDriverException e) {
			}
		}
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param textToFind
	 *            - text that has to be searched
	 * @param timeout
	 */
	@SuppressWarnings("rawtypes")
	protected Boolean textCheckpoint(final String textToFind, final Integer timeout, AppiumDriver driver) {
		perfectoCommand.put("content", textToFind);
		perfectoCommand.put("timeout", timeout);
		final Object result = driver.executeScript("mobile:checkpoint:text", perfectoCommand);
		final Boolean resultBool = Boolean.valueOf(result.toString());
		perfectoCommand.clear();
		return resultBool;
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param textToFind
	 *            - text that has to be searched
	 * @param timeout
	 */
	@SuppressWarnings("rawtypes")
	protected void textClick(final String textToFind, final Integer timeout, AppiumDriver driver) {
		perfectoCommand.put("content", textToFind);
		perfectoCommand.put("timeout", timeout);
		driver.executeScript("mobile:text:select", perfectoCommand);
		perfectoCommand.clear();

	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param label
	 *            - text that has to be searched
	 * @param threshold
	 */
	@SuppressWarnings("rawtypes")
	protected void visualScrollToClick(final String label, final Integer threshold, AppiumDriver driver) {
		perfectoCommand.put("label", label);
		perfectoCommand.put("threshold", threshold);
		perfectoCommand.put("scrolling", "scroll");
		driver.executeScript("mobile:button-text:click", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param label
	 *            - text that has to be searched
	 * @param timeout
	 * @param threshold
	 */
	@SuppressWarnings("rawtypes")
	protected void visualClick(final String label, final Integer timeout, final Integer threshold,
			AppiumDriver driver) {
		perfectoCommand.put("label", label);
		perfectoCommand.put("threshold", threshold);
		perfectoCommand.put("timeout", timeout);
		driver.executeScript("mobile:button-text:click", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param label
	 *            - text that has to be searched
	 * @param timeout
	 * @param threshold
	 * @param labelDirection
	 * @param labelOffset
	 */
	@SuppressWarnings("rawtypes")
	protected void visualClick(final String label, final Integer timeout, final Integer threshold,
			final String labelDirection, final String labelOffset, AppiumDriver driver) {
		perfectoCommand.put("label", label);
		perfectoCommand.put("threshold", threshold);
		perfectoCommand.put("timeout", timeout);
		perfectoCommand.put("label.direction", labelDirection);
		perfectoCommand.put("label.offset", labelOffset);
		driver.executeScript("mobile:button-text:click", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param imagePath
	 */
	@SuppressWarnings("rawtypes")
	protected void imageClick(String imagePath, AppiumDriver driver) {
		perfectoCommand.put("content", imagePath);
		perfectoCommand.put("timeout", "5");
		perfectoCommand.put("screen.top", "0%");
		perfectoCommand.put("screen.height", "100%");
		perfectoCommand.put("screen.left", "0%");
		perfectoCommand.put("screen.width", "100%");
		driver.executeScript("mobile:image:select", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param imagePath
	 */
	@SuppressWarnings("rawtypes")
	protected Boolean imageCheckpoint(String imagePath, AppiumDriver driver) {
		perfectoCommand.put("content", imagePath);
		perfectoCommand.put("threshold", "90");
		perfectoCommand.put("screen.top", "0%");
		perfectoCommand.put("screen.height", "100%");
		perfectoCommand.put("screen.left", "0%");
		perfectoCommand.put("screen.width", "100%");
		Object result = driver.executeScript("mobile:image:find", perfectoCommand);
		final Boolean resultBool = Boolean.valueOf(result.toString());
		perfectoCommand.clear();
		return resultBool;
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param repositoryFile
	 * @param handsetFile
	 */
	@SuppressWarnings("rawtypes")
	protected void putFileOnDevice(final String repositoryFile, final String handsetFile, AppiumDriver driver) {
		perfectoCommand.put("repositoryFile", repositoryFile);
		perfectoCommand.put("handsetFile", handsetFile);
		driver.executeScript("mobile:media:put", perfectoCommand);
		perfectoCommand.clear();

	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param handsetFile
	 * @param repositoryFile
	 */
	@SuppressWarnings("rawtypes")
	protected void getFileOnDevice(final String handsetFile, final String repositoryFile, AppiumDriver driver) {
		perfectoCommand.put("repositoryFile", repositoryFile);
		perfectoCommand.put("handsetFile", handsetFile);
		driver.executeScript("mobile:media:get", perfectoCommand);
		perfectoCommand.clear();

	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param handsetFile
	 */
	@SuppressWarnings("rawtypes")
	protected void deleteFromDevice(final String handsetFile, AppiumDriver driver) {
		perfectoCommand.put("handsetFile", handsetFile);
		driver.executeScript("mobile:media:delete", perfectoCommand);
		perfectoCommand.clear();

	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param repositoryFile
	 */
	@SuppressWarnings("rawtypes")
	protected void deleteFromRepository(final String repositoryFile, AppiumDriver driver) {
		perfectoCommand.put("repositoryFile", repositoryFile);
		driver.executeScript("mobile:media:delete", perfectoCommand);
		perfectoCommand.clear();

	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param keyPress
	 */
	@SuppressWarnings("rawtypes")
	protected void deviceKeyPress(final String keyPress, AppiumDriver driver) {

		perfectoCommand.put("keySequence", keyPress);
		driver.executeScript("mobile:presskey", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	@SuppressWarnings("rawtypes")
	protected void swipe(final String x1, final String y1, final String x2, final String y2, AppiumDriver driver) {
		final List<String> swipeCoordinates = new ArrayList<>();
		swipeCoordinates.add(x1 + ',' + y1);
		swipeCoordinates.add(x2 + ',' + y2);
		perfectoCommand.put("location", swipeCoordinates);
		driver.executeScript("mobile:touch:drag", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param textToFind
	 */
	@SuppressWarnings("rawtypes")
	protected void swipeTillText(String textToFind, AppiumDriver driver) {
		perfectoCommand.put("content", textToFind);
		perfectoCommand.put("scrolling", "scroll");
		perfectoCommand.put("maxscroll", "10");
		perfectoCommand.put("next", "SWIPE_UP");
		driver.executeScript("mobile:text:select", perfectoCommand);
		perfectoCommand.clear();
	}

	/**
	 * Function Applicable to Pause the Script, Generic Application
	 * 
	 * @param How_Long_To_Pause
	 */
	protected void PauseScript(int How_Long_To_Pause) {
		// convert to seconds
		How_Long_To_Pause = How_Long_To_Pause * 1000;

		try {
			Thread.sleep(How_Long_To_Pause);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * All reusuable Selenium Functions with Perfecto
	 */

	/**
	 * Function to switch the Context
	 * 
	 * @param driver
	 * @RemoteWebDriver
	 * @param context
	 */
	protected static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param driver
	 * @param list
	 */
	@SuppressWarnings("rawtypes")
	protected void scrollChecker(IOSDriver driver, String[] list) {
		for (int i = 0; i < list.length; i++) {

			MobileElement me = (MobileElement) driver.findElement(By.xpath("//UIAPickerWheel[" + (i + 1) + "]"));
			int mget = getMonthInt(me.getText().split(",")[0]);

			if (i == 0) {
				if (mget > getMonthInt(list[i])) {
					scrollAndSearch(driver, list[i], me, true);
				} else {
					scrollAndSearch(driver, list[i], me, false);
				}
			} else {
				if (Integer.parseInt(me.getText().split(",")[0]) > Integer.parseInt(list[i])) {
					scrollAndSearch(driver, list[i], me, true);
				} else {
					scrollAndSearch(driver, list[i], me, false);
				}
			}
		}
	}

	// Used to get the integer for a month based on the string of the month
	private int getMonthInt(String month) {
		int monthInt = 0;
		switch (month) {
		case "Jan":
			monthInt = 1;
			break;
		case "January":
			monthInt = 1;
			break;
		case "February":
			monthInt = 2;
			break;
		case "Feb":
			monthInt = 2;
			break;
		case "March":
			monthInt = 3;
			break;
		case "Mar":
			monthInt = 3;
			break;
		case "April":
			monthInt = 4;
			break;
		case "Apr":
			monthInt = 4;
			break;
		case "May":
			monthInt = 5;
			break;
		case "June":
			monthInt = 6;
			break;
		case "Jun":
			monthInt = 6;
			break;
		case "July":
			monthInt = 7;
			break;
		case "Jul":
			monthInt = 7;
			break;
		case "August":
			monthInt = 8;
			break;
		case "Aug":
			monthInt = 8;
			break;
		case "September":
			monthInt = 9;
			break;
		case "Sep":
			monthInt = 9;
			break;
		case "October":
			monthInt = 10;
			break;
		case "Oct":
			monthInt = 10;
			break;
		case "November":
			monthInt = 11;
			break;
		case "Nov":
			monthInt = 11;
			break;
		case "December":
			monthInt = 12;
			break;
		case "Dec":
			monthInt = 12;
			break;
		}
		return monthInt;
	}

	// Code here shouldn't be modified
	@SuppressWarnings("rawtypes")
	private void scrollAndSearch(IOSDriver driver, String value, MobileElement me, Boolean direction) {
		String x = getLocationX(me);
		String y = getLocationY(me);
		while (!driver.findElementByXPath(getXpathFromElement(me)).getText().contains(value)) {
			swipe(driver, x, y, direction);
		}
	}

	// Performs the swipe and search operation
	// Code here shouldn't be modified
	@SuppressWarnings("rawtypes")
	private void swipe(IOSDriver driver, String start, String end, Boolean up) {
		String direction;
		if (up) {
			direction = start + "," + (Integer.parseInt(end) + 70);
		} else {
			direction = start + "," + (Integer.parseInt(end) - 70);
		}

		Map<String, Object> params1 = new HashMap<>();
		params1.put("location", start + "," + end);
		params1.put("operation", "down");
		driver.executeScript("mobile:touch:tap", params1);

		Map<String, Object> params2 = new HashMap<>();
		List<String> coordinates2 = new ArrayList<>();

		coordinates2.add(direction);
		params2.put("location", coordinates2);
		params2.put("auxiliary", "notap");
		params2.put("duration", "3");
		driver.executeScript("mobile:touch:drag", params2);

		Map<String, Object> params3 = new HashMap<>();
		params3.put("location", direction);
		params3.put("operation", "up");
		driver.executeScript("mobile:touch:tap", params3);
	}

	// Gets the objects X location in pixels
	private String getLocationX(MobileElement me) {
		int x = me.getLocation().x;
		int width = (Integer.parseInt(me.getAttribute("width")) / 2) + x;
		return width + "";
	}

	// Gets the objects X location in pixels
	private String getLocationY(MobileElement me) {
		int y = me.getLocation().y;
		int height = (Integer.parseInt(me.getAttribute("height")) / 2) + y;
		return height + "";
	}

	// Parses webelement to retrieve the xpath used for identification
	private String getXpathFromElement(MobileElement me) {
		return (me.toString().split("-> xpath: ")[1]).substring(0, (me.toString().split("-> xpath: ")[1]).length() - 1);
	}

	/**
	 * Function Applicable only when the ExecutionMode used is <b>PERFECTO
	 * 
	 * @param letter
	 */
	@SuppressWarnings("rawtypes")
	protected void drawLetter(final String letter, AppiumDriver driver) {
		final List<String> coordinates = new ArrayList<>();

		switch (letter) {
		case "A":

			break;
		case "B":

			break;
		case "C":

			break;
		case "D":

			break;
		case "E":
			coordinates.add("42%,40%");
			coordinates.add("42%,60%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("42%,40%");
			coordinates.add("52%,40%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("42%,48%");
			coordinates.add("52%,48%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("42%,56%");
			coordinates.add("52%,56%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			break;
		case "F":

			break;
		case "G":

			break;
		case "H":

			break;
		case "I":

			break;
		case "J":

			break;
		case "K":

			break;
		case "L":

			break;
		case "M":

			break;
		case "N":

			break;
		case "O":

			break;
		case "P":
			coordinates.add("30%,40%");
			coordinates.add("30%,60%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("30%,40%");
			coordinates.add("40%,40%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("38%,40%");
			coordinates.add("38%,52%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("38%,48%");
			coordinates.add("28%,48%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			break;
		case "Q":

			break;
		case "R":
			coordinates.add("54%,40%");
			coordinates.add("54%,60%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("54%,40%");
			coordinates.add("64%,40%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("62%,40%");
			coordinates.add("62%,52%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("62%,48%");
			coordinates.add("52%,48%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			coordinates.add("54%,48%");
			coordinates.add("64%,60%");
			perfectoCommand.put("location", coordinates);
			driver.executeScript("mobile:touch:drag", perfectoCommand);
			perfectoCommand.clear();
			coordinates.clear();
			break;
		case "S":

			break;
		case "T":

			break;
		case "U":

			break;
		case "V":

			break;
		case "W":

			break;
		case "X":

			break;
		case "Y":

			break;
		case "Z":

			break;
		}
	}

	/**
	 * Function to check the broken links
	 * 
	 * @param Url
	 */
	protected void brokenLinkValidator(String Url) {
		urlLinkStatus(validationOfLinks(Url));
	}

	private String[] validationOfLinks(String urlToValidate) {
		String[] responseArray = new String[3];
		try {
			URL url = new URL(urlToValidate);
			httpURLConnect = (HttpURLConnection) url.openConnection();
			httpURLConnect.setConnectTimeout(3000);
			httpURLConnect.connect();
			responseStatus = httpURLConnect.getResponseCode();
			responseCode = responseStatus / 100;
		} catch (Exception e) {
		}
		responseArray[0] = urlToValidate;
		responseArray[1] = String.valueOf(responseCode);
		responseArray[2] = String.valueOf(responseStatus);
		return responseArray;
	}

	private void urlLinkStatus(String[] responseArray) {
		try {
			String linkValue = responseArray[0];
			String responseValue = responseArray[1];
			responseCode = Integer.valueOf(responseValue);
			String responseStatus = responseArray[2];
			switch (responseCode) {
			case 2:
				/*
				 * System.out.println("It's a Non- Broken Link with URL " +
				 * linkValue + " - " + responseCode + " - with Response code " +
				 * HttpURLConnection.HTTP_OK + " OK ");
				 */
				currentScenario.write(linkValue + ": Response code : " + responseStatus + " - OK" + " & STATUS:PASS");
				break;
			case 3:
				currentScenario.write(linkValue + ": Unknown Responce Code" + " & STATUS:FAIL");
				break;
			case 4:
				/*
				 * System.out.println("It's a Broken link with URL " + linkValue
				 * + " - " + responseCode + " - Client Error " +
				 * HttpURLConnection.HTTP_CLIENT_TIMEOUT + " Bad Request ");
				 */
				currentScenario
						.write(linkValue + ": Response code : " + responseStatus + " - Client error & STATUS:FAIL");
				break;

			case 5:
				/*
				 * System.out.println("It's a Broken link with URL " + linkValue
				 * + " - " + responseCode + " - Internal Server Error " +
				 * HttpURLConnection.HTTP_SERVER_ERROR);
				 */
				currentScenario.write(
						linkValue + ": Response code : " + responseStatus + " - Internal Server Error & STATUS:FAIL");
				break;
			default:
				currentScenario.write(linkValue + ": Unknown Responce Code & STATUS:FAIL");
				break;
			}

		} catch (Exception e) {

		} finally {
			httpURLConnect.disconnect();

		}
	}

}