package com.insureedge.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ApplicationStatusPage extends BasePage {

    public enum StatusCard {
        APPLIED(
                By.id("ContentPlaceHolder_Admin_lblApplied"),               // <h6> count element
                By.xpath("//*[@id='ContentPlaceHolder_Admin_lblApplied']/ancestor::a[1]"),
                "/AppliedList"
        ),
        APPROVED(
                By.id("ContentPlaceHolder_Admin_lblApproved"),
                By.xpath("//*[@id='ContentPlaceHolder_Admin_lblApproved']/ancestor::a[1]"),
                "/ApprovedList"
        ),
        PENDING(
                By.id("ContentPlaceHolder_Admin_lblPending"),
                By.xpath("//*[@id='ContentPlaceHolder_Admin_lblPending']/ancestor::a[1]"),
                "/PendingList"
        ),
        REJECTED(
                By.id("ContentPlaceHolder_Admin_lblRejected"),
                By.xpath("//*[@id='ContentPlaceHolder_Admin_lblRejected']/ancestor::a[1]"),
                "/RejectedList"
        );

        public final By countLocator;
        public final By linkLocator;
        public final String expectedRoute;

        StatusCard(By countLocator, By linkLocator, String expectedRoute) {
            this.countLocator = countLocator;
            this.linkLocator = linkLocator;
            this.expectedRoute = expectedRoute;
        }
    }

    public ApplicationStatusPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /** Wait until all 4 cards' count elements are visible. */
    public void waitForLoaded() {
        for (StatusCard c : StatusCard.values()) {
            visible(c.countLocator);
        }
        System.out.println("[PASS] Application Status cards visible.");
    }

    /** Returns the resolved href of the card's anchor (empty string if absent). */
   public String getHref(StatusCard card) {
        WebElement link = visible(card.linkLocator);
        String href = attr(link, "href");
        return href == null ? "" : href.trim();
    }

    /** True if the card points to a real, navigable URL (not # or js:void). */
   /* public boolean isClickable(StatusCard card) {
        String href = getHref(card).toLowerCase();
        return !(href.isEmpty() || href.endsWith("#") || href.contains("javascript:void"));
    }

    /** Clicks the card by clicking on its count element (which sits inside the anchor). */
    public void click(StatusCard card) {
        click(card.countLocator);
    }

    /** Parses the visible numeric text of a card's count. Throws if non-numeric. */
   public int getCount(StatusCard card) {
        String t = visible(card.countLocator).getText().trim();
        if (!t.matches("\\d+")) {
            throw new AssertionError(card.name() + " count is not numeric: '" + t + "'");
        }
        return Integer.parseInt(t);
    }
}
