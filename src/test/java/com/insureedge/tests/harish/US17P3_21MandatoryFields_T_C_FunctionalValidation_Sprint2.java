package com.insureedge.tests.harish;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.CreateAccountPage;
import com.insureedge.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class US17P3_21MandatoryFields_T_C_FunctionalValidation_Sprint2 extends BaseUiTest {

    // Keep the same variable names as in your original code
    static CreateAccountPage c;
    static JavascriptExecutor js;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        baseSetup();

        // Open login via config (same as your original)
        String loginUrl = config.getProperty("login.url", "").trim();
        new LoginPage(driver, wait).open(loginUrl);

        driver.manage().window().maximize();
        // If you want some implicit wait, keep it low to avoid masking issues
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        // Navigate to "Create an account" using the link on Login page
        driver.findElement(By.xpath("//a[normalize-space()='Create an account']")).click();

        // Initialize page object + JS executor
        c = new CreateAccountPage(driver, wait).waitForPage();
        js = (JavascriptExecutor) driver;
    }

    @Test(priority = 1)
    public void test_NameIsEmpty() {
        // Ensure we are on the page and fields are visible
        c.waitForPage();

        // Use page getters for elements (no brittle finds)
        WebElement nameField = c.nameField();
        WebElement emailField = c.emailField();
        WebElement usernameField = c.usernameField();
        WebElement passwordField = c.passwordField();
        WebElement termsCheck  = c.termsCheckbox();
        WebElement createBtn   = c.createButton();

        System.out.println("===== Test: Name is EMPTY =====");

        // Steps
        nameField.clear(); // leave empty
        emailField.clear();
        emailField.sendKeys("valid@mail.com");
        usernameField.clear();
        usernameField.sendKeys("testuser");
        passwordField.clear();
        passwordField.sendKeys("Password123");
        if (!termsCheck.isSelected()) {
            termsCheck.click();
        }

        js.executeScript("window.scrollBy(0, 400);");
        createBtn.click();

        boolean nameValid = (Boolean) js.executeScript("return arguments[0].checkValidity();", nameField);

        if (!nameValid) {
            System.out.println("PASS → Create button blocked because NAME is empty.");
        } else {
            System.out.println("FAIL → Name empty but form allowed submission.");
        }
    }

    @Test(priority = 2)
    public void test_EmailIsEmpty() {
        // Refresh and wait for page
        driver.navigate().refresh();
        c.waitForPage();

        WebElement nameField = c.nameField();
        WebElement emailField = c.emailField();
        WebElement usernameField = c.usernameField();
        WebElement passwordField = c.passwordField();
        WebElement termsCheck  = c.termsCheckbox();
        WebElement createBtn   = c.createButton();

        System.out.println("===== Test: Email is EMPTY =====");

        nameField.clear();
        nameField.sendKeys("Harish");
        emailField.clear(); // leave empty
        usernameField.clear();
        usernameField.sendKeys("testuser");
        passwordField.clear();
        passwordField.sendKeys("Password123");
        if (!termsCheck.isSelected()) {
            termsCheck.click();
        }

        js.executeScript("window.scrollBy(0, 400);");
        createBtn.click();

        boolean emailValid = (Boolean) js.executeScript("return arguments[0].checkValidity();", emailField);

        if (!emailValid) {
            System.out.println("PASS → Create button blocked because EMAIL is empty.");
        } else {
            System.out.println("FAIL → Email empty but form allowed submission.");
        }
    }
}