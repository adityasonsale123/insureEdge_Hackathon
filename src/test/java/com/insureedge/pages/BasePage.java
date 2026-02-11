package com.insureedge.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final JavascriptExecutor js;

    protected BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void type(By locator, String text) {
        WebElement el = visible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void click(By locator) {
        try {
            clickable(locator).click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", driver.findElement(locator));
        }
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    protected int parseInt(By locator) {
        String t = text(locator);
        if (!t.matches("\\d+")) {
            throw new AssertionError("Expected numeric text for " + locator + " but got: '" + t + "'");
        }
        return Integer.parseInt(t);
    }

    protected void jsFocus(WebElement el) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].focus();", el);
    }
}
