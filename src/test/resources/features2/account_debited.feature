Feature: Account is debited with amount

  Scenario: Debit amount
    Given account balance is 10.0
    When the account is debited with 2.0
    Then account should have a balance of 8.0