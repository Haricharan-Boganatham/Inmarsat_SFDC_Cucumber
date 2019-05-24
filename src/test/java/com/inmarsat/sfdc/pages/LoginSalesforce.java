package com.inmarsat.sfdc.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginSalesforce  {
	
	WebDriver driver;
	
	@FindBy(xpath="//input[@id='username']")
	WebElement lbl_Username;
	
	@FindBy(xpath="//input[@id='password']")
	WebElement lbl_Password;
	
	@FindBy(xpath="//input[@id='Login']")
	WebElement btn_Login;
	
	
	public LoginSalesforce(WebDriver rdriver){
		driver = rdriver;
		PageFactory.initElements(rdriver, this);
	}
	
	
	public void entercred(String username, String password){
		
		try {
			Thread.sleep(800);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lbl_Username.sendKeys(username);
		lbl_Password.sendKeys(password);
		btn_Login.click();
		
	}
	
	public void getHomepage_Title(){		
		driver.getTitle();
	}

}
