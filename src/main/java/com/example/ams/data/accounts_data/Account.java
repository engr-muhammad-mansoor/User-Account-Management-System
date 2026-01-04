package com.example.ams.data.accounts_data;


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
@Table(name = "ACCOUNTS")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TRANSACTION_PIN")
    private Long transactionPin;

    @Column(name = "AVAILABLE_BALANCE")
    private BigDecimal availableBalance;

    @Column(name = "ACCOUNT_HOLDER_STATUS")
    private String accountHolderStatus;

    @Column(name = "CREATION_DATE")
    private ZonedDateTime creationDate;

    @Column(name = "MODIFICATION_DATE")
    private ZonedDateTime modificationDate;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @OneToOne(mappedBy = "userAccount")
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private User users;

    public Account() {
    }
}
