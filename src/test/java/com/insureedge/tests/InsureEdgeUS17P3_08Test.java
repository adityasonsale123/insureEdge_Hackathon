package com.insureedge.tests;

import org.openqa.selenium.support.Color;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

public class InsureEdgeUS17P3_08Test extends BaseUiTest {

    WebDriver driver;
    WebElement footer;

    @BeforeClass
    public void setUp() {
        // Bind to the WebDriver initialized by BaseUiTest
        this.driver = super.driver;

        // Reuse base login flow (reads config: loginUrl, adminUser, adminPass, optional dashboardUrl)
        loginIfNeeded();

        // Ensure we are on the login page as per your original behavior
        String loginUrl = mustGet("loginUrl");
        driver.get(loginUrl);

        driver.manage().window().maximize();
        footer = driver.findElement(By.xpath("//div[@class='credits']"));
    }

    // Task 1
    @Test
    public void verifyFooterVisible() {
        if (footer.isDisplayed()) {
            System.out.println("Footer text is visible.");
        }
    }

    // Task 2
    @Test
    public void verifyFooterNonClickable() {
        System.out.println("Footer displayed: " +footer.isDisplayed() + ", enabled: " +footer.isEnabled());
        if (!footer.getTagName().equalsIgnoreCase("button")) {
            System.out.println("The footer is not clickable UI element");
        }
    }

    // Task 3
    @Test
    public void verifyFooterTextColor() {
        String color = footer.getCssValue("color"); // returns rgba format
        String hexColor = Color.fromString(color).asHex();
        System.out.println("Footer text color: " + hexColor);
    }

    @AfterClass
    public void closeBrowser() {
        // Let BaseUiTest handle driver quit.
        // Intentionally left blank to avoid double-quit.
    }
}
