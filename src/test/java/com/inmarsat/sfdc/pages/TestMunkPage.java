package com.inmarsat.sfdc.pages;

import org.openqa.selenium.By;

public class TestMunkPage {

	public static By txtUsername = By.xpath(
			"//XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeTextField[1]");

	public static By txtPassword = By.xpath(
			"//XCUIElementTypeApplication[1]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeSecureTextField[1]");

	public static By btnSignIn = By.name("SIGN IN");

	public static By btnSkip = By.name("Skip");

	public static By btnSecond = By.name("Second");

	public static By btnAlert = By.name("SHOW ALERT VIEW");

	public static By btnDismiss = By.name("Dismiss");

	public static By btnTable = By.name("Plain Table");

	public static By btnSection = By.name("Section 1 Row 1");

	public static By btnBack = By.name("Back");

	public static By btnHome = By.name("Home");

	public static By lblHome = By.name("Hello world");

}
