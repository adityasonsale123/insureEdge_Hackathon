package com.insureedge.tests.aditya;

import com.insureedge.base.BaseUiTest;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class AccessibilityCreateAccountFormTest_US17P3_20 extends BaseUiTest {

    @BeforeClass(alwaysRun = true)
    public void openRegisterPage() {
        // Get URL from config.properties (no hardcoding)
        String registerUrl = config.getProperty("register.url", "").trim();
        if (registerUrl.isEmpty()) {
            throw new IllegalStateException("Missing 'register.url' in config.properties");
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(registerUrl);
    }

    @Test
    public void verifyFocusOrder() {
        // Expected elements in the tab (focus) order
        WebElement[] order = {
                driver.findElement(By.id("yourName")),
                driver.findElement(By.id("yourEmail")),
                driver.findElement(By.id("yourUsername")),
                driver.findElement(By.id("yourPassword")),
                driver.findElement(By.id("acceptTerms")),
                driver.findElement(By.cssSelector(".form-check a")),
                driver.findElement(By.cssSelector("button.btn.btn-primary")),
                driver.findElement(By.cssSelector("a[href='pages-login.html']"))
        };

        // Put focus on the first field
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus()", order[0]);

        Actions actions = new Actions(driver);

        // Step through with TAB and assert focus at each step
        for (int i = 0; i < order.length; i++) {
            WebElement active = (WebElement)
                    ((JavascriptExecutor) driver).executeScript("return document.activeElement");

            Assert.assertEquals(active, order[i], "Wrong focus at index: " + i);

            if (i < order.length - 1) {
                actions.sendKeys(Keys.TAB).perform();
            }
        }
    }
}