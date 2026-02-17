package com.insureedge.tests.harish;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class US17P3_12MandatoryFieldsUsernamePassword extends BaseUiTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        baseSetup();
        String loginUrl=config.getProperty("login.url", "").trim();

        new LoginPage(driver,wait).open(loginUrl);
        driver.manage().window().maximize();
    }

    @Test
    public void runOriginalScenariosWithBaseDriver() throws InterruptedException {
        // ---- Keep original variable names exactly the same ----
        WebDriver driver = this.driver; // reuse BaseUiTest's driver
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // ---------------- Scenario 1: Both empty ----------------
        driver.findElement(By.id("BtnLogin")).click();

        String msg1 = wait
                .until(ExpectedConditions.presenceOfElementLocated(By.id("lblMessage")))
                .getText();
        System.out.println("Both empty → Message: " + msg1);

        // ---------------- Scenario 2: Password only ----------------
        driver.findElement(By.id("txtPassword")).sendKeys("Test123!");
        driver.findElement(By.id("BtnLogin")).click();

        String msg2 = wait
                .until(ExpectedConditions.presenceOfElementLocated(By.id("lblMessage")))
                .getText();
        System.out.println("Password only → Message: " + msg2);

        // Clear password for next scenario
        driver.findElement(By.id("txtPassword")).clear();

        // ---------------- Scenario 3: Username only ----------------
        driver.findElement(By.id("txtUsername")).sendKeys("demo123");
        driver.findElement(By.id("BtnLogin")).click();

        String msg3 = wait
                .until(ExpectedConditions.presenceOfElementLocated(By.id("lblMessage")))
                .getText();
        System.out.println("Username only → Message: " + msg3);

        // Do NOT quit here—BaseUiTest handles teardown
    }
}