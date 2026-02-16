package com.insureedge.tests;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTest extends BaseUiTest {

    private LoginPage login;

    @BeforeClass(alwaysRun = true)
    @Override
    public void baseSetup() {
        super.baseSetup();
        login = new LoginPage(driver, wait);
    }

    @Test
    public void loginFun() {
        // read from config.properties
        String url  = config.getProperty("login.url", "").trim();
        String user = config.getProperty("login.username", "").trim();
        String pass = config.getProperty("login.password", "").trim();


        System.out.println("[STEP] Opening login page...");
        login.open(url);

        // optional: confirm we're on login
        login.login("admin123", "addfsdfd");


    }
}