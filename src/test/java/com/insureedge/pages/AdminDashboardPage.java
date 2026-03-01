package com.insureedge.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class AdminDashboardPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public AdminDashboardPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // All dashboard counters
    public enum Tile {
        REGISTERED_USERS(By.id("ContentPlaceHolder_Admin_lblRegisteredUsers"), "AdminViewCustomer"),
        LISTED_POLICIES(By.id("ContentPlaceHolder_Admin_lblListedPolicies"), "AdminViewPolicy"),
        LISTED_CATEGORIES(By.id("ContentPlaceHolder_Admin_lblListedCategories"), "AdminCreateMainCategory"),
        TOTAL_QUESTIONS(By.id("ContentPlaceHolder_Admin_lblTotalQuestions"), null);

        public final By locator;
        public final String expectedRoute;

        Tile(By locator, String route) {
            this.locator = locator;
            this.expectedRoute = route;
        }
    }

    // Wait for tiles to show
    public void waitForLoaded() {
        for (Tile t : Tile.values()) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(t.locator));
        }
    }

    // Read count number
    public int getCount(Tile t) {
        String text = driver.findElement(t.locator).getText().trim();
        return Integer.parseInt(text);
    }

    // Check if tile is clickable
    public boolean isClickable(Tile t) {
        if (t.expectedRoute == null) return false;
        WebElement a = driver.findElement(t.locator).findElement(By.xpath("./ancestor::a[1]"));
        String href = a.getAttribute("href");
        return href != null && !href.endsWith("#");
    }

    // Click tile
    public void click(Tile t) {
        driver.findElement(t.locator).click();
    }
}