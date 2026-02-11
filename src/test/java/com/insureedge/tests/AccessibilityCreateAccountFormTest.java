package com.insureedge.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class AccessibilityCreateAccountFormTest {

    private WebDriver driver;
    private ResourceBundle prop;

    // If you want URL from properties, set key createAccountUrl in config.properties
    private String TEST_URL;

    private static final double MIN_CONTRAST_RATIO = 4.5;

    @BeforeClass
    public void setup() {
        prop = ResourceBundle.getBundle("config");
        TEST_URL = getPropOrDefault("createAccountUrl", "https://qeaskillhub.cognizant.com/pages_Register");

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get(TEST_URL);
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        if (driver != null) driver.quit();
    }

    // -------------------------
    // Task 3A: Labels
    // -------------------------
    @Test
    public void shouldHaveCorrectLabelsAndAccessibleNames() {
        By[] controls = new By[]{
                By.id("yourName"),
                By.id("yourEmail"),
                By.id("yourUsername"),
                By.id("yourPassword"),
                By.id("acceptTerms")
        };

        for (By locator : controls) {
            WebElement control = driver.findElement(locator);
            String id = control.getAttribute("id");
            Assert.assertNotNull(id, "Control is missing id attribute: " + locator);

            // label[for=id]
            List<WebElement> labels = driver.findElements(By.cssSelector("label[for='" + cssEscape(id) + "']"));
            boolean hasProperLabelFor = labels.stream().anyMatch(l -> !l.getText().trim().isEmpty());

            // aria-label
            String ariaLabel = safeAttr(control, "aria-label").trim();
            boolean hasAriaLabel = !ariaLabel.isEmpty();

            // aria-labelledby
            String ariaLabelledBy = safeAttr(control, "aria-labelledby").trim();
            boolean hasAriaLabelledBy = false;
            if (!ariaLabelledBy.isEmpty()) {
                String combined = Arrays.stream(ariaLabelledBy.split("\\s+"))
                        .map(idref -> {
                            try {
                                WebElement el = driver.findElement(By.id(idref));
                                return el.getText().trim();
                            } catch (NoSuchElementException e) {
                                return "";
                            }
                        })
                        .collect(Collectors.joining(" ")).trim();
                hasAriaLabelledBy = !combined.isEmpty();
            }

            Assert.assertTrue(
                    hasProperLabelFor || hasAriaLabel || hasAriaLabelledBy,
                    "Accessible name missing for control id='" + id + "'. Expected <label for='"
                            + id + "'> or aria-label/aria-labelledby."
            );
        }
    }

    // -------------------------
    // Task 3B: Focus Order (FORM-ONLY)
    // -------------------------
    @Test
    public void shouldHaveLogicalKeyboardFocusOrder() {

        WebElement form = driver.findElement(By.cssSelector("form.needs-validation"));

        // Expected tab sequence INSIDE YOUR FORM (Java 8 compatible)
        List<FocusTarget> expected = Arrays.asList(
                FocusTarget.byId("yourName"),
                FocusTarget.byId("yourEmail"),
                FocusTarget.byId("yourUsername"),
                FocusTarget.byId("yourPassword"),
                FocusTarget.byId("acceptTerms"),
                FocusTarget.byCss("a", "terms and conditions"),
                FocusTarget.byCss("button[type='submit']", "Create Account"),
                FocusTarget.byCss("a[href$='pages-login.html']", "Log in")
        );

        // Sanity: required fields must be keyboard-focusable
        assertKeyboardReachable(By.id("yourName"), "yourName");
        assertKeyboardReachable(By.id("yourEmail"), "yourEmail");
        assertKeyboardReachable(By.id("yourUsername"), "yourUsername");
        assertKeyboardReachable(By.id("yourPassword"), "yourPassword");
        assertKeyboardReachable(By.id("acceptTerms"), "acceptTerms");

        // Force starting focus at first field
        WebElement start = driver.findElement(By.id("yourName"));
        jsFocus(start);

        Actions actions = new Actions(driver);

        List<FocusTarget> actual = new ArrayList<>();
        actual.add(FocusTarget.from(getActiveElement())); // first focused

        for (int i = 1; i < expected.size(); i++) {
            actions.sendKeys(Keys.TAB).perform();
            WebElement active = getActiveElement();

            // stop if focus leaves form
            if (!isDescendantOf(active, form)) break;

            actual.add(FocusTarget.from(active));
        }

        System.out.println("ACTUAL (within form): " + actual);

        Assert.assertEquals(
                actual.size(),
                expected.size(),
                "Focus order inside the form is incomplete or leaves the form early.\n" +
                        "Expected count: " + expected.size() + "\n" +
                        "Actual count:   " + actual.size() + "\n" +
                        "Actual list:    " + actual
        );

        for (int i = 0; i < expected.size(); i++) {
            FocusTarget exp = expected.get(i);
            FocusTarget act = actual.get(i);

            boolean match = exp.matches(act, driver);

            Assert.assertTrue(
                    match,
                    "\nFocus order mismatch at position " + (i + 1) +
                            "\nExpected: " + exp +
                            "\nActual:   " + act +
                            "\nAll actual (within form): " + actual
            );
        }
    }

    private void assertKeyboardReachable(By locator, String name) {
        WebElement el = driver.findElement(locator);
        Assert.assertTrue(el.isDisplayed(), name + " is not displayed.");
        Assert.assertTrue(el.isEnabled(), name + " is disabled.");

        String tabindex = safeAttr(el, "tabindex").trim();
        Assert.assertFalse("-1".equals(tabindex), name + " has tabindex=-1.");
    }

    private void jsFocus(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'}); arguments[0].focus();", el
        );
    }

    private boolean isDescendantOf(WebElement child, WebElement ancestor) {
        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return arguments[1].contains(arguments[0]);", child, ancestor
        );
    }

    // -------------------------
    // Task 3C: Contrast
    // -------------------------
    @Test
    public void shouldMeetMinimumContrastForKeyText() {
        List<WebElement> elementsToCheck = new ArrayList<>();
        elementsToCheck.addAll(driver.findElements(By.cssSelector("form label.form-label")));
        elementsToCheck.addAll(driver.findElements(By.cssSelector("button.btn.btn-primary")));
        elementsToCheck.addAll(driver.findElements(By.cssSelector("form a")));

        elementsToCheck = elementsToCheck.stream()
                .filter(WebElement::isDisplayed)
                .filter(e -> !e.getText().trim().isEmpty())
                .collect(Collectors.toList());

        Assert.assertTrue(elementsToCheck.size() > 0, "No elements found to contrast-check.");

        List<String> failures = new ArrayList<>();

        for (WebElement el : elementsToCheck) {
            Color fg = getComputedColor(el, "color");
            Color bg = getEffectiveBackgroundColor(el);

            double ratio = contrastRatio(fg, bg);

            if (ratio < MIN_CONTRAST_RATIO) {
                failures.add(String.format(
                        "Low contrast %.2f (< %.1f) for [%s] text='%s' fg=%s bg=%s",
                        ratio, MIN_CONTRAST_RATIO,
                        cssSummary(el), el.getText().trim(),
                        fg, bg
                ));
            }
        }

        if (!failures.isEmpty()) {
            Assert.fail("Contrast failures:\n" + String.join("\n", failures));
        }
    }

    private WebElement getActiveElement() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return document.activeElement;");
    }

    // -------------------------
    // Color helpers
    // -------------------------
    private static class Color {
        final int r, g, b;

        Color(int r, int g, int b) {
            this.r = clamp(r);
            this.g = clamp(g);
            this.b = clamp(b);
        }

        static int clamp(int v) { return Math.max(0, Math.min(255, v)); }

        @Override
        public String toString() {
            return "rgb(" + r + "," + g + "," + b + ")";
        }
    }

    private Color getComputedColor(WebElement el, String cssProp) {
        String val = el.getCssValue(cssProp);
        return parseCssColor(val);
    }

    private Color getEffectiveBackgroundColor(WebElement el) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        String script =
                "function bg(el) {" +
                        "  while (el) {" +
                        "    var s = window.getComputedStyle(el);" +
                        "    var c = s.backgroundColor;" +
                        "    if (c && c !== 'rgba(0, 0, 0, 0)' && c !== 'transparent') return c;" +
                        "    el = el.parentElement;" +
                        "  }" +
                        "  return 'rgb(255, 255, 255)';" +
                        "}" +
                        "return bg(arguments[0]);";

        String bg = String.valueOf(js.executeScript(script, el));
        return parseCssColor(bg);
    }

    private Color parseCssColor(String cssColor) {
        cssColor = cssColor.trim().toLowerCase(Locale.ROOT);

        if (cssColor.startsWith("rgb")) {
            String inside = cssColor.substring(cssColor.indexOf("(") + 1, cssColor.indexOf(")"));
            String[] parts = inside.split(",");
            int r = Integer.parseInt(parts[0].trim());
            int g = Integer.parseInt(parts[1].trim());
            int b = Integer.parseInt(parts[2].trim());
            return new Color(r, g, b);
        }

        if (cssColor.startsWith("#")) {
            String hex = cssColor.substring(1);
            if (hex.length() == 3) {
                hex = "" + hex.charAt(0) + hex.charAt(0)
                        + hex.charAt(1) + hex.charAt(1)
                        + hex.charAt(2) + hex.charAt(2);
            }
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return new Color(r, g, b);
        }

        return new Color(0, 0, 0);
    }

    private double contrastRatio(Color c1, Color c2) {
        double L1 = relativeLuminance(c1);
        double L2 = relativeLuminance(c2);
        double lighter = Math.max(L1, L2);
        double darker = Math.min(L1, L2);
        return (lighter + 0.05) / (darker + 0.05);
    }

    private double relativeLuminance(Color c) {
        double rs = srgbToLinear(c.r / 255.0);
        double gs = srgbToLinear(c.g / 255.0);
        double bs = srgbToLinear(c.b / 255.0);
        return 0.2126 * rs + 0.7152 * gs + 0.0722 * bs;
    }

    private double srgbToLinear(double v) {
        return (v <= 0.03928) ? (v / 12.92) : Math.pow((v + 0.055) / 1.055, 2.4);
    }

    // -------------------------
    // FocusTarget
    // -------------------------
    private static class FocusTarget {
        final String id;
        final String css;
        final String containsText;
        final WebElement element;

        private FocusTarget(String id, String css, String containsText, WebElement element) {
            this.id = id;
            this.css = css;
            this.containsText = containsText;
            this.element = element;
        }

        static FocusTarget byId(String id) {
            return new FocusTarget(id, null, null, null);
        }

        static FocusTarget byCss(String css, String containsText) {
            return new FocusTarget(null, css, containsText, null);
        }

        static FocusTarget from(WebElement el) {
            String id = safeAttrStatic(el, "id");
            String text = el.getText() != null ? el.getText().trim() : "";
            if (id != null && !id.isEmpty()) {
                return new FocusTarget(id, null, text.isEmpty() ? null : text, el);
            }
            return new FocusTarget(null, el.getTagName(), text.isEmpty() ? null : text, el);
        }

        boolean matches(FocusTarget actual, WebDriver driver) {
            WebElement focusedEl = actual.element;
            if (focusedEl == null) return false;

            if (this.id != null) {
                return this.id.equals(focusedEl.getAttribute("id"));
            }

            if (this.css != null) {
                Boolean cssMatch = (Boolean) ((JavascriptExecutor) driver).executeScript(
                        "return arguments[0].matches(arguments[1]);",
                        focusedEl, this.css
                );
                if (cssMatch == null || !cssMatch) return false;

                if (this.containsText != null && !this.containsText.trim().isEmpty()) {
                    String t = focusedEl.getText() == null ? "" : focusedEl.getText().trim().toLowerCase(Locale.ROOT);
                    return t.contains(this.containsText.toLowerCase(Locale.ROOT));
                }
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            if (id != null) return "FocusTarget{id='" + id + "'}";
            return "FocusTarget{css='" + css + "', text~='" + containsText + "'}";
        }
    }

    // -------------------------
    // Misc helpers
    // -------------------------
    private static String safeAttr(WebElement el, String attr) {
        String v = el.getAttribute(attr);
        return v == null ? "" : v;
    }

    private static String safeAttrStatic(WebElement el, String attr) {
        String v = el.getAttribute(attr);
        return v == null ? "" : v;
    }

    private static String cssSummary(WebElement el) {
        String tag = el.getTagName();
        String id = safeAttrStatic(el, "id");
        String cls = safeAttrStatic(el, "class");
        StringBuilder sb = new StringBuilder(tag);
        if (!id.isEmpty()) sb.append("#").append(id);
        if (!cls.isEmpty()) sb.append(".").append(cls.trim().replace(" ", "."));
        return sb.toString();
    }

    private static String cssEscape(String s) {
        return s.replace("'", "\\'");
    }

    private String getPropOrDefault(String key, String defaultVal) {
        try {
            String v = prop.getString(key);
            return v == null || v.trim().isEmpty() ? defaultVal : v.trim();
        } catch (Exception e) {
            return defaultVal;
        }
    }
}