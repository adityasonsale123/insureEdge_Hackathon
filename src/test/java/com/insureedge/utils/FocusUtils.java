package com.insureedge.utils;



import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.Locale;

public final class FocusUtils {
    private FocusUtils() {}

    public static void focusJS(WebDriver driver, WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'}); arguments[0].focus();", el
        );
    }

    public static WebElement activeElement(WebDriver driver) {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return document.activeElement;");
    }

    public static void pressTab(WebDriver driver) {
        new Actions(driver).sendKeys(Keys.TAB).perform();
    }

    public static boolean isDescendantOf(WebDriver driver, WebElement child, WebElement ancestor) {
        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return arguments[1].contains(arguments[0]);", child, ancestor
        );
    }

    public static String summarize(WebElement el) {
        String tag = el.getTagName();
        String id = attr(el, "id");
        String cls = attr(el, "class").replace(" ", ".");
        String text = safeText(el);
        if (!text.isEmpty() && text.length() > 30) text = text.substring(0, 30) + "…";
        String base = tag + (id.isEmpty() ? "" : "#" + id) + (cls.isEmpty() ? "" : "." + cls);
        return base + (text.isEmpty() ? "" : " text~='" + text + "'");
    }

    public static String attr(WebElement el, String name) {
        String v = el.getAttribute(name);
        return v == null ? "" : v.trim();
    }

    public static String safeText(WebElement el) {
        String t = el.getText();
        return t == null ? "" : t.trim();
    }
}