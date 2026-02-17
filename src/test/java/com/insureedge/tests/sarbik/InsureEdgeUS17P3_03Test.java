package com.insureedge.tests.sarbik;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

public class InsureEdgeUS17P3_03Test extends BaseUiTest {

    // Reuse BaseUiTest's driver; do NOT redeclare WebDriver driver here
    WebElement usernameField;
    WebElement label;
    WebElement prefix;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        // Initialize driver and wait via BaseUiTest
        baseSetup();

        // Use config if present; otherwise default to the known login URL
        String loginUrl = config.getProperty("login.url", "").trim();


        // Open login page using your POM (no added waits)
        new LoginPage(driver, wait).open(loginUrl);

        driver.manage().window().maximize();

        // Your original element lookups (unchanged)
        usernameField = driver.findElement(By.xpath("//input[@id=\"txtUsername\"]"));
        label = driver.findElement(By.xpath("//label[text()='Username']"));
        prefix = driver.findElement(By.xpath("//span[@class='input-group-text']"));
    }

    // Task 1
    @Test
    public void verifyUsernameFieldVisible() {
        if (usernameField.isDisplayed()) {
            System.out.println("Username field is visible below header.");
        }
    }

    // Task 2
    @Test
    public void verifyLabelTextAndColor() {
        String color = label.getCssValue("color");
        System.out.println("Label text: " + label.getText() + " and Color: " + color);
    }

    // Task 5
    @Test
    public void verifyPrefixAndUsernameBackgroundColors() {
        String prefixColor = prefix.getCssValue("background-color");
        String usernameColor = usernameField.getCssValue("background-color");
        System.out.println("Color of @ prefix: " + prefixColor + " and Color of text area: " + usernameColor);
    }

    // Task 3 - clickable check
    @Test
    public void verifyPrefixNonClickable() {
        System.out.println("Prefix displayed: " +prefix.isDisplayed() + ", enabled: " +prefix.isEnabled());
        if (!prefix.getTagName().equalsIgnoreCase("button")) {
            System.out.println("The @ prefix is not clickable UI element");
        }
    }

    // Task 4 - typing on prefix
    @Test
    public void verifyCannotTypeOnPrefix() {
        try {
            prefix.sendKeys("Writing on prefix @");
            System.out.println("Successfully written");
        } catch (Exception e) {
            System.out.println("Cannot write anything on the prefix @");
        }
    }

    // Passing a text to check if the text is visible
    @Test
    public void verifyUsernameInput() {
        usernameField.clear();
        usernameField.sendKeys("admin_user");
        String enteredText = usernameField.getAttribute("value");
        if (enteredText.equals("admin_user")) {
            System.out.println("Text is correctly showing in the Username field.");
        } else {
            System.out.println("Text is not showing in the Username field.");
        }

        //Assert.assertEquals(enteredText, "admin_user"); We can use Assert also instead of if-else
    }

    @AfterClass(alwaysRun = true)
    public void closeBrowser() {
        baseTeardown();
    }
}