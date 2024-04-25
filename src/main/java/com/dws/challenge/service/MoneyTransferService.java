package com.dws.challenge.service;

import com.dws.challenge.request.MoneyTransferRequest;
import com.dws.challenge.response.MoneyTransferResponse;

public interface MoneyTransferService {

    MoneyTransferResponse transferMoney(MoneyTransferRequest transferRequest);

}

