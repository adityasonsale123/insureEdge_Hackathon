package com.insureedge.tests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class US17P306LoginButtonUIValidation extends BaseUiTest {

        WebDriver driver;

        @BeforeClass
        public void setup() {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get("https://qeaskillhub.cognizant.com/LoginPage?logout=true");
        }

        @AfterClass
        public void tearDown() {
            driver.quit();
        }

        @Test
        public void testLoginButtonText() {

            WebElement loginBtn = driver.findElement(By.id("BtnLogin"));

            String buttonText = loginBtn.getAttribute("value");

            if (buttonText.equalsIgnoreCase("Login")) {
                System.out.println("PASS → Button text is correct: " + buttonText);
            } else {
                System.out.println("FAIL → Expected 'Login' but found: " + buttonText);
            }
        }

        @Test
        public void testLoginButtonEnabled() {

            WebElement loginBtn = driver.findElement(By.id("BtnLogin"));

            if (loginBtn.isEnabled()) {
                System.out.println("PASS → Login button is clickable/enabled.");
            } else {
                System.out.println("FAIL → Login button is NOT enabled.");
            }
        }

    }


