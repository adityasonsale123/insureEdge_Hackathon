package com.insureedge.pages;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ApplicationStatusPage extends BasePage {

    public enum StatusCard {
        APPLIED(
                By.id("ContentPlaceHolder_Admin_lblApplied"),               // COUNT <h6>
                By.xpath("//*[@id='ContentPlaceHolder_Admin_lblApplied']/ancestor::a[1]"),
                "/AppliedList"   // expected target route
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

    public void waitForLoaded() {
        for (StatusCard c : StatusCard.values()) {
            visible(c.countLocator);
        }
    }

    public String getHref(StatusCard card) {
        WebElement link = visible(card.linkLocator);
        String href = link.getAttribute("href");
        return href == null ? "" : href.trim();
    }

    public boolean isClickable(StatusCard card) {
        String href = getHref(card).toLowerCase();
        return !(href.isEmpty() || href.endsWith("#") || href.contains("javascript:void"));
    }

    public void click(StatusCard card) {
        click(card.countLocator); // clicking the count inside the anchor
    }

    public int getCount(StatusCard card) {
        return parseInt(card.countLocator);
    }
}
