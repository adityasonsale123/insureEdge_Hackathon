package com.insureedge.tests;

import com.insureedge.pages.AdminDashboardPage;
import com.insureedge.tests.BaseUiTest; // If BaseUiTest is in com.insureedge.tests.base, change import accordingly.
// If your utils live in com.insureedge.tests.utils, change next two imports to that package:
import com.insureedge.utils.A11yUtils;
import com.insureedge.utils.FocusUtils;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.insureedge.utils.FocusUtils.*; // adjust package if needed

public class AdminDashboardAccessibilityTest extends BaseUiTest {

    private static final By LINK_USERS      = By.xpath("//*[@id='ContentPlaceHolder_Admin_lblRegisteredUsers']/ancestor::a[1]");
    private static final By LINK_POLICIES   = By.xpath("//*[@id='ContentPlaceHolder_Admin_lblListedPolicies']/ancestor::a[1]");
    private static final By LINK_CATEGORIES = By.xpath("//*[@id='ContentPlaceHolder_Admin_lblListedCategories']/ancestor::a[1]");
    private static final By MAYBE_LINK_QUESTIONS = By.xpath("//*[@id='ContentPlaceHolder_Admin_lblTotalQuestions']/ancestor::a[1]");

    private static final By TITLES = By.xpath(
            "//h5[normalize-space()=\"Registered User's\" or normalize-space()='Listed Policies' or normalize-space()='Listed Categories' or normalize-space()='Total Questions']"
    );

    private static final By COUNTS = By.xpath(
            "//*[@id='ContentPlaceHolder_Admin_lblRegisteredUsers' or " +
                    "@id='ContentPlaceHolder_Admin_lblListedPolicies' or " +
                    "@id='ContentPlaceHolder_Admin_lblListedCategories' or " +
                    "@id='ContentPlaceHolder_Admin_lblTotalQuestions']"
    );

    private static final double AA_TEXT = 4.5;
    private static final double UI_OR_LARGE = 3.0;

    @BeforeClass
    @Override
    public void baseSetup() {
        super.baseSetup();
        loginIfNeeded();
        new AdminDashboardPage(driver, wait).waitForLoaded();
    }

    @Test(priority = 1)
    public void interactiveCardsHaveAccessibleNames_nonInteractiveQuestions() {
        assertLinkHasName(LINK_USERS, "Users link");
        assertLinkHasName(LINK_POLICIES, "Policies link");
        assertLinkHasName(LINK_CATEGORIES, "Categories link");
        ensureQuestionsNonInteractive();
    }

    @Test(priority = 2)
    public void keyboardFocusMovesInLogicalOrder() {
        WebElement first  = wait.until(ExpectedConditions.visibilityOfElementLocated(LINK_USERS));
        WebElement second = wait.until(ExpectedConditions.visibilityOfElementLocated(LINK_POLICIES));
        WebElement third  = wait.until(ExpectedConditions.visibilityOfElementLocated(LINK_CATEGORIES));

        focusJS(driver, first);

        // Use Arrays.asList for Java 8 compatibility
        List<WebElement> expected = Arrays.asList(first, second, third);
        List<WebElement> actual   = new ArrayList<>();
        actual.add(activeElement(driver));

        for (int i = 1; i < expected.size(); i++) {
            pressTab(driver);
            actual.add(activeElement(driver));
        }

        Assert.assertEquals(actual.size(), expected.size(), "Focus length mismatch");
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertTrue(expected.get(i).equals(actual.get(i)),
                    "Focus mismatch at pos " + (i + 1) +
                            "\nExpected: " + summarize(expected.get(i)) +
                            "\nActual: " + summarize(actual.get(i)));
        }

