package com.ebay.utils;


import com.ebay.pages.HomePage;

public class Pages {

    private HomePage homePage;

    public HomePage homePage() {
        if (homePage == null) {
            homePage = new HomePage();
        }
        return homePage;
    }
}
