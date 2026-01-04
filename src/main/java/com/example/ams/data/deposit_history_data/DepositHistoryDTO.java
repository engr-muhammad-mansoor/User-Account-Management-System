package com.example.ams.data.deposit_history_data;


import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class DepositHistoryDTO {
    public Long id;
    private ZonedDateTime depositDateAndTime;
    private BigDecimal depositedAmount;
    private BigDecimal updatedBalance;
    private String transactionType;
    private String depositMethod;
    private Long accountId;
    private Long approvedById;
    private String approvedByDetails;

    public DepositHistoryDTO(){

    }

    public DepositHistoryDTO(Long id, ZonedDateTime depositDateAndTime, BigDecimal depositedAmount, BigDecimal updatedBalance, String transactionType, String depositMethod, Long accountId, Long approvedById, String approvedByDetails) {
        this.id = id;
        this.depositDateAndTime = depositDateAndTime;
        this.depositedAmount = depositedAmount;
        this.updatedBalance = updatedBalance;
        this.transactionType = transactionType;
        this.depositMethod = depositMethod;
        this.accountId = accountId;
        this.approvedById = approvedById;
        this.approvedByDetails = approvedByDetails;
    }

    public DepositHistoryDTO convertToDTO(DepositHistory depositHistory) {
        DepositHistoryDTO dto = new DepositHistoryDTO();
        dto.setId(depositHistory.getId());
        dto.setDepositDateAndTime(depositHistory.getDepositDateAndTime());
        dto.setDepositedAmount(depositHistory.getDepositedAmount());
        dto.setUpdatedBalance(depositHistory.getUpdatedBalance());
        dto.setTransactionType(depositHistory.getTransactionType());
        dto.setDepositMethod(depositHistory.getDepositMethod());
        dto.setApprovedById(depositHistory.getApprovedBy().getId());
        dto.setApprovedByDetails(depositHistory.getApprovedBy().getId() + " -- " +
                depositHistory.getApprovedBy().getFirstName() + " " + depositHistory.getApprovedBy().getLastName());
        dto.setAccountId(depositHistory.getAccount().getId());

        return dto;
    }

    public DepositHistory convertToEntity(DepositHistoryDTO depositHistoryDTO) {
        DepositHistory depositHistory = new DepositHistory();
        depositHistory.setDepositDateAndTime(ZonedDateTime.now());
        depositHistory.setDepositedAmount(depositHistoryDTO.getDepositedAmount());
        depositHistory.setUpdatedBalance(depositHistoryDTO.getUpdatedBalance());
        depositHistory.setTransactionType("Deposit");

        if (depositHistoryDTO.getDepositedAmount() == null || depositHistoryDTO.getDepositMethod() == null) {
            throw new IllegalArgumentException("Deposited amount and deposit method are required");
        }

        depositHistory.setDepositDateAndTime(depositHistoryDTO.getDepositDateAndTime()); // Allow the client to provide the date

        return depositHistory;
    }
}
