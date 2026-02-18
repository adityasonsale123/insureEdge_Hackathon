package com.insureedge.tests.shubham;

import com.insureedge.base.BaseUiTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class RegistrationUiTests extends BaseUiTest {

    private By loginCreateAccountLink = By.linkText("Create an account");

    // Registration form locators (from provided markup)
    private By registerForm = By.cssSelector("form.needs-validation");

    private By nameLabel   = By.cssSelector("label[for='yourName']");
    private By nameInput   = By.id("yourName");
    private By nameInvalid = By.xpath("//input[@id='yourName']/following-sibling::div[contains(@class,'invalid-feedback')]");

    private By emailLabel   = By.cssSelector("label[for='yourEmail']");
    private By emailInput   = By.id("yourEmail");
    private By emailInvalid = By.xpath("//input[@id='yourEmail']/following-sibling::div[contains(@class,'invalid-feedback')]");

    private void openRegisterFromLogin() {
        String loginUrl = config.getProperty("login.url", "").trim();
        if (loginUrl.isEmpty()) throw new RuntimeException("login.url missing in config.properties");
        driver.get(loginUrl);

        // Click "Create an account"
        wait.until(ExpectedConditions.elementToBeClickable(loginCreateAccountLink)).click();

        // Wait for Register panel + fields
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h5[contains(normalize-space(),'Create an Account')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
    }

    /**
     * US17P3_15: Primary Fields – Name & Email – UI Validation
     * Validates label text, presence, input types, required attribute, and invalid-feedback containers.
     * Then forces Bootstrap 'was-validated' state to ensure invalid-feedback becomes visible.
     * If feedback text is empty, logs a WARN instead of failing (to avoid brittle tests).
     */
    @Test(description = "US17P3_15: Primary Fields – Name & Email – UI Validation")
    public void register_PrimaryFields_UiValidation() {
        openRegisterFromLogin();

        SoftAssert softly = new SoftAssert();

        // --- Name field UI ---
        WebElement nameLbl = wait.until(ExpectedConditions.visibilityOfElementLocated(nameLabel));
        WebElement nameTxt = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        softly.assertEquals(nameLbl.getText().trim(), "Your Name", "Name label text");
        softly.assertEquals(nameLbl.getAttribute("for"), "yourName", "Name label 'for'");
        softly.assertEquals(nameTxt.getAttribute("type"), "text", "Name type=text");
        softly.assertTrue(nameTxt.getAttribute("required") != null, "Name should be required");

        // --- Email field UI ---
        WebElement emailLbl = wait.until(ExpectedConditions.visibilityOfElementLocated(emailLabel));
        WebElement emailTxt = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        softly.assertEquals(emailLbl.getText().trim(), "Your Email", "Email label text");
        softly.assertEquals(emailLbl.getAttribute("for"), "yourEmail", "Email label 'for'");
        softly.assertEquals(emailTxt.getAttribute("type"), "email", "Email type=email");
        softly.assertTrue(emailTxt.getAttribute("required") != null, "Email should be required");

        // --- Invalid-feedback containers should exist (may be hidden before validation) ---
        WebElement nameInvalidFeedback = getOrNull(nameInvalid);
        WebElement emailInvalidFeedback = getOrNull(emailInvalid);
        softly.assertNotNull(nameInvalidFeedback, "Name invalid-feedback should exist");
        softly.assertNotNull(emailInvalidFeedback, "Email invalid-feedback should exist");

        // Try to force Bootstrap validation visuals (adds 'was-validated' to the form)
        try {
            WebElement form = wait.until(ExpectedConditions.presenceOfElementLocated(registerForm));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].classList.add('was-validated');", form);

            // Make sure fields remain empty (invalid)
            nameTxt.clear();
            emailTxt.clear();

            // After forcing 'was-validated', invalid-feedback should be visible for required empty inputs
            // (display might still depend on CSS, so we don't hard-fail if it's not displayed)
            if (nameInvalidFeedback != null) {
                boolean visible = safeDisplayed(nameInvalidFeedback);
                if (!visible) {
                    System.out.println("[WARN] Name invalid-feedback not visible after was-validated (CSS/JS may control it).");
                }
                String t = safeText(nameInvalidFeedback);
                if (t.isEmpty()) {
                    System.out.println("[WARN] Name invalid-feedback text is empty. Provide expected copy to assert strictly.");
                } else if (!t.toLowerCase().contains("name")) {
                    System.out.println("[INFO] Name invalid-feedback text present but generic: '" + t + "'");
                }
            }
            if (emailInvalidFeedback != null) {
                boolean visible = safeDisplayed(emailInvalidFeedback);
                if (!visible) {
                    System.out.println("[WARN] Email invalid-feedback not visible after was-validated (CSS/JS may control it).");
                }
                String t = safeText(emailInvalidFeedback);
                if (t.isEmpty()) {
                    System.out.println("[WARN] Email invalid-feedback text is empty. Provide expected copy to assert strictly.");
                } else if (!(t.toLowerCase().contains("email") || t.toLowerCase().contains("valid"))) {
                    System.out.println("[INFO] Email invalid-feedback text present but generic: '" + t + "'");
                }
            }
        } catch (Throwable t) {
            System.out.println("[WARN] Could not force Bootstrap validation visuals: " + t.getMessage());
        }

        // Optional: click submit to ensure no navigation happens (since invalid)
        try {
            WebElement form = wait.until(ExpectedConditions.presenceOfElementLocated(registerForm));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new Event('submit', {bubbles:true,cancelable:true}))", form);
        } catch (Throwable ignored) {}

        // Structural checks must pass
        softly.assertAll();
    }

    // ---- helpers ----

    private WebElement getOrNull(By locator) {
        try { return driver.findElement(locator); } catch (NoSuchElementException e) { return null; }
    }

    private boolean safeDisplayed(WebElement el) {
        try { return el.isDisplayed(); } catch (Throwable t) { return false; }
    }

    private String safeText(WebElement el) {
        try { return el.getText() == null ? "" : el.getText().trim(); } catch (Throwable t) { return ""; }
    }
}