        // Ensure questions (if anchor exists) is not tabbable or navigable
        List<WebElement> maybe = driver.findElements(MAYBE_LINK_QUESTIONS);
        if (!maybe.isEmpty()) {
            WebElement q = maybe.get(0);
            String href = attr(q, "href").toLowerCase(Locale.ROOT);
            String tabindex = attr(q, "tabindex");
            Assert.assertTrue(href.isEmpty() || href.endsWith("#") || href.contains("javascript:void"),
                    "Questions must not have a real href");
            Assert.assertNotEquals(tabindex, "0", "Questions must not be in tab order");
        }
    }

    @Test(priority = 3)
    public void keyTextAndIconsMeetContrast() {
        List<WebElement> elements = new ArrayList<>();
        elements.addAll(driver.findElements(TITLES));
        elements.addAll(driver.findElements(COUNTS));
        elements.addAll(driver.findElements(LINK_USERS));
        elements.addAll(driver.findElements(LINK_POLICIES));
        elements.addAll(driver.findElements(LINK_CATEGORIES));
        elements.addAll(driver.findElements(By.cssSelector(".row i.bi")));

        List<WebElement> toCheck = elements.stream()
                .filter(WebElement::isDisplayed)
                .distinct()
                .collect(Collectors.toList());

        List<String> failures = new ArrayList<>();
        for (WebElement el : toCheck) {
            A11yUtils.Color fg = A11yUtils.computed(el, "color");
            A11yUtils.Color bg = A11yUtils.effectiveBackground(driver, el);
            double ratio = A11yUtils.contrastRatio(fg, bg);
            boolean normalText = A11yUtils.isLikelyNormalText(el);
            double threshold = normalText ? AA_TEXT : UI_OR_LARGE;

            if (ratio < threshold) {
                failures.add(String.format(
                        "Low contrast %.2f (< %.1f) for [%s] text='%s' fg=%s bg=%s",
                        ratio, threshold, summarize(el), safeText(el), fg, bg
                ));
            }
        }

        if (!failures.isEmpty()) {
            Assert.fail("Contrast failures:\n" + String.join("\n", failures));
        }
    }

    private void assertLinkHasName(By by, String label) {
        WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        String tabindex = attr(link, "tabindex");
        Assert.assertNotEquals(tabindex, "-1", label + " must not have tabindex=-1");
        String href = attr(link, "href");
        Assert.assertTrue(!href.trim().isEmpty(), label + " must have href");
        String text = safeText(link);
        String ariaLabel = attr(link, "aria-label");
        String ariaLabelledByResolved = resolveAriaLabelledBy(link);
        Assert.assertTrue(!text.isEmpty() || !ariaLabel.isEmpty() || !ariaLabelledByResolved.isEmpty(),
                label + " should have discernible accessible name");
        // Optional: numeric text check when visible name is used
        if (!text.isEmpty()) {
            Assert.assertTrue(text.matches("\\d+"),
                    label + " visible text should be numeric count but got: '" + text + "'");
        }
    }

    private void ensureQuestionsNonInteractive() {
        List<WebElement> anchors = driver.findElements(MAYBE_LINK_QUESTIONS);
        if (anchors.isEmpty()) return;
        WebElement a = anchors.get(0);
        String href = attr(a, "href").toLowerCase(Locale.ROOT);
        String tabindex = attr(a, "tabindex");
        Assert.assertTrue(href.isEmpty() || href.endsWith("#") || href.contains("javascript:void"),
                "Questions must not have navigable href; found: " + href);
        Assert.assertNotEquals(tabindex, "0", "Questions must not be focusable (tabindex != 0)");
    }

    private String resolveAriaLabelledBy(WebElement el) {
        String ids = attr(el, "aria-labelledby");
        if (ids.isEmpty()) return "";
        StringBuilder combined = new StringBuilder();
        for (String id : ids.split("\\s+")) {
            try {
                WebElement ref = driver.findElement(By.id(id));
                String t = safeText(ref);
                if (!t.isEmpty()) {
                    if (combined.length() > 0) combined.append(" ");
                    combined.append(t);
                }
            } catch (NoSuchElementException ignored) {}
        }
        return combined.toString().trim();
    }
}
