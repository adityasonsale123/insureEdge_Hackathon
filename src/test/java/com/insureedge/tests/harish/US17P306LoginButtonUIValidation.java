package com.insureedge.tests.harish;

import com.insureedge.pages.LoginPage;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class US17P306LoginButtonUIValidation {

    private WebDriver openLogin(String url) {
        WebDriver d = new ChromeDriver();
        d.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        d.manage().window().maximize();
        new LoginPage(d, new org.openqa.selenium.support.ui.WebDriverWait(d, Duration.ofSeconds(10))).open(url);
        return d;
    }

    @Test
    public void testLoginButtonText() {
        WebDriver driver = openLogin("https://your-login-url-here");
        try {
            WebElement loginBtn = driver.findElement(By.id("BtnLogin"));
            String text = loginBtn.getAttribute("value");
            Assert.assertTrue(text != null && text.equalsIgnoreCase("Login"),
                    "Expected 'Login' but found: " + text);
        } finally {
            driver.quit();
        }
    }

    @Test
    public void testLoginButtonEnabled() {
        WebDriver driver = openLogin("https://your-login-url-here");
        try {
            WebElement loginBtn = driver.findElement(By.id("BtnLogin"));
            Assert.assertTrue(loginBtn.isEnabled(), "Login button should be enabled.");
        } finally {
            driver.quit();
        }
    }
}