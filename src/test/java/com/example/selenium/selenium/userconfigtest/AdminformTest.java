package com.example.selenium.selenium.userconfigtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.example.selenium.selenium.BasicTestClass;

import lombok.val;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminformTest extends BasicTestClass{	
	
	private static final By GRID 									= By.id("grid");
	private static final By GRID_ROWS								= By.cssSelector(".v-grid-body tr");	
	private static final By TXT_USERNAME_FOR_CREATION 				= By.id("txtUserNameForCreation");
	private static final By TXT_DISPLAYNAME_FOR_CREATION 			= By.id("txtDisplayNameForCreation");
	private static final By TXT_PASSWORD_FOR_CREATION 				= By.id("txtPassword");
	private static final By TXT_EMAIL_FOR_CREATION 					= By.id("txtEmailForCreation");
	private static final By CMB_ROLE 								= By.cssSelector(".v-filterselect-input");
	private static final By BTN_SAVE_FOR_CREATION 					= By.id("btnSaveForCreation");
	private static final By TAB_SHEETS								= By.cssSelector(".v-tabsheet-tabs .v-tabsheet-tabitemcell");
	private static final By TXT_USERNAME_FOR_MODIFICATION			= By.id("txtUserNameForEditing");
	private static final By TXT_DISPLAYNAME_FOR_MODIFICATION 		= By.id("txtDisplayNameForEditing");
	private static final By TXT_EMAIL_FOR_MODIFICATION 				= By.id("txtEmailForEditing");
	private static final By BTN_SAVE_FOR_FOR_MODIFICATION			= By.id("btnSaveForEditing");
	private static final By TXT_USERNAME_FOR_DELETING				= By.id("txtUserSearchForDeleting");
	private static final By BTN_DELETE								= By.id("btnDelete");
	
	private static final String SQL_NUMBER_OF_ELEMENTS				= "SELECT COUNT(*) FROM ACCOUNTS WHERE USERNAME LIKE 'testUsername'";
	private static final String SQL_MODIFIED_ITEM					= "SELECT DISPLAYNAME,EMAIL FROM ACCOUNTS WHERE USERNAME LIKE 'testUsername'";
	
	
	@Test
	public void t1_userCreationTest() throws SQLException{
		login("admin","admin");
		menuSelector(1,2);
		wait.until(ExpectedConditions.presenceOfElementLocated(GRID));
		
		val numberOfElementsBeforeSaving = sqlExecutor(SQL_NUMBER_OF_ELEMENTS);
		val beforeSaving = numberOfElementsBeforeSaving.getInt(1);
		
		driver.findElement(TXT_USERNAME_FOR_CREATION).sendKeys("testUsername");
		driver.findElement(TXT_DISPLAYNAME_FOR_CREATION).sendKeys("testDisplayName");
		driver.findElement(TXT_EMAIL_FOR_CREATION).sendKeys("testemail@mail.hu");
		driver.findElement(TXT_PASSWORD_FOR_CREATION).sendKeys("test");
		driver.findElement(CMB_ROLE).sendKeys("ADMIN");
		driver.findElement(CMB_ROLE).sendKeys(Keys.ENTER);
		
		wait.until(ExpectedConditions.elementToBeClickable(BTN_SAVE_FOR_CREATION));
		driver.findElement(BTN_SAVE_FOR_CREATION).click();
		
		forceSleep();		
		val numberOfElementsAfterSaving = sqlExecutor(SQL_NUMBER_OF_ELEMENTS);
		val afterSaving = numberOfElementsAfterSaving.getInt(1);
		assertTrue(beforeSaving+1 == afterSaving );
		logout();
		login("testUsername","test");
		logout();		
	}
	
	@Test
	public void t2_userModificationTest() throws SQLException{
		login("admin","admin");
		menuSelector(1,2);
		wait.until(ExpectedConditions.presenceOfElementLocated(GRID));
		val rows = driver.findElements(GRID_ROWS);
		rows.get(rows.size()-1).click();
		
		driver.findElements(TAB_SHEETS).get(1).click();
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(TXT_USERNAME_FOR_MODIFICATION));
		
		driver.findElement(TXT_DISPLAYNAME_FOR_MODIFICATION).clear();
		driver.findElement(TXT_EMAIL_FOR_MODIFICATION).clear();
		
		driver.findElement(TXT_DISPLAYNAME_FOR_MODIFICATION).sendKeys("testDisplayNameAfterModification");
		driver.findElement(TXT_EMAIL_FOR_MODIFICATION).sendKeys("testemailAfterModification@mail.hu");
		
		wait.until(ExpectedConditions.elementToBeClickable(BTN_SAVE_FOR_FOR_MODIFICATION));
		driver.findElement(BTN_SAVE_FOR_FOR_MODIFICATION).click();
		
		forceSleep();
		val modifiedItem = sqlExecutor(SQL_MODIFIED_ITEM);
		val displayName = modifiedItem.getString(1);
		val email = modifiedItem.getString(2);
		
		assertEquals("testDisplayNameAfterModification", displayName);
		assertEquals("testemailAfterModification@mail.hu", email);
		logout();		
	}
	
	@Test
	public void t3_userDeleteTest() throws SQLException{
		login("admin","admin");
		menuSelector(1,2);
		wait.until(ExpectedConditions.presenceOfElementLocated(GRID));
		
		val numberOfElementsBeforeSaving = sqlExecutor(SQL_NUMBER_OF_ELEMENTS);
		val beforeSaving = numberOfElementsBeforeSaving.getInt(1);
		
		driver.findElements(TAB_SHEETS).get(2).click();
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(TXT_USERNAME_FOR_DELETING));
		val rows = driver.findElements(GRID_ROWS);
		rows.get(rows.size()-1).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(BTN_DELETE));
		driver.findElement(BTN_DELETE).click();
		
		forceSleep();		
		val numberOfElementsAfterSaving = sqlExecutor(SQL_NUMBER_OF_ELEMENTS);
		val afterSaving = numberOfElementsAfterSaving.getInt(1);
		
		assertTrue(beforeSaving-1 == afterSaving );		
		logout();		
	}	
}