package com.insureedge.tests;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.AdminDashboardPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DashBoardTest extends BaseUiTest {
    private AdminDashboardPage a;

    @BeforeClass(alwaysRun = true)
    @Override
    public  void baseSetup(){
        super.baseSetup();

        loginIfNeeded();

        a = new AdminDashboardPage(driver,wait);

    }

    @Test
    public  void test(){
        System.out.println("admin opened !!");
    }

}
