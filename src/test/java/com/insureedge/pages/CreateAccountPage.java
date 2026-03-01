package com.insureedge.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * "Create an Account" (NiceAdmin) page.
 * Only the essentials for a simple create-flow.
 */
public class CreateAccountPage extends BasePage {

    private final By hdrCreateAccount = By.cssSelector("h5.card-title.text-center");
    private final By txtName          = By.id("yourName");
    private final By txtEmail         = By.id("yourEmail");
    private final By txtUsername      = By.id("yourUsername");
    private final By txtPassword      = By.id("yourPassword");
    private final By chkAcceptTerms   = By.id("acceptTerms");
    private final By btnCreate        = By.cssSelector("button[type='submit']");

    public CreateAccountPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public CreateAccountPage open(String registerUrl) {
        driver.get(registerUrl);
        return this;
    }

    public CreateAccountPage waitForPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(hdrCreateAccount));
        visible(txtName);
        visible(btnCreate);
        return this;
    }

    public CreateAccountPage setName(String fullName)   { type(txtName, fullName);     return this; }
    public CreateAccountPage setEmail(String email)     { type(txtEmail, email);       return this; }
    public CreateAccountPage setUsername(String user)   { type(txtUsername, user);     return this; }
    public CreateAccountPage setPassword(String pass)   { type(txtPassword, pass);     return this; }

    public CreateAccountPage acceptTerms(boolean check) {
        WebElement el = visible(chkAcceptTerms);
        if (el.isSelected() != check) {
            scrollIntoView(el);
            el.click();
        }
        return this;
    }

    public CreateAccountPage submit() {
        click(btnCreate);
        return this;
    }

    /** One-shot create flow. */
    public CreateAccountPage createAccount(String name, String email, String username, String password) {
        return setName(name).setEmail(email).setUsername(username).setPassword(password).acceptTerms(true).submit();
    }

   

    // Expose locators if a test wants them (e.g., focus order checks)
    public By nameLocator()     { return txtName; }
    public By emailLocator()    { return txtEmail; }
    public By usernameLocator() { return txtUsername; }
    public By passwordLocator() { return txtPassword; }
    public By termsLocator()    { return chkAcceptTerms; }
    public By createBtnLocator(){ return btnCreate; }
}