package com.example.selenium.selenium;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import lombok.val;

public class BasicTestClass {

	protected static WebDriver driver;
	protected static WebDriverWait wait;
	protected static WebElement username;
	protected static WebElement password;
	protected static WebElement loginButton;	
	
	protected static final By BTN_LOGOUT				= By.cssSelector(".v-menubar-menuitem");
	protected static final By TXT_USERNAME 				= By.id("txtUserName");
	protected static final By TXT_PASSWORD 				= By.id("txtPassword");
	protected static final By BTN_LOGIN 				= By.id("btnLogin");
	private static final By MAIN_SIDE_MENU 				= By.cssSelector(".v-slot-topMenuButton");
	private static final By SUB_MENU_BUTTONS 			= By.cssSelector(".v-slot-subMenuButton");	
	protected static Connection dbConnection;
	
	@Before
	public void setup(){
		System.setProperty("webdriver.chrome.driver", "C:\\dev\\selenium\\drivers\\chromedriver.exe");
		driverSetup();
		dbSetup();
	}
	
	private void dbSetup() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbdiary", "root", "root");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	private void driverSetup() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		driver	= new ChromeDriver(options);
        wait	= new WebDriverWait(driver, 10);
		driver.get("http://localhost:8080");
	}
	
	protected void login(final String usernameString, final String passwordString){
		wait.until(ExpectedConditions.presenceOfElementLocated(TXT_USERNAME));
		wait.until(ExpectedConditions.presenceOfElementLocated(TXT_PASSWORD));
		username = driver.findElement(TXT_USERNAME);
		password = driver.findElement(TXT_PASSWORD);
		loginButton	= driver.findElement(BTN_LOGIN);
		
		username.sendKeys(usernameString);
		password.sendKeys(passwordString);
		loginButton.click();
	}

	protected void logout() {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menubarForAccount")));
        val topMenu = driver.findElement(By.id("menubarForAccount"));
		topMenu.click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(BTN_LOGOUT));
		
		val topMenuElements = driver.findElements(BTN_LOGOUT);
		topMenuElements.get(1).click();		
	}
	
	public void menuSelector(final int topMenuIndex, final int submenuIndex){
		wait.until(ExpectedConditions.numberOfElementsToBe(MAIN_SIDE_MENU, 2));
		val mainButtons = driver.findElements(MAIN_SIDE_MENU);
		mainButtons.get(topMenuIndex).click();
		wait.until(ExpectedConditions.numberOfElementsToBe(SUB_MENU_BUTTONS, 3));
		val subButtons = driver.findElements(SUB_MENU_BUTTONS);
		subButtons.get(submenuIndex).click();
	}	
	
	protected ResultSet sqlExecutor(final String sqlCommand){
		ResultSet result = null;
		try {			
			Statement stmt = dbConnection.createStatement();
			result = stmt.executeQuery(sqlCommand);
			result.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	protected void forceSleep(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown(){
		driver.close();
	}
}
