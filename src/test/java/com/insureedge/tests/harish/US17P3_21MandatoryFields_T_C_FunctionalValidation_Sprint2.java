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

    private CreateAccountPage create;
    private JavascriptExecutor js;

    @BeforeClass(alwaysRun = true)
    public void openCreateAccount() {
        // BaseUiTest already launches browser and loads config.
        // Just open login page and click "Create an account".
        String loginUrl = config.getProperty("login.url", "").trim();
        new LoginPage(driver, wait).open(loginUrl);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        // Go to Create Account
        driver.findElement(By.xpath("//a[normalize-space()='Create an account']")).click();

        // Init page + js helper
        create = new CreateAccountPage(driver, wait).waitForPage();
        js = (JavascriptExecutor) driver;
    }

    @Test(priority = 1)
    public void nameIsEmpty_shouldBlockSubmit() {
        // Locate fields (using known IDs)
        WebElement name        = driver.findElement(By.id("yourName"));
        WebElement email       = driver.findElement(By.id("yourEmail"));
        WebElement username    = driver.findElement(By.id("yourUsername"));
        WebElement password    = driver.findElement(By.id("yourPassword"));
        WebElement terms       = driver.findElement(By.id("acceptTerms"));
        WebElement createBtn   = driver.findElement(By.cssSelector("button[type='submit']"));

        // --- Fill with NAME empty ---
        name.clear(); // leave empty
        email.clear();       email.sendKeys("valid@mail.com");
        username.clear();    username.sendKeys("testuser");
        password.clear();    password.sendKeys("Password123");
        if (!terms.isSelected()) terms.click();

        js.executeScript("window.scrollBy(0, 300);");
        createBtn.click();

        boolean nameValid = (Boolean) js.executeScript("return arguments[0].checkValidity();", name);
        assert !nameValid : "FAIL → Name was empty but form allowed submission.";
        System.out.println("PASS → Create button blocked because NAME is empty.");
    }

    @Test(priority = 2)
    public void emailIsEmpty_shouldBlockSubmit() {
        // Refresh to reset the form
        driver.navigate().refresh();
        create.waitForPage();

        WebElement name        = driver.findElement(By.id("yourName"));
        WebElement email       = driver.findElement(By.id("yourEmail"));
        WebElement username    = driver.findElement(By.id("yourUsername"));
        WebElement password    = driver.findElement(By.id("yourPassword"));
        WebElement terms       = driver.findElement(By.id("acceptTerms"));
        WebElement createBtn   = driver.findElement(By.cssSelector("button[type='submit']"));

        // --- Fill with EMAIL empty ---
        name.clear();       name.sendKeys("Harish");
        email.clear();      // leave empty
        username.clear();   username.sendKeys("testuser");
        password.clear();   password.sendKeys("Password123");
        if (!terms.isSelected()) terms.click();

        js.executeScript("window.scrollBy(0, 300);");
        createBtn.click();

        boolean emailValid = (Boolean) js.executeScript("return arguments[0].checkValidity();", email);
        assert !emailValid : "FAIL → Email was empty but form allowed submission.";
        System.out.println("PASS → Create button blocked because EMAIL is empty.");
    }
}