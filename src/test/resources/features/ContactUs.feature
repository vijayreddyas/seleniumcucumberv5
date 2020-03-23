Feature: Shopping cart on an e-commerce website

  @ContactUs
  Scenario Outline: Contact Us Action
    When I open automationpractice website
    Then I perform contact us actions by passing "<email>" and "<textmessage>"
    Examples:
      | email   | textmessage |
      | abc@234.com | Testmessage2 |