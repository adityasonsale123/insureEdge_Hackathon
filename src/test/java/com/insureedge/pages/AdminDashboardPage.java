package com.insureedge.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminDashboardPage extends BasePage {

    public AdminDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public enum Card {
        REGISTERED_USERS(
                By.id("ContentPlaceHolder_Admin_lblRegisteredUsers"),
                "/AdminViewCustomer"
        ),
        LISTED_POLICIES(
                By.id("ContentPlaceHolder_Admin_lblListedPolicies"),
                "/AdminViewPolicy"
        ),
        LISTED_CATEGORIES(
                By.id("ContentPlaceHolder_Admin_lblListedCategories"),
                "/AdminCreateMainCategory"
        ),
        TOTAL_QUESTIONS(
                By.id("ContentPlaceHolder_Admin_lblTotalQuestions"),
                null // non-clickable
        );

        public final By countLocator;
        public final String expectedRouteIfClickable;

        Card(By countLocator, String expectedRouteIfClickable) {
            this.countLocator = countLocator;
            this.expectedRouteIfClickable = expectedRouteIfClickable;
        }
    }

    public void waitForLoaded() {
        visible(Card.REGISTERED_USERS.countLocator);
        visible(Card.LISTED_POLICIES.countLocator);
        visible(Card.LISTED_CATEGORIES.countLocator);
        visible(Card.TOTAL_QUESTIONS.countLocator);
    }

    public int getCount(Card card) {
        return parseInt(card.countLocator);
    }

    public boolean isClickable(Card card) {
        if (card.expectedRouteIfClickable == null) return false;
        try {
            WebElement h6 = visible(card.countLocator);
            WebElement anchor = h6.findElement(By.xpath("./ancestor::a[1]"));
            String href = anchor.getAttribute("href");
            if (href == null) return false;
            href = href.trim().toLowerCase();
            return !(href.isEmpty() || href.endsWith("#") || href.contains("javascript:void"));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickCard(Card card) {
        click(card.countLocator);
    }
}
