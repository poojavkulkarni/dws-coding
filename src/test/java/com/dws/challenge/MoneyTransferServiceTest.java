package com.dws.challenge;


import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.exception.InSufficientAmountException;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.request.MoneyTransferRequest;
import com.dws.challenge.response.MoneyTransferResponse;
import com.dws.challenge.service.MoneyTransferService;
import com.dws.challenge.service.NotificationService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;


@SpringBootTest
public class MoneyTransferServiceTest {

    @Autowired
    private MoneyTransferService moneyTransferService;

    @MockBean
    private NotificationService notificationService;
    @MockBean
    private AccountsRepository accountsRepository;

    @Test
    void testTransferMoneyAccountIdNotPresent() {
        MoneyTransferRequest transferRequest = MoneyTransferRequest.builder()
                .transferAmount(BigDecimal.valueOf(2000))
                .accountFromId("123")
                .accountToId("456").build();

        Assert.assertThrows(AccountNotFoundException.class, () ->
                moneyTransferService.transferMoney(transferRequest));
    }

    @Test
    void testTransferMoneySuccessful() {

        MoneyTransferRequest transferRequest = MoneyTransferRequest.builder()
                .transferAmount(BigDecimal.valueOf(2000))
                .accountFromId("ID-123")
                .accountToId("ID-456").build();

        Account fromAccount = new Account("ID-123");
        fromAccount.setBalance(BigDecimal.valueOf(10000));

        when(accountsRepository.getAccount("ID-123")).thenReturn(fromAccount);

        Account toAccount = new Account("ID-456");
        toAccount.setBalance(BigDecimal.valueOf(4000));
        when(accountsRepository.getAccount("ID-456")).thenReturn(toAccount);

        MoneyTransferResponse moneyTransferResponse = moneyTransferService.transferMoney(transferRequest);
        Assertions.assertEquals(moneyTransferResponse.getStatus(), MoneyTransferResponse.Status.SUCCESS);
        Assertions.assertEquals(accountsRepository.getAccount("ID-123").getBalance(), BigDecimal.valueOf(8000));
        Assertions.assertEquals(accountsRepository.getAccount("ID-456").getBalance(), BigDecimal.valueOf(6000));

    }

    @Test
    void testTransferMoneyNotEnoughBalance() {
        MoneyTransferRequest transferRequest = MoneyTransferRequest.builder()
                .transferAmount(BigDecimal.valueOf(2000))
                .accountFromId("ID-123")
                .accountToId("ID-456").build();

        Account fromAccount = new Account("ID-123");
        fromAccount.setBalance(BigDecimal.valueOf(1000));

        when(accountsRepository.getAccount("ID-123")).thenReturn(fromAccount);

        Account toAccount = new Account("ID-456");
        toAccount.setBalance(BigDecimal.valueOf(4000));
        when(accountsRepository.getAccount("ID-456")).thenReturn(toAccount);
        
        Assert.assertThrows(InSufficientAmountException.class, () ->
                moneyTransferService.transferMoney(transferRequest));
    }

}
