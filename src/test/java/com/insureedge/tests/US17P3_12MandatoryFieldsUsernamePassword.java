package com.insureedge.tests;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

public class US17P3_12MandatoryFieldsUsernamePassword extends BaseUiTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://qeaskillhub.cognizant.com/LoginPage?logout=true");
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @Test
    public void testBothEmpty() {
        driver.findElement(By.id("BtnLogin")).click();
        String msg1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lblMessage"))).getText();
        System.out.println("Both empty → Message: " + msg1);
        // You can add assertions here
        // Assert.assertEquals(msg1, "Expected message");
    }

    @Test
    public void testPasswordOnly() {
        driver.findElement(By.id("txtPassword")).sendKeys("Test123!");
        driver.findElement(By.id("BtnLogin")).click();
        String msg2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lblMessage"))).getText();
        System.out.println("Password only → Message: " + msg2);
        driver.findElement(By.id("txtPassword")).clear();
        // Assert.assertEquals(msg2, "Expected message");
    }

    @Test
    public void testUsernameOnly() {
        driver.findElement(By.id("txtUsername")).sendKeys("demo123");
        driver.findElement(By.id("BtnLogin")).click();
        String msg3 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lblMessage"))).getText();
        System.out.println("Username only → Message: " + msg3);
        // Assert.assertEquals(msg3, "Expected message");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
