package com.example.selenium.selenium.logintest;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.example.selenium.selenium.BasicTestClass;

public class LoginTest extends BasicTestClass{

	private static final By NOTIFICATION_ERROR	 	= By.cssSelector(".v-Notification.error");

	@Test
	public void loginTest(){
		login("admin", "");		
        wait.until(ExpectedConditions.elementToBeClickable(NOTIFICATION_ERROR));
		
        driver.findElement(NOTIFICATION_ERROR).click();
        username.clear();
        password.clear();
        
        wait.until(ExpectedConditions.textToBe(TXT_USERNAME, StringUtils.EMPTY));
        wait.until(ExpectedConditions.textToBe(TXT_PASSWORD, StringUtils.EMPTY));
        
        login("admin", "admin");
        logout();
	}
}
