@wechat
Feature: Get Wechat Info
  wechat contacts info

  Scenario Outline: all contacts info
    Given start wechat
    And go to contact menu
    When loop all contacts
    Then save or print contacts info

    Examples:
      | menu    |
      | contact |