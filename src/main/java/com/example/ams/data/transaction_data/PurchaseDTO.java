package com.example.ams.data.transaction_data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class PurchaseDTO {

    private long accountId;
    private long customerId;
    private String customerName;
    private Long productId;
    private String productDescription;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long transactionPin;
    private BigDecimal processedAmount;
    private BigDecimal availableBalance;
    private String transactionType;
    private String transactionMethod;
    private ZonedDateTime transactionDateAndTime;
    private Long sellerId;
    private String sellerName;

    public PurchaseDTO(long accountId, long customerId, String customerName, Long productId, String productDescription, BigDecimal processedAmount, BigDecimal availableBalance, String transactionType, String transactionMethod, ZonedDateTime transactionDateAndTime, Long sellerId, String sellerName) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.productId = productId;
        this.productDescription = productDescription;
        this.processedAmount = processedAmount;
        this.availableBalance = availableBalance;
        this.transactionType = transactionType;
        this.transactionMethod = transactionMethod;
        this.transactionDateAndTime = transactionDateAndTime;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
    }

    public PurchaseDTO() {
    }

//    public PurchaseDTO(long accountId, long customerId, String productName, BigDecimal processedAmount, BigDecimal availableBalance, String transactionType, String transactionMethod, ZonedDateTime transactionDateAndTime, Long sellerId) {
//        this.accountId = accountId;
//        this.customerId = customerId;
//        this.productName = productName;
//        this.processedAmount = processedAmount;
//        this.availableBalance = availableBalance;
//        this.transactionType = transactionType;
//        this.transactionMethod = transactionMethod;
//        this.transactionDateAndTime = transactionDateAndTime;
//        this.sellerId = sellerId;
//    }
}
