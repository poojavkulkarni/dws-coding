package com.dws.challenge.web;


import com.dws.challenge.request.MoneyTransferRequest;
import com.dws.challenge.response.MoneyTransferResponse;
import com.dws.challenge.service.MoneyTransferService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts/transfer")
@Slf4j
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;

    @Autowired
    public MoneyTransferController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public MoneyTransferResponse getAccount(@RequestBody @Valid MoneyTransferRequest transferRequest) {
        log.info("Starting transfer of money from account {} to account {}",
                transferRequest.getAccountFromId(), transferRequest.getAccountToId());
        return this.moneyTransferService.transferMoney(transferRequest);
    }

}
