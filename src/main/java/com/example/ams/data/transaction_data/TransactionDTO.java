package com.example.ams.data.transaction_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
public class TransactionDTO {
    private long accountId;
    private long customerId;
    private String customerName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long transactionPin;
    private BigDecimal processedAmount;
    private BigDecimal availableBalance;
    private String transactionType;
    private String transactionMethod;
    private ZonedDateTime transactionDateAndTime;
    private Long performedBy;
    private String employee_name;

    public TransactionDTO(){}

    public TransactionDTO(long accountId, long customerId, String customerName, BigDecimal processedAmount, BigDecimal availableBalance, String transactionType, String transactionMethod, ZonedDateTime transactionDateAndTime, Long performedBy, String employee_name) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.customerName=customerName;
        this.processedAmount = processedAmount;
        this.availableBalance = availableBalance;
        this.transactionType = transactionType;
        this.transactionMethod = transactionMethod;
        this.transactionDateAndTime = transactionDateAndTime;
        this.performedBy = performedBy;
        this.employee_name = employee_name;

    }
}
