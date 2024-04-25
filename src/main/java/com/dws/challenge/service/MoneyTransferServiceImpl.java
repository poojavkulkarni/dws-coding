package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.exception.InSufficientAmountException;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.request.MoneyTransferRequest;
import com.dws.challenge.response.MoneyTransferResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service class to handle money transfer between the accounts
 */
@Service
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final AccountsRepository accountsRepository;
    private final NotificationService notificationService;

    @Autowired
    public MoneyTransferServiceImpl(AccountsRepository accountsRepository,
                                    NotificationService notificationService) {
        this.accountsRepository = accountsRepository;
        this.notificationService = notificationService;
    }


    /**
     * Validate if accounts are present
     * Validate account has sufficient balance to transfer
     * Transfer money from a account to another account
     *
     * @param transferRequest
     * @return
     */
    @Override
    public MoneyTransferResponse transferMoney(MoneyTransferRequest transferRequest) {
        log.info("Inside MoneyTransferServiceImpl.transferMoney to initiate money transfer");

        Optional<Account> fromAccount = validateAccountPresent(transferRequest.getAccountFromId());
        Optional<Account> toAccount = validateAccountPresent(transferRequest.getAccountToId());

        Account moneyFromAccount = deductMoneyFromAccount(fromAccount.get(), transferRequest.getTransferAmount());
        Account moneyToAccount = addMoneyToAccount(toAccount.get(), transferRequest.getTransferAmount());
        accountsRepository.updateAccount(moneyFromAccount);
        accountsRepository.updateAccount(moneyToAccount);

        notificationService.notifyAboutTransfer(moneyFromAccount,
                "Money has been deducted from account");
        notificationService.notifyAboutTransfer(moneyToAccount,
                "Money has been credited to account");

        log.info("Money transfer is successful");
        return getResponseMessage(transferRequest, MoneyTransferResponse.Status.SUCCESS,
                "Amount transfer is successful");
    }

    /**
     * Validate if provided account is present
     *
     * @param accountId
     * @return
     */
    private Optional<Account> validateAccountPresent(String accountId) {
        Optional<Account> fromAccount = Optional.ofNullable(accountsRepository
                .getAccount(accountId));
        if (fromAccount.isEmpty()) {
            throw new AccountNotFoundException("Account id not found "
                    + accountId);
        }
        return fromAccount;
    }

    /**
     * Form response message
     *
     * @param transferRequest
     * @param failure
     * @param message
     * @return
     */
    private static MoneyTransferResponse getResponseMessage(MoneyTransferRequest transferRequest,
                                                            MoneyTransferResponse.Status failure,
                                                            String message) {
        return MoneyTransferResponse.builder()
                .transferAmount(transferRequest.getTransferAmount())
                .accountFromId(transferRequest.getAccountFromId())
                .accountToId(transferRequest.getAccountToId())
                .status(failure)
                .message(message).build();
    }

    /**
     * Deduct balance from the account
     *
     * @param account
     * @param amountToTransfer
     * @return
     */
    private synchronized Account deductMoneyFromAccount(Account account, BigDecimal amountToTransfer) {
        log.info("Deducting money from account {} ", account.getAccountId());
        if (account.getBalance().compareTo(amountToTransfer) < 0) {
            throw new InSufficientAmountException("Account not contain enough balance to initiate transfer");
        }
        account.setBalance(account.getBalance().subtract(amountToTransfer));
        return account;
    }

    /**
     * Credit balance to the account
     *
     * @param account
     * @param amountToTransfer
     * @return
     */
    private synchronized Account addMoneyToAccount(Account account, BigDecimal amountToTransfer) {
        log.info("Crediting money to account {}", account.getAccountId());
        account.setBalance(account.getBalance().add(amountToTransfer));
        return account;
    }
}
