package com.insureedge.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Minimal base page: only what current pages/tests use.
 */
public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final JavascriptExecutor js;

    protected BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }

    /** Wait until visible and return element. */
    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Clear + type into a visible element. */
    protected void type(By locator, String text) {
        WebElement el = visible(locator);
        el.clear();
        el.sendKeys(text);
    }

    /** Click with a small JS fallback. */
    protected void click(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", driver.findElement(locator));
        }
    }

    /** Safe attribute read. */
    protected String attr(WebElement el, String name) {
        String v = el.getAttribute(name);
        return v == null ? "" : v.trim();
    }

    /** Scroll element into view (center). */
    protected void scrollIntoView(WebElement el) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }
}