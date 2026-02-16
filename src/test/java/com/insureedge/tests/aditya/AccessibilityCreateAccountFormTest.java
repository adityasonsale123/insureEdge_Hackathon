package com.insureedge.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class AccessibilityCreateAccountFormTest {

    private WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        // Use your actual page URL here
        driver.get("https://qeaskillhub.cognizant.com/pages_Register");
        System.out.println("[STEP] Opened Create Account page");
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        if (driver != null) driver.quit();
        System.out.println("[PASS] Browser closed");
    }

    /**
     * Minimal focus order test that matches the provided DOM.
     * Expected order inside the form:
     * 1) yourName
     * 2) yourEmail
     * 3) yourUsername
     * 4) yourPassword
     * 5) acceptTerms
     * 6) terms and conditions link  (inside the checkbox label) -> .form-check a
     * 7) Create Account button      -> button.btn.btn-primary
     * 8) Log in link                -> a[href='pages-login.html']
     */
    @Test
    public void focusOrderInsideForm_shouldMatchDom() {
        WebElement form = driver.findElement(By.cssSelector("form.needs-validation"));

        WebElement name        = form.findElement(By.id("yourName"));
        WebElement email       = form.findElement(By.id("yourEmail"));
        WebElement username    = form.findElement(By.id("yourUsername"));
        WebElement password    = form.findElement(By.id("yourPassword"));
        WebElement acceptTerms = form.findElement(By.id("acceptTerms"));
        WebElement termsLink   = form.findElement(By.cssSelector(".form-check a")); // href="#"
        WebElement submitBtn   = form.findElement(By.cssSelector("button.btn.btn-primary"));
        WebElement loginLink   = form.findElement(By.cssSelector("a[href='pages-login.html']"));

        WebElement[] expected = new WebElement[] {
                name, email, username, password, acceptTerms, termsLink, submitBtn, loginLink
        };

        ((JavascriptExecutor) driver).executeScript("arguments[0].focus()", name);
        Actions actions = new Actions(driver);

        for (int i = 0; i < expected.length; i++) {
            WebElement active = (WebElement) ((JavascriptExecutor) driver)
                    .executeScript("return document.activeElement");
            Assert.assertEquals(active, expected[i],
                    "Focus mismatch at index " + i + " -> expected " + summary(expected[i]) +
                            ", got " + summary(active));
            System.out.println("[PASS] Focus OK -> " + summary(active));

            // only tab if there is a next expected element
            if (i < expected.length - 1) {
                actions.sendKeys(Keys.TAB).perform();
            }
        }
    }

    private String summary(WebElement el) {
        String tag = el.getTagName();
        String id  = el.getAttribute("id");
        if (id != null && !id.isEmpty()) return tag + "#" + id;
        String name = el.getAttribute("name");
        if (name != null && !name.isEmpty()) return tag + "[name='" + name + "']";
        String href = el.getAttribute("href");
        if (href != null && !href.isEmpty()) return tag + "[href='" + href + "']";
        String type = el.getAttribute("type");
        if (type != null && !type.isEmpty()) return tag + "[type='" + type + "']";
        return tag;
    }

}
