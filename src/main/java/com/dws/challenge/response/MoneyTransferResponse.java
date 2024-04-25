package com.dws.challenge.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class MoneyTransferResponse {
    private String accountFromId;
    private String accountToId;
    private BigDecimal transferAmount;
    
    public enum Status {
        SUCCESS,
        FAILURE
    }
    private Status status;
    private String message;

}
