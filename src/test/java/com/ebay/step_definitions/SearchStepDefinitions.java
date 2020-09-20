package com.ebay.step_definitions;

import com.ebay.utils.ConfigurationReader;
import com.ebay.utils.Driver;
import com.ebay.utils.Pages;
import io.cucumber.java.en.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;



import static org.junit.Assert.*;

public class SearchStepDefinitions {
    private static Logger logger = Logger.getLogger(String.valueOf(SearchStepDefinitions.class));
    Actions actions = new Actions(Driver.get());

    Pages pages = new Pages(); // Pages class related with the all creating object.Just define in the pages all classes and it'll provide easy to access
    String searchItemName = "sebamed";
    String categoryName = "Baby";
    boolean ascOrderFlag;
    int totalResult;

    @Given("I am a non-registered customer")
    public void i_am_a_non_registered_customer() {
        logger.info("I am a non-registered customer");
    }

    @Given("I navigate to www.ebay.co.uk")
    public void i_navigate_to_www_ebay_co_uk() {
        Driver.get().get(ConfigurationReader.get("url"));
        //Verify Title on the page
        assertEquals("Electronics, Cars, Fashion, Collectibles & More | eBay", Driver.get().getTitle());
    }

    @When("I search for an item")
    public void i_search_for_an_item() {
        pages.homePage().searchInputBox.sendKeys(searchItemName);  //Keys.ENTER option
        pages.homePage().searchBtn.click();
        logger.debug(searchItemName);
    }

    @Then("I get a list of matching results")
    public void i_get_a_list_of_matching_results() {
        totalResult = Integer.parseInt(pages.homePage().totalMatchResult.getText().replace(",", ""));
        pages.homePage().getMatchingResultMethod(searchItemName);
        logger.debug(totalResult);
    }

    @Then("the resulting items cards show: postage price, No of bids, price or show BuyItNow tag")
    public void the_resulting_items_cards_show_postage_price_no_of_bids_price_or_show_buy_it_now_tag() {
        pages.homePage().getVerifyPriceNoBidBuyItNowMethod();
    }

    @Then("I can sort the results by Lowest Price")
    public void i_can_sort_the_results_by_lowest_price() {
        ascOrderFlag = true;
        actions.moveToElement(pages.homePage().priceDropDown).click().moveToElement(pages.homePage().lowestPriceBtn).click().perform();
        logger.debug(ascOrderFlag);
    }

    @Then("the results are listed in the page in the correct order")
    public void the_results_are_listed_in_the_page_in_the_correct_order() {
        pages.homePage().getListOfCorrectOrder(ascOrderFlag);
    }

    @Then("I can sort the results by Highest Price")
    public void i_can_sort_the_results_by_highest_price() {
        ascOrderFlag = false;
        actions.click(pages.homePage().priceDropDown).moveToElement(pages.homePage().highestPrice).click().perform();
        logger.debug(ascOrderFlag);
    }

    @Then("I can filter the results by {string}")
    public void i_can_filter_the_results_by(String buyItNow) {
        Driver.get().findElement(By.xpath("//h2[.='" + buyItNow + "']")).click();
        logger.debug(buyItNow);
    }

    @Then("all the results shown in the page have the {string} tag")
    public void all_the_results_shown_in_the_page_have_the_tag(String buyItNow) {
        pages.homePage().getVerifyAllResultShowButItNowTag(buyItNow);
        logger.info(buyItNow);
    }

    @When("I enter a search term and select a specific Category")
    public void i_enter_a_search_term_and_select_a_specific_category() {
        pages.homePage().getCategoryDropDown().selectByVisibleText(categoryName);
        pages.homePage().searchInputBox.sendKeys(searchItemName, Keys.ENTER);
        logger.debug(categoryName);

    }

    @Then("I can verify that the results shown as per the the selected category")
    public void i_can_verify_that_the_results_shown_as_per_the_the_selected_category() {
        for (WebElement we : pages.homePage().listOfMatchingElements) {
            assertTrue(we.getText().toLowerCase().contains(searchItemName));
        }
        assertEquals(categoryName, pages.homePage().selectedCategory.getText().trim());
    }

    @Then("the results show more than one page")
    public void the_results_show_more_than_one_page() {
        assertTrue(pages.homePage().pageNumberElementList.size() > 1);
    }

    @Then("the user can navigate through the pages to continue looking at the items")
    public void the_user_can_navigate_through_the_pages_to_continue_looking_at_the_items() {
        pages.homePage().getNavigateNextPageAndVerify(searchItemName);
    }


}
