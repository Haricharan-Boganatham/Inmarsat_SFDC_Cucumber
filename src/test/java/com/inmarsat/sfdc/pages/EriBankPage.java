package com.inmarsat.sfdc.pages;

import org.openqa.selenium.By;

public class EriBankPage {

	public static final By userName = By.id("com.experitest.ExperiBank:id/usernameTextField");
	public static final By password = By.id("com.experitest.ExperiBank:id/passwordTextField");
	public static final By login = By.id("com.experitest.ExperiBank:id/loginButton");
	public static final By logout = By.id("com.experitest.ExperiBank:id/logoutButton");
	
	public static final By makePayment = By.id("com.experitest.ExperiBank:id/makePaymentButton");
	public static final By phone = By.id("com.experitest.ExperiBank:id/phoneTextField");
	public static final By name = By.id("com.experitest.ExperiBank:id/nameTextField");
	public static final By amount = By.id("com.experitest.ExperiBank:id/amountTextField");
	public static final By selectCountry = By.id("com.experitest.ExperiBank:id/countryButton");
	public static final By country = By.id("com.experitest.ExperiBank:id/countryTextField");
	public static final By sendPayment = By.id("com.experitest.ExperiBank:id/sendPaymentButton");
	public static final By confirmPayment = By.id("android:id/button1");

}
