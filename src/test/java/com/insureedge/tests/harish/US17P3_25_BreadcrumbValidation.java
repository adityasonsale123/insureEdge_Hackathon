package com.insureedge.tests.harish;

import com.insureedge.base.BaseUiTest;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class US17P3_25_BreadcrumbValidation extends BaseUiTest {

    @BeforeClass(alwaysRun = true)
    public void setupAndLogin() throws Exception {
        // NOTE:
        // - BaseUiTest.baseSetup() runs automatically (superclass @BeforeClass).
        // - It launches Chrome, sets waits, maximizes the window, and loads config.
        // - Here we only ensure we're logged in and on the dashboard.

        // Reuse central login + dashboard navigation from BaseUiTest
        loginIfNeeded();

        // Keep your beginner-friendly extra implicit wait (as in your original code)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // No @AfterClass here — BaseUiTest.baseTeardown() will quit the browser.

    @Test
    public void validateBreadcrumbHomeDashboard() throws Exception {

        // 1) Breadcrumb visible
        WebElement breadcrumb = driver.findElement(By.cssSelector("div.pagetitle nav ol.breadcrumb"));
        if (breadcrumb.isDisplayed()) {
            System.out.println("PASS → Breadcrumb is visible on Dashboard.");
        } else {
            System.out.println("FAIL → Breadcrumb is NOT visible.");
        }

        // 2) Validate breadcrumb items text: Home / Dashboard
        WebElement homeLink = driver.findElement(By.xpath("//div[@class='pagetitle']//ol[@class='breadcrumb']/li[1]/a"));
        WebElement dashboardText = driver.findElement(By.xpath("//div[@class='pagetitle']//ol[@class='breadcrumb']/li[2]"));

        String home = homeLink.getText().trim();
        String dash = dashboardText.getText().trim();

        if (home.equals("Home") && dash.equals("Dashboard")) {
            System.out.println("PASS → Breadcrumb text is correct: Home / Dashboard");
        } else {
            System.out.println("FAIL → Breadcrumb text incorrect. Found: " + home + " / " + dash);
        }

        // 3) Validate Navigation on click of Home
        homeLink.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("AdminDashboard")){
            System.out.println("PASS → Clicking Home navigates correctly to Dashboard.");
        } else {
            System.out.println("FAIL → Home navigation incorrect. URL: " + currentUrl);
        }
    }
}