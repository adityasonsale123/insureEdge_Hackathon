package com.insureedge.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

    public class FirstStory extends BaseUiTest {

        @Test
        public void verifyTitle() {
            driver.get(getOrDefault("loginUrl", "https://qeaskillhub.cognizant.com/LoginPage?logout=true"));

            String expectedTitle = "InsurEdge";
            WebElement titleElement = driver.findElement(By.xpath("//span[normalize-space()='InsurEdge']"));
            String actualTitle = titleElement.getText();

            System.out.println(actualTitle.equals(expectedTitle)
                    ? "Title Validation Passed! Found: " + actualTitle
                    : "Title Validation Failed. Expected " + expectedTitle + " but found " + actualTitle);

            Assert.assertEquals(actualTitle, expectedTitle, "Page title text mismatch");
        }

        @Test
        public void verifyLogoVisibility() {
            driver.get(getOrDefault("loginUrl", "https://qeaskillhub.cognizant.com/LoginPage?logout=true"));

            WebElement logo = driver.findElement(By.xpath("//span[text()='InsurEdge']"));
            System.out.println(logo.isDisplayed()
                    ? "Logo is visible on the page."
                    : "Logo is not visible");

            Assert.assertTrue(logo.isDisplayed(), "Logo should be visible on the page");
        }
    }


