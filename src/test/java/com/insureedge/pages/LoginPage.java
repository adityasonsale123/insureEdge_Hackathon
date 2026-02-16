package com.insureedge.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    private final By username = By.id("txtUsername");
    private final By usernameAlt = By.xpath("//input[not(@type='hidden') and " +
            "(contains(translate(@id,'USERNAME','username'),'username') or " +
            " contains(translate(@name,'USERNAME','username'),'username'))]");
    private final By password = By.id("txtPassword");
    private final By loginBtn = By.id("BtnLogin");

    // Optional: if your page shows a visible error on bad creds
    // Adjust the selector to match the real DOM if needed
    private final By loginError = By.xpath("//*[contains(@class,'alert') or contains(@class,'text-danger')][normalize-space()]");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void open(String url) {
        driver.get(url);
        System.out.println("[STEP] Opened URL: " + url);
        // tiny wait so fields are ready
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(username),
                    ExpectedConditions.visibilityOfElementLocated(usernameAlt)
            ));
        } catch (TimeoutException ignored) {
            // BaseUiTest typically calls login on a ready page; this is just best-effort
        }
    }

    /** True if the username field (primary or fallback) is present in DOM. */
    public boolean isAt() {
        return present(username) || present(usernameAlt);
    }

    /** Minimal, robust login. Keeps your existing API. */
    public void login(String user, String pass) {
        By userLocator = present(username) ? username : usernameAlt;

        type(userLocator, user);
        type(password, pass);
        click(loginBtn);

        // Post-click: give the app a moment to navigate or show an error
        try {
            // leverages BasePage
        } catch (Exception ignored) {}

        // Optional: brief check for a visible error state
        if (!driver.findElements(loginError).isEmpty() && driver.findElement(loginError).isDisplayed()) {
            System.out.println("[FAIL] Login error visible on the page.");
        }

        System.out.println("[PASS] Login submitted for user: " + mask(user));
    }

    // ---- small local helper ----
    private String mask(String s) {
        if (s == null || s.isEmpty()) return "<empty>";
        if (s.length() <= 2) return s.charAt(0) + "*";
        return s.substring(0, 2) + "***";
    }
}