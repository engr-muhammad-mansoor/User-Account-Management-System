package com.example.ams.data.deposit_history_data;

import com.example.ams.data.accounts_data.Account;
import com.example.ams.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.ZonedDateTime;


@Builder
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "DEPOSIT_HISTORY")
@Data
public class DepositHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DEPOSIT_DATE_AND_TIME")
    private ZonedDateTime depositDateAndTime;

    @Column(name = "DEPOSITED_AMOUNT")
    private BigDecimal depositedAmount;

    @Column(name = "UPDATED_BALANCE")
    private BigDecimal updatedBalance;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;


    @Column(name = "DEPOSIT_METHOD")
    private String depositMethod;

    @ManyToOne()
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID")
    private Account account;




    public DepositHistory( ZonedDateTime depositDateAndTime, BigDecimal depositedAmount, BigDecimal updatedBalance, String transactionType, String depositMethod, Account account, User approvedBy) {

        this.depositDateAndTime = depositDateAndTime;
        this.depositedAmount = depositedAmount;
        this.updatedBalance = updatedBalance;
        this.transactionType = transactionType;
        this.depositMethod = depositMethod;
        this.account = account;
        this.approvedBy = approvedBy;
    }

    @ManyToOne()
    @JoinColumn(name = "APPROVED_BY", referencedColumnName = "ID")
    private User approvedBy;

    public DepositHistory(){

    }

}
