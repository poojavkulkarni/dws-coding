package com.dws.challenge.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class MoneyTransferRequest {
    private String accountFromId;
    private String accountToId;
    private BigDecimal transferAmount;
                                                                                            
    @JsonCreator
    public MoneyTransferRequest(@JsonProperty("accountFromId") String accountFromId,
                                @JsonProperty("accountToId") String accountToId,
                                @JsonProperty("transferAmount") BigDecimal transferAmount) {
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.transferAmount = transferAmount;
    }

}
