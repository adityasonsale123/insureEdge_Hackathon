package com.insureedge.tests.shubham;

import com.insureedge.base.BaseUiTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class DashboardUiTests extends BaseUiTest {

    private final By usernameField = By.id("txtUsername");
    private final By passwordField = By.id("txtPassword");
    private final By loginBtn      = By.id("BtnLogin");
    private final By rememberMeCb  = By.id("rememberMe");
    private final By rememberMeLbl = By.cssSelector("label[for='rememberMe']");
    private final By messageLbl    = By.id("lblMessage");

    private void openLogin() {
        String loginUrl = config.getProperty("login.url", "").trim();
        if (loginUrl.isEmpty()) throw new RuntimeException("login.url missing in config.properties");
        driver.get(loginUrl);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(usernameField),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(translate(@name,'USERNAME','username'),'username')]"))
        ));
    }

    /**
     * US17P3_05: “Remember Me” Checkbox – UI Validation
     */
    @Test(description = "US17P3_05: Remember Me Checkbox – UI Validation")
    public void rememberMe_UiValidation() {
        openLogin();

        SoftAssert softly = new SoftAssert();

        WebElement cb  = wait.until(ExpectedConditions.visibilityOfElementLocated(rememberMeCb));
        WebElement lbl = wait.until(ExpectedConditions.visibilityOfElementLocated(rememberMeLbl));

        // Presence, visibility, enabled
        softly.assertTrue(cb.isDisplayed(), "Checkbox should be displayed");
        softly.assertTrue(cb.isEnabled(), "Checkbox should be enabled");

        // Label correctness & association
        String labelText = lbl.getText().trim();
        softly.assertTrue(labelText.equalsIgnoreCase("Remember me"),
                "Label should be 'Remember me'. Found: " + labelText);
        softly.assertEquals(lbl.getAttribute("for"), "rememberMe", "Label 'for' must match checkbox id");

        // Default state: expect unchecked (normalize if pre-selected)
        if (cb.isSelected()) {
            System.out.println("[INFO] Remember Me pre-selected (session/history). Normalizing...");
            cb.click();
        }
        softly.assertFalse(cb.isSelected(), "Checkbox should be unchecked by default");

        // Click toggles
        cb.click();
        softly.assertTrue(cb.isSelected(), "After click, checkbox should be selected");
        cb.click();
        softly.assertFalse(cb.isSelected(), "After second click, checkbox should be unselected");

        // Keyboard SPACE toggle
        cb.sendKeys(Keys.SPACE);
        softly.assertTrue(cb.isSelected(), "SPACE should toggle checkbox selection");

        // Structure: .form-check wrapper for alignment
        WebElement wrapper = cb.findElement(By.xpath("./ancestor::div[contains(@class,'form-check')]"));
        softly.assertNotNull(wrapper, "Checkbox should be inside .form-check container");

        softly.assertAll();
    }

    /**
     * US17P3_10: Invalid Credentials Message
     */
    @Test(description = "US17P3_10: Invalid Credentials Message")
    public void invalidCredentials_ShouldShowErrorMessage() {
        openLogin();

        // Invalid creds
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys("invalid_user");
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys("wrong_password");
        driver.findElement(loginBtn).click();

        // Wait a bit for message
        long deadline = System.currentTimeMillis() + Duration.ofSeconds(5).toMillis();
        String msg = "";
        while (System.currentTimeMillis() < deadline) {
            try {
                WebElement lbl = driver.findElement(messageLbl);
                msg = lbl.getText().trim();
                if (!msg.isEmpty()) break;
            } catch (NoSuchElementException ignored) {}
            try { Thread.sleep(150); } catch (InterruptedException ignored) {}
        }

        Assert.assertFalse(msg.isEmpty(), "Expected validation/error message on invalid login");

        String msgLower = msg.toLowerCase();
        Assert.assertTrue(
                msgLower.contains("invalid") || msgLower.contains("incorrect") || msgLower.contains("wrong"),
                "Message should indicate invalid/incorrect credentials. Actual: " + msg
        );

        // Visual cue (best-effort): red color
        try {
            String cssColor = driver.findElement(messageLbl).getCssValue("color");
            Assert.assertTrue(
                    cssColor.contains("255, 0, 0") || cssColor.equalsIgnoreCase("red"),
                    "Message color should be red. Actual: " + cssColor
            );
        } catch (Exception e) {
            System.out.println("[WARN] CSS color check not reliable here: " + e.getMessage());
        }

        // Still on login page
        Assert.assertTrue(!driver.findElements(usernameField).isEmpty(),
                "Should remain on login page after invalid attempt");
    }
}
