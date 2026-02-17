package com.insureedge.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the "Create an Account" page (NiceAdmin template).
 * Uses IDs: yourName, yourEmail, yourUsername, yourPassword, acceptTerms
 * and submit button with type='submit'.
 */
public class CreateAccountPage extends BasePage {

    // ===== Locators =====
    private final By hdrCreateAccount = By.cssSelector("h5.card-title.text-center");
    private final By txtName          = By.id("yourName");
    private final By txtEmail         = By.id("yourEmail");
    private final By txtUsername      = By.id("yourUsername");
    private final By txtPassword      = By.id("yourPassword");
    private final By chkAcceptTerms   = By.id("acceptTerms");
    private final By btnCreate        = By.cssSelector("button[type='submit']");

    // Bootstrap invalid feedback blocks shown on invalid input
    private final By invalidFeedbacks = By.cssSelector(".invalid-feedback");

    public CreateAccountPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ===== Navigation / Page readiness =====

    /** Navigate directly to the registration page URL. */
    public CreateAccountPage open(String registerUrl) {
        driver.get(registerUrl);
        return this;
    }

    /** Wait for header + key controls. */
    public CreateAccountPage waitForPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(hdrCreateAccount));
        visible(txtName);
        visible(btnCreate);
        return this;
    }

    /** Heuristic to confirm we're on the Create Account page. */
    public boolean isAt() {
        try {
            String t = visible(hdrCreateAccount).getText().trim().toLowerCase();
            return t.contains("create an account");
        } catch (Exception e) {
            return false;
        }
    }

    // ===== Field interactions (fluent) =====

    public CreateAccountPage setName(String fullName) {
        type(txtName, fullName);
        return this;
    }

    public CreateAccountPage setEmail(String email) {
        type(txtEmail, email);
        return this;
    }

    public CreateAccountPage setUsername(String username) {
        type(txtUsername, username);
        return this;
    }

    public CreateAccountPage setPassword(String password) {
        type(txtPassword, password);
        return this;
    }

    public CreateAccountPage acceptTerms(boolean check) {
        WebElement el = visible(chkAcceptTerms);
        if (el.isSelected() != check) {
            focusJS(el); // scroll into view + focus (from BasePage)
            el.click();
        }
        return this;
    }

    public CreateAccountPage submit() {
        click(btnCreate); // BasePage.click has JS fallback
        return this;
    }

    /** High-level flow to fill all fields and submit. */
    public CreateAccountPage createAccount(String name, String email, String username, String password) {
        setName(name)
                .setEmail(email)
                .setUsername(username)
                .setPassword(password)
                .acceptTerms(true)
                .submit();
        return this;
    }

    // ===== Validation helpers (for your mandatory field checks) =====

    /** Generic HTML5 validity via JS: element.checkValidity() */
    public boolean isFieldValid(By field) {
        try {
            WebElement el = visible(field);
            Object ok = js.executeScript("return arguments[0].checkValidity();", el);
            return (ok instanceof Boolean) && (Boolean) ok;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNameValid()     { return isFieldValid(txtName); }
    public boolean isEmailValid()    { return isFieldValid(txtEmail); }
    public boolean isUsernameValid() { return isFieldValid(txtUsername); }
    public boolean isPasswordValid() { return isFieldValid(txtPassword); }

    /** True if any .invalid-feedback is displayed with text. */
    public boolean anyInvalidFeedbackVisible() {
        try {
            for (WebElement el : driver.findElements(invalidFeedbacks)) {
                if (el.isDisplayed() && !el.getText().trim().isEmpty()) {
                    return true;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    // ===== Element getters (optional to ease transition in tests) =====

    public WebElement nameField()     { return visible(txtName); }
    public WebElement emailField()    { return visible(txtEmail); }
    public WebElement usernameField() { return visible(txtUsername); }
    public WebElement passwordField() { return visible(txtPassword); }
    public WebElement termsCheckbox() { return visible(chkAcceptTerms); }
    public WebElement createButton()  { return visible(btnCreate); }

    public By nameLocator()     { return txtName; }
    public By emailLocator()    { return txtEmail; }
    public By usernameLocator() { return txtUsername; }
    public By passwordLocator() { return txtPassword; }
    public By termsLocator()    { return chkAcceptTerms; }
    public By createBtnLocator(){ return btnCreate; }
}