package com.insureedge.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    private final By username = By.id("txtUsername");
    private final By password = By.id("txtPassword");
    private final By loginBtn = By.id("BtnLogin");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void open(String url) {
        driver.get(url);
    }

    public void login(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginBtn);
    }

    public boolean isAt() {
        return driver.getCurrentUrl().toLowerCase().contains("login");
    }
}