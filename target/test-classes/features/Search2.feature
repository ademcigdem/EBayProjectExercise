Feature: User should be able to search item, can sort, filter and see the details of the result products

  Background:
#    Given I am a non-registered customer
#    And I navigate to www.ebay.co.uk


  @002
  Scenario: Search per category
    Given I am a non-registered customer
    And I navigate to www.ebay.co.uk
    When I enter a search term and select a specific Category
    Then I get a list of matching results
    And I can verify that the results shown as per the the selected category

