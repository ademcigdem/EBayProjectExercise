package com.ebay.pages;

import com.ebay.utils.Driver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class HomePage {
    public HomePage() {
        PageFactory.initElements(Driver.get(), this);
    }

    @FindBy(id = "gh-ac")
    public WebElement searchInputBox;

    @FindBy(id = "gh-btn")
    public WebElement searchBtn;

    @FindBy(xpath = "//h3[@class='s-item    s-item--watch-at-corner' or @class='s-item    ']")
    public List<WebElement> listOfMatchingElements;

    @FindBy(css = "h1[class='srp-controls__count-heading'] span")
    public WebElement totalMatchResult;

    @FindBy(css = ".s-item__price")
    public List<WebElement> itemPrice;

    @FindBy(css = ".s-item__buy-it-now-icon")
    public List<WebElement> buyItNowElement;

    @FindBy(css = ".s-item_bids.s-item_bidCount")
    public List<WebElement> bidCount;

    @FindBy(css = ".s-item_shipping.s-item_logisticsCost")
    public List<WebElement> postagePrice;

    @FindBy(xpath = "//a [@class='pagination__next' and @aria-disabled='true']")
    public WebElement lastPageFlagElement;

    @FindBy(css = ".pagination__next")
    public WebElement nextPageButton;

    @FindBy(id = "gh-cat")
    public WebElement categoryDropDownElement;

    @FindBy(xpath = "//option[@selected='selected']")
    public WebElement selectedCategory;

    @FindBy(xpath = "//a//span[.='Lowest price']")
    public WebElement lowestPriceBtn;

    @FindBy(xpath = "//a//span[.='Highest price']")
    public WebElement highestPrice;

    @FindBy(xpath = "(//button[@class='fake-menu-button__button expand-btn expand-btn--small expand-btn--secondary'])[4]")
    public WebElement priceDropDown;

    @FindBy(css = "a[class='pagination__item']")
    public List<WebElement> pageNumberElementList;


    /**
     * This method return specific category select element
     * @return Select
     */
    public Select getCategoryDropDown() {

        return new Select(categoryDropDownElement);
    }


    /**
     * This method return matching items list as a string
     * It'll pass every matching element by one by
     * @return list of string
     */
    public List<String> listOfMatchingItems() {
        List<String> matchingList = new ArrayList<>();
        for (WebElement s : listOfMatchingElements) {
            matchingList.add(s.getText());
        }
        return matchingList;
    }

    /**
     * This method will get all matching result than go through the next page and verify of the result list
     * Provide try catch about NoSuchElementException
     * @param searchItemName user will choose
     */
    public void getMatchingResultMethod(String searchItemName) {
        boolean flag = true;
        List<WebElement> resultElements = new ArrayList<>();
        while (flag) {
            for (WebElement element : listOfMatchingElements) {
                assertTrue(element.getText().toLowerCase().contains(searchItemName));
            }
            resultElements.addAll(listOfMatchingElements);
            try {
                // for next page click
                nextPageButton.click();
            } catch (NoSuchElementException e) {
                // when flag false
                flag = false;
                log.info("NoSuchElementException nextPageButton.click() ");
            }

            try {
                // last element is appear than it will finish
                lastPageFlagElement.isDisplayed();
                flag = false;
            } catch (NoSuchElementException e) {
                log.info("NoSuchElementException nextPageButton.click() ");
            }

        }
    }


    /**
     * This method verify item price, bid count, but it now, shipping type
     * Also provide try catch about the NoSuchElementException
     * It'll check all type of element by one by until last index number.
     */
    public void getVerifyPriceNoBidBuyItNowMethod() {
        boolean flag = true;
        int lastIndexNumber = postagePrice.size();
        while (flag) {

            for (int i = 1; i <= lastIndexNumber; i++) {
                assertTrue(Driver.get().findElement(By.xpath("(//span[@class='s-item__price'])[" + i + "]")).isEnabled());

                try {
                    assertTrue(Driver.get().findElement(By.xpath("(//span[@class='s-item_bids s-item_bidCount'])[" + i + "]")).isEnabled() ||
                            Driver.get().findElement(By.xpath("(//span[@class='s-item__buy-it-now-icon'])[" + i + "]")).isEnabled());
                } catch (NoSuchElementException e) {
                    log.info("NoSuchElementException bid count and buy it now size");
                }

                assertTrue(Driver.get().findElement(By.xpath("(//span[@class='s-item_shipping s-item_logisticsCost'])[" + i + "]")).isEnabled());
            }
            try {
                // It'll check all page
                nextPageButton.click();
            } catch (NoSuchElementException e) {
                flag = false;
                log.info("NoSuchElementException flag = false");
            }

            try {
                // when at the last page it'll finish
                lastPageFlagElement.isDisplayed();
                flag = false;
            } catch (NoSuchElementException e) {
                log.info("NoSuchElementException flag = false");
            }

        }
    }

    /**
     * This method check it the correct order of the price list.
     * When user click the lowest price it'll check by one by all price. It'll work for highest price as well.
     * Provide try catch ArrayIndexOutOfBoundsException
     * @param flag
     */
    public void getListOfCorrectOrder(boolean flag) {
        List<Double> actualPrices = new ArrayList<>();
        for (WebElement element : itemPrice) {
            if (flag) {
                // delete "£" symbol
                // work for highest price
                actualPrices.add(Double.parseDouble((element.getText().replace("£", "").split(" "))[0].trim().toString()));
            } else {
                try {
                    // work for lowest price. Also second price number.(£3.99 to £30.99)
                    actualPrices.add(Double.parseDouble((element.getText().replace("£", "").split(" "))[2].trim().toString()));
                } catch (ArrayIndexOutOfBoundsException e) {
                    actualPrices.add(Double.parseDouble((element.getText().replace("£", "").split(" "))[0].trim().toString()));
                    log.info("ArrayIndexOutOfBoundsException ");
                }
            }
        }
        List<Double> pricesSorted = new ArrayList<>(actualPrices);
        Collections.sort(pricesSorted);
        if (flag) {
            assertEquals(pricesSorted, actualPrices);
        } else {
            Collections.reverse(pricesSorted);
            assertEquals(pricesSorted, actualPrices);
        }
    }

    /**
     * This method for verify but it now text. It'll order but it noe button than check all expected list of verify
     * @param butItNow
     */
    public void getVerifyAllResultShowButItNowTag(String butItNow) {
        for (int i = 1; i <= listOfMatchingElements.size(); i++) {
            assertTrue(Driver.get().findElement(By.xpath("//li[@data-view='mi:1686|iid:" + i + "']//span[@aria-label='" + butItNow + "' or @aria-label='or Best Offer']")).isDisplayed());
        }
    }


    /**
     * This method for searching every page. If result has more than 1 page it takes every page by one by and verify all display results.
     * Provide try catch NoSuchElementException
     * @param searchItemName
     */
    public void getNavigateNextPageAndVerify(String searchItemName) {
        boolean flag = true;
        while (flag) {
            for (WebElement element : listOfMatchingElements) {
                assertTrue(element.getText().toLowerCase().contains(searchItemName));
            }
            try {
                nextPageButton.click();
            } catch (NoSuchElementException e) {
                flag = false;
                log.info("NoSuchElementException nextPageButton.click()");

            }
            try {
                lastPageFlagElement.isDisplayed();
                flag = false;
            } catch (NoSuchElementException e) {
                log.info("NoSuchElementException lastPageFlagElement.isDisplayed()");
            }

        }
    }
}
