package com.insureedge.tests.harish;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class US17P306LoginButtonUIValidation extends BaseUiTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        baseSetup();
        String loginUrl=config.getProperty("login.url", "").trim();

        new LoginPage(driver,wait).open(loginUrl);
        driver.manage().window().maximize();
    }

    @Test
    public void testLoginButtonText() {
        WebElement loginBtn = driver.findElement(By.id("BtnLogin"));
        String buttonText = loginBtn.getAttribute("value");

        if (buttonText.equalsIgnoreCase("Login")) {
            System.out.println("PASS → Button text is correct: " + buttonText);
        } else {
            System.out.println("FAIL → Expected 'Login' but found: " + buttonText);
        }
    }

    @Test
    public void testLoginButtonEnabled() {
        WebElement loginBtn = driver.findElement(By.id("BtnLogin"));

        if (loginBtn.isEnabled()) {
            System.out.println("PASS → Login button is clickable/enabled.");
        } else {
            System.out.println("FAIL → Login button is NOT enabled.");
        }
    }
}