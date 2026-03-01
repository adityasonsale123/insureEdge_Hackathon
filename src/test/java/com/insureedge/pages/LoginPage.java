package com.insureedge.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Minimal Login Page used only by BaseUiTest.loginIfNeeded().
 * 👉 Adjust the three locators to match your real login page.
 */
public class LoginPage extends BasePage {

    private final By txtUser  = By.id("username");
    private final By txtPass  = By.id("password");
    private final By btnLogin = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public LoginPage open(String url) {
        driver.get(url);
        return this;
    }

    public void login(String user, String pass) {
        type(txtUser, user);
        type(txtPass, pass);
        click(btnLogin);
    }
}