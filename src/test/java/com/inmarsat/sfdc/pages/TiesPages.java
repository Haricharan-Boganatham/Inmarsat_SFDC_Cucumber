package com.inmarsat.sfdc.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * UI Map for SignOnPage
 */
public class TiesPages {

	public static final By research = By.cssSelector("#rdbResearch");
	public static final By continues = By.cssSelector("#wscontinue");

	// collections

	public static final By collections = By.xpath("//*[@id='nav']/ol/li[1]/a");
	public static final By arrivals = By.cssSelector("#matter > div.layer-fore > div > div > ol > li:nth-child(1) > a");
	public static final By select_tie = By.xpath("//*[@id=\"matter\"]/div[2]/div/ol/li[1]/a");
	public static final By quantity = By.id("qty-increase");
	public static final By add_to_cart = By.cssSelector("#product_addtocart_form > div.actions-block > div > button");
	public static final By checkout = By.xpath("//*[@id='cart-checkout-methods']");

	// contacts
	public static final By contact = By.cssSelector("#footer > section.footer-sub > div > section.contact-box > a");
	public static final By txtname = By.cssSelector("#name");
	public static final By txtemail = By.cssSelector("#email");
	public static final By txtphone = By.xpath("//*[@id=\"telephone\"]");
	public static final By txtmessage = By.xpath("//*[@id=\"comment\"]");
	public static final By home = By.cssSelector("#logo > a");

	private WebDriver driver;

	public TiesPages(WebDriver driver) {
		this.driver = driver;
	}

	public void provideDetails(String name, String email, String phone, String message) throws InterruptedException {

		driver.findElement(contact).click();
		driver.findElement(txtname).sendKeys(name);
		driver.findElement(txtemail).sendKeys(email);
		driver.findElement(txtphone).sendKeys(phone);
		driver.findElement(txtmessage).sendKeys(message);
		driver.findElement(TiesPages.home).click();
	}

}