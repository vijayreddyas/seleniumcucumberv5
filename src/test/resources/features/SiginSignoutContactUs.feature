Feature: Shopping cart on an e-commerce website

  @Login
  Scenario Outline: Sign im and sign out
    When I open automationpractice website
    Then I sign in with credentials "<username>" and "<password>"
    Then I sign out
    Examples:
      | username   | password |
      | vijayabhaskar12345@gmail.com | Automation@123 |

  @ContactUsLogin
  Scenario Outline: Contact Us Action
    When I open automationpractice website
    Then I perform contact us actions by passing "<email>" and "<textmessage>"
    Examples:
      | email   | textmessage |
      | abc@234.com | Testmessage2 |