package com.insureedge.tests;

import com.insureedge.tests.BaseUiTest;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class InsureEdgeUS17P3_29Test extends BaseUiTest {

    WebDriver driver;
    List<WebElement> cards;

    @BeforeClass
    public void setUp() {
        // Bind to the WebDriver managed by BaseUiTest
        this.driver = super.driver;

        // Perform login using BaseUiTest (reads from config.properties)
        loginIfNeeded();

        String loginUrl = mustGet("loginUrl");
        driver.get(loginUrl);

        driver.manage().window().maximize();

        // Locate all cards on the (post-login) dashboard page
        cards = driver.findElements(By.className("card-body"));
    }

    @Test
    public void validateAllCardUI() {
        int i = 1;
        for (WebElement card : cards) {
            System.out.println("Validating card " + i);

            // Task 1: Heading centered and black
            WebElement heading = card.findElement(By.className("card-title"));
            String headingColor = heading.getCssValue("color");
            String hexHeading = Color.fromString(headingColor).asHex();
            System.out.println("Heading color: " + hexHeading);

            // Task 2: Icon and number visible below heading
            WebElement icon = card.findElement(By.xpath("//div[@class='card-icon rounded-circle d-flex align-items-center justify-content-center']/i"));
            WebElement number = card.findElement(By.xpath("//div[@class='ps-3']/a/h6"));
            if (icon.isDisplayed() && number.isDisplayed()) {
                System.out.println("Icon and number are visible below heading.");
            }

            // Task 3: Number clickable
            try {
                number.click();
                System.out.println("Number is clickable and navigates to new page.");
                driver.navigate().back();
            } catch (Exception e) {
                System.out.println("Number is NOT clickable.");
            }

            // Task 4: Number bold and black
            String fontWeight = number.getCssValue("font-weight");
            String numberColor = number.getCssValue("color");
            String hexNumber = Color.fromString(numberColor).asHex();
            if ((fontWeight.equals("700") || fontWeight.equals("bold")) && hexNumber.equals("#012970")) {
                System.out.println("Number is bold and black.");
            }

            // Task 5: Icon aligned left of number
            int iconX = icon.getRect().getX();
            int numberX = number.getRect().getX();
            if (iconX < numberX) {
                System.out.println("Icon is aligned to the left of the number.");
            }
            i++;
        }
    }

    @AfterClass
    public void closeBrowser() {
        // Let BaseUiTest handle driver.quit() in its @AfterClass
    }
}
