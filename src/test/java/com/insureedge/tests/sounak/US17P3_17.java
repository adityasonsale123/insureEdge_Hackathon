package com.insureedge.tests.sounak;

import com.insureedge.base.BaseUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;                 // <-- Java 8-friendly
import java.nio.file.Path;                  // <-- Still fine to use Path, just not Path.of(...)
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * US17P3_17
 *
 * Task: Navigate to the Register page, type into the password field,
 *       and capture a screenshot of the password field element.
 *
 * Notes:
 * - Java 8 compatible: uses Paths.get(...) instead of Path.of(...)
 * - Saves to ./screenshots/password_field_<timestamp>.png
 */
public class US17P3_17 extends BaseUiTest {

    private static final By PASSWORD_FIELD = By.id("yourPassword");

    private String getRegisterUrl() {
        String url = config.getProperty("register.url", "").trim();
        return url.isEmpty() ? "https://qeaskillhub.cognizant.com/pages_Register" : url;
    }

    @Test(description = "Capture screenshot of the password field on Register page")
    public void capturePasswordFieldScreenshot() throws IOException {
        // Step 1: Open Register page
        String registerUrl = getRegisterUrl();
        driver.get(registerUrl);
        System.out.println("[STEP] Opened Register URL: " + registerUrl);

        // Step 2: Type into password field
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_FIELD));
        passwordField.clear();
        passwordField.sendKeys("testadmin");
        System.out.println("[STEP] Typed into password field.");

        // Step 3: Capture element screenshot to a temp file
        File src = passwordField.getScreenshotAs(OutputType.FILE);

        // Ensure ./screenshots directory exists (Java 8 style)
        Path screenshotsDir = Paths.get(".", "screenshots");
        if (!Files.exists(screenshotsDir)) {
            Files.createDirectories(screenshotsDir);
        }

        // Timestamped filename to avoid overwrite
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path dest = screenshotsDir.resolve("password_field_" + ts + ".png");

        // Copy temp file to destination
        Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("[PASS] Saved password field screenshot to: " + dest.toAbsolutePath());

        // Step 4: Assert file exists and non-empty
        Assert.assertTrue(Files.exists(dest), "Screenshot file was not created.");
        Assert.assertTrue(Files.size(dest) > 0, "Screenshot file is empty.");
    }
}