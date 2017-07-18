package com.example.selenium.selenium.courseconfigtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.example.selenium.selenium.BasicTestClass;

import lombok.val;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CourseTest extends BasicTestClass{
	
	private static final By GRID 										= By.id("grid");
	private static final By GRID_ROWS									= By.cssSelector(".v-grid-body tr");	
	private static final By TXT_COURSE_NAME_FOR_CREATION 				= By.id("txtCourseNameForCreation");
	private static final By TXT_COURSE_CREDIT_NUMBER_FOR_CREATION 		= By.id("txtCreditNumberForCreation");
	private static final By TXT_COURSE_SEMESTER_FOR_CREATION 			= By.id("txtSemesterForCreation");
	private static final By BTN_SAVE_FOR_CREATION 						= By.id("btnSaveForCreation");
	private static final By TAB_SHEETS									= By.cssSelector(".v-tabsheet-tabs .v-tabsheet-tabitemcell");
	private static final By TXT_COURSE_NAME_FOR_MODIFICATION			= By.id("txtCourseName");
	private static final By TXT_COURSE_CREDIT_NUMBER_FOR_MODIFICATION 	= By.id("txtCreditNumber");
	private static final By TXT_COURSE_SEMESTER_FOR_MODIFICATION 		= By.id("txtSemester");
	private static final By BTN_SAVE_FOR_MODIFICATION					= By.id("btnSaveForEditing");
	private static final By TXT_COURSE_NAME_FOR_DELETING				= By.id("txtCourseNameForDeleting");
	private static final By BTN_DELETE									= By.id("btnDelete");
	
	private static final String SQL_NUMBER_OF_ELEMENTS					= "SELECT COUNT(*) FROM COURSE_TYPE WHERE COURSE_NAME LIKE 'testCourse'";
	private static final String SQL_NUMBER_OF_MODIFIED_ELEMENTS			= "SELECT COUNT(*) FROM COURSE_TYPE WHERE COURSE_NAME LIKE 'testCourseNameAfterModification'";
	private static final String SQL_MODIFIED_ITEM						= "SELECT COURSE_NAME,CREDIT_NUMBER,SEMESTER FROM COURSE_TYPE WHERE COURSE_NAME LIKE 'testCourseNameAfterModification'";
	
	@Test
	public void t1_courseCreationTest() throws SQLException{
		login("admin","admin");
		menuSelector(1,1);
		wait.until(ExpectedConditions.presenceOfElementLocated(GRID));
		
		val numberOfElementsBeforeSaving = sqlExecutor(SQL_NUMBER_OF_ELEMENTS);
		val beforeSaving = numberOfElementsBeforeSaving.getInt(1);
		
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
		
		forceSleep();		
		val numberOfElementsAfterSaving = sqlExecutor(SQL_NUMBER_OF_ELEMENTS);
		val afterSaving = numberOfElementsAfterSaving.getInt(1);
		assertTrue(beforeSaving+1 == afterSaving );
		logout();
	}
	
	@Test
	public void t2_courseModificationTest() throws SQLException{
		login("admin","admin");
		menuSelector(1,1);
		wait.until(ExpectedConditions.presenceOfElementLocated(GRID));
		
		driver.findElements(TAB_SHEETS).get(1).click();
		val rows = driver.findElements(GRID_ROWS);
		rows.get(rows.size()-1).click();
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(TXT_COURSE_NAME_FOR_MODIFICATION));
		
		driver.findElement(TXT_COURSE_CREDIT_NUMBER_FOR_MODIFICATION).clear();
		driver.findElement(TXT_COURSE_SEMESTER_FOR_MODIFICATION).clear();
		driver.findElement(TXT_COURSE_NAME_FOR_MODIFICATION).clear();
		wait.until(ExpectedConditions.textToBe(TXT_COURSE_NAME_FOR_MODIFICATION, StringUtils.EMPTY));
		driver.findElement(TXT_COURSE_NAME_FOR_MODIFICATION).sendKeys("testCourseNameAfterModification");
		driver.findElement(TXT_COURSE_CREDIT_NUMBER_FOR_MODIFICATION).sendKeys("3");
		driver.findElement(TXT_COURSE_SEMESTER_FOR_MODIFICATION).sendKeys("3");
		
		forceSleep();
		wait.until(ExpectedConditions.elementToBeClickable(BTN_SAVE_FOR_MODIFICATION));
		driver.findElement(BTN_SAVE_FOR_MODIFICATION).click();
		
		forceSleep();
		val modifiedItem = sqlExecutor(SQL_MODIFIED_ITEM);
		val name = modifiedItem.getString(1);
		val credit = modifiedItem.getInt(2);
		val semester = modifiedItem.getInt(3);
		
		assertEquals("testCourseNameAfterModification", name);
		assertTrue(credit == 3);
		assertTrue(semester == 3);
		logout();		
	}
	
	@Test
	public void t3_courseDeleteTest() throws SQLException{
		login("admin","admin");
		menuSelector(1,1);
		wait.until(ExpectedConditions.presenceOfElementLocated(GRID));
		
		val numberOfElementsBeforeSaving = sqlExecutor(SQL_NUMBER_OF_MODIFIED_ELEMENTS);
		val beforeSaving = numberOfElementsBeforeSaving.getInt(1);
		
		driver.findElements(TAB_SHEETS).get(2).click();
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(TXT_COURSE_NAME_FOR_DELETING));
		val rows = driver.findElements(GRID_ROWS);
		rows.get(rows.size()-1).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(BTN_DELETE));
		driver.findElement(BTN_DELETE).click();
		
		forceSleep();		
		val numberOfElementsAfterSaving = sqlExecutor(SQL_NUMBER_OF_MODIFIED_ELEMENTS);
		val afterSaving = numberOfElementsAfterSaving.getInt(1);
		
		assertTrue(beforeSaving-1 == afterSaving );		
		logout();		
	}	
}
