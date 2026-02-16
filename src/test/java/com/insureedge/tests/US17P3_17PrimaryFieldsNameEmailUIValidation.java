package com.insureedge.tests;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class US17P3_17PrimaryFieldsNameEmailUIValidation extends BaseUiTest{


        WebDriver driver;

        @BeforeClass
        public void setup() throws Exception {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get("https://qeaskillhub.cognizant.com/LoginPage?logout=true");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }

        @AfterClass
        public void tearDown() {
            driver.quit();
        }

        @Test
        public void testEnterNameAndEmailOnCreateAccount() throws Exception {

            WebElement createlink = driver.findElement(By.xpath("//a[normalize-space()='Create an account']"));
            createlink.click();

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            WebElement nameField = driver.findElement(By.id("yourName"));
            nameField.sendKeys("harish");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            WebElement emailField = driver.findElement(By.id("yourEmail"));
            emailField.sendKeys("ahbdajbad123@gmail.com");

            System.out.println("Entered Name and Email successfully on Create Account page.");
        }
    }


