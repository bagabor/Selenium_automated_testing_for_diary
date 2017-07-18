package com.example.selenium.selenium.associationtest;

import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.example.selenium.selenium.BasicTestClass;

import lombok.val;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserCourseAssocTest extends BasicTestClass{

	private static final By GRID_FOR_USERS 								= By.id("gridForUsers");
	private static final By GRID_FOR_COURSES 							= By.cssSelector("#gridForCourses tr");	
	private static final By GRID_ROWS									= By.cssSelector(".v-grid-body tr");
	private static final By BTN_MODIFY									= By.id("btnModification");
	private static final By CMB_SEMESTER								= By.cssSelector("#cmbSemester .v-filterselect-input");
	private static final By CMB_COURSES									= By.cssSelector("#cmbCourses .v-filterselect-input");
	private static final By TXT_RESULT_FOR_MODIFICATION					= By.id("txtResultForEditing");
	private static final By BTN_SAVE_FOR_MODIFICATION					= By.id("btnSaveForEdit");	
	private static final By BTN_SAVE									= By.id("btnSave");
	private static final By BTN_DELETE									= By.id("btnRemove");	
	private static final By TAB_SHEETS									= By.cssSelector(".v-tabsheet-tabs .v-tabsheet-tabitemcell");
	private static final By TXT_COURSE_NAME_FOR_CREATION 				= By.id("txtCourseNameForCreation");
	private static final By TXT_COURSE_CREDIT_NUMBER_FOR_CREATION 		= By.id("txtCreditNumberForCreation");
	private static final By TXT_COURSE_SEMESTER_FOR_CREATION 			= By.id("txtSemesterForCreation");
	private static final By BTN_SAVE_FOR_CREATION 						= By.id("btnSaveForCreation");
	private static final By GRID 										= By.id("grid");
	
	
	@Test
	public void t1_courseCreationTest() throws SQLException{
		login("admin","admin");
		menuSelector(1,1);
		wait.until(ExpectedConditions.presenceOfElementLocated(GRID));		
		
		driver.findElement(TXT_COURSE_CREDIT_NUMBER_FOR_CREATION).clear();
		driver.findElement(TXT_COURSE_SEMESTER_FOR_CREATION).clear();
        wait.until(ExpectedConditions.textToBe(TXT_COURSE_CREDIT_NUMBER_FOR_CREATION, StringUtils.EMPTY));
        wait.until(ExpectedConditions.textToBe(TXT_COURSE_SEMESTER_FOR_CREATION, StringUtils.EMPTY));
		
		driver.findElement(TXT_COURSE_NAME_FOR_CREATION).sendKeys("testCourse");
		driver.findElement(TXT_COURSE_CREDIT_NUMBER_FOR_CREATION).sendKeys("2");
		driver.findElement(TXT_COURSE_SEMESTER_FOR_CREATION).sendKeys("2");
		
		forceSleep();	
		wait.until(ExpectedConditions.elementToBeClickable(BTN_SAVE_FOR_CREATION));
		driver.findElement(BTN_SAVE_FOR_CREATION).click();	
		logout();
	}
	
	@Test
	public void t2_assocCreationTest() throws SQLException{
		init();		
		driver.findElement(CMB_SEMESTER).sendKeys("2");
		driver.findElement(CMB_SEMESTER).sendKeys(Keys.ENTER);
		
		driver.findElement(CMB_COURSES).sendKeys("testCourse");
		driver.findElement(CMB_COURSES).sendKeys(Keys.ENTER);
		wait.until(ExpectedConditions.elementToBeClickable(BTN_SAVE));
		driver.findElement(BTN_SAVE).click();
		logout();
	}
	
	@Test
	public void t3_assocModificationTest() throws SQLException{
		init();		
		val coursesGridRows = driver.findElements(GRID_FOR_COURSES);
		coursesGridRows.get(coursesGridRows.size()-1).click();
		
		driver.findElements(TAB_SHEETS).get(1).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(TXT_RESULT_FOR_MODIFICATION));

		driver.findElement(TXT_RESULT_FOR_MODIFICATION).clear();		
		driver.findElement(TXT_RESULT_FOR_MODIFICATION).sendKeys("2");
		wait.until(ExpectedConditions.elementToBeClickable(BTN_SAVE_FOR_MODIFICATION));
		driver.findElement(BTN_SAVE_FOR_MODIFICATION).click();
		logout();
	}
	
	@Test
	public void t4_assocDeletingTest() throws SQLException{
		init();
		val coursesGridRows = driver.findElements(GRID_FOR_COURSES);
		coursesGridRows.get(coursesGridRows.size()-1).click();
		
		driver.findElements(TAB_SHEETS).get(2).click();
		wait.until(ExpectedConditions.elementToBeClickable(BTN_DELETE));
		driver.findElement(BTN_DELETE).click();
		logout();
	}
	
	private void init(){
		login("admin","admin");
		menuSelector(1,0);
		wait.until(ExpectedConditions.presenceOfElementLocated(GRID_FOR_USERS));
		forceSleep();
		
		val rows = driver.findElements(GRID_ROWS);
		rows.get(rows.size()-1).click();
		wait.until(ExpectedConditions.elementToBeClickable(BTN_MODIFY));
		driver.findElement(BTN_MODIFY).click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(CMB_SEMESTER));
		wait.until(ExpectedConditions.presenceOfElementLocated(CMB_COURSES));
	}
}
