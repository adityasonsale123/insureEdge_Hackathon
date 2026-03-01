package com.insureedge.tests.harish;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class US17P3_17PrimaryFieldsNameEmailUIValidation extends BaseUiTest {

    @BeforeClass(alwaysRun = true)
    public void setup() throws Exception
        {
            baseSetup();
            String loginUrl=config.getProperty("login.url", "").trim();

            new LoginPage(driver,wait).open(loginUrl);
            driver.manage().window().maximize();
        }

    @Test()
    public void testEnterNameAndEmailOnCreateAccount() throws Exception {
        // Click "Create an account" link
        WebElement createlink = driver.findElement(By.xpath("//a[text()='Create an account']"));
        createlink.click();

        // Keep your implicit wait usage as in original code
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Enter Name
        WebElement nameField = driver.findElement(By.id("yourName"));
        nameField.sendKeys("harish");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Enter Email
        WebElement emailField = driver.findElement(By.id("yourEmail"));
        emailField.sendKeys("ahbdajbad123@gmail.com");

        System.out.println("Entered Name and Email successfully on Create Account page.");
    }
}