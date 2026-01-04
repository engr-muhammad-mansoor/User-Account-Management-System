package com.example.ams.data.accounts_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class AccountDTO {

    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long transactionPin;

    private BigDecimal initialBalance;
    private String accountHolderStatus;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long customerId;
    private ZonedDateTime creationDate;
    private ZonedDateTime modificationDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long createdBy;

    public AccountDTO() {
    }

    public AccountDTO convertToDTO(Account account) {
        this.id = account.getId();
        this.transactionPin = account.getTransactionPin();
        this.initialBalance = account.getAvailableBalance();
        this.accountHolderStatus = account.getAccountHolderStatus();
        this.creationDate = account.getCreationDate();
        this.modificationDate = account.getModificationDate();
        this.createdBy = account.getCreatedBy();

        if (account.getUsers() != null && account.getUsers().getId() != null) {
            this.customerId = account.getUsers().getId();
        } else {
            this.customerId = 0L;
        }

        return this;
    }

    public Account convertToEntity(AccountDTO accountDTO) {
        Account account = new Account();
        account.setTransactionPin(accountDTO.getTransactionPin());
        account.setAccountHolderStatus(accountDTO.getAccountHolderStatus());
        account.setAvailableBalance(accountDTO.getInitialBalance());
        account.setCreationDate(accountDTO.getCreationDate());
        account.setCreatedBy(accountDTO.getCreatedBy());
        account.setModificationDate(accountDTO.getModificationDate());

        return account;
    }
}
