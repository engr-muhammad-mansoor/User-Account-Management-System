package com.example.ams.data.movement_history;


import com.example.ams.data.accounts_data.Account;
import com.example.ams.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "MOVEMENT_HISTORY")
@Data
public class MovementHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MOVEMENT_DATE_AND_TIME")
    private ZonedDateTime movementDateAndTime;

    @Column(name = "PROCESSED_AMOUNT")
    private BigDecimal processedAmount;

    @Column(name = "UPDATED_BALANCE")
    private BigDecimal updatedBalance;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    @Column(name = "TRANSACTION_METHOD")
    private String transactionMethod;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @ManyToOne()
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID")
    private Account account;

    @ManyToOne()
    @JoinColumn(name = "APPROVED_BY", referencedColumnName = "ID")
    private User approvedBy;

    public MovementHistory(){

    }

    public MovementHistory( ZonedDateTime movementDateAndTime, BigDecimal processedAmount, BigDecimal updatedBalance, String transactionType, String transactionMethod, Account account, User approvedBy) {
        this.movementDateAndTime = movementDateAndTime;
        this.processedAmount = processedAmount;
        this.updatedBalance = updatedBalance;
        this.transactionType = transactionType;
        this.transactionMethod = transactionMethod;
        this.account = account;
        this.approvedBy = approvedBy;
    }


    public MovementHistory(ZonedDateTime now, BigDecimal processedAmount, BigDecimal availableBalance, String purchase, String transactionMethod, Account account, User user, String productName) {
        this.movementDateAndTime = movementDateAndTime;
        this.processedAmount = processedAmount;
        this.updatedBalance = updatedBalance;
        this.transactionType = transactionType;
        this.transactionMethod = transactionMethod;
        this.account = account;
        this.approvedBy = approvedBy;
        this.productName = productName;
    }
}
