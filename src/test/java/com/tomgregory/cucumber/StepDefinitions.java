package com.tomgregory.cucumber;

import com.tomgregory.Account;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {

    private Account account;

    @Given("account balance is {double}")
    public void givenAccountBalance(Double initialBalance) {
        account = new Account(initialBalance);
    }

    @When("the account is credited with {double}")
    public void whenAccountIsCredited(Double amount) {
        account.credit(amount);
    }

    @When("the account is debited with {double}")
    public void whenAccountIsDebited(Double amount) {
        account.debit(amount);
    }

    @Then("account should have a balance of {double}")
    public void thenAccountShouldHaveBalance(Double expectedBalance) {
        assertEquals(expectedBalance, account.getBalance());
    }
}