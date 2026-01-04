package com.example.ams.data.movement_history;


import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class MovementHistoryDTO {

    public Long id;
    private ZonedDateTime movementDateAndTime;
    private BigDecimal processedAmount;
    private BigDecimal updatedBalance;
    private String transactionType;
    private String transactionMethod;
    private Long accountId;
    private Long approvedById;
    private String aprrovedByDetails;

    public MovementHistoryDTO(){

    }

    public MovementHistoryDTO(Long id, ZonedDateTime movementDateAndTime, BigDecimal processedAmount, BigDecimal updatedBalance, String transactionType, String transactionMethod, Long accountId, Long approvedById, String approvedByDetails) {
        this.id = id;
        this.movementDateAndTime = movementDateAndTime;
        this.processedAmount = processedAmount;
        this.updatedBalance = updatedBalance;
        this.transactionType = transactionType;
        this.transactionMethod = transactionMethod;
        this.accountId = accountId;
        this.approvedById = approvedById;
        this.aprrovedByDetails = approvedByDetails;
    }

    public MovementHistoryDTO convertToDTO(MovementHistory movementHistory) {
        MovementHistoryDTO dto = new MovementHistoryDTO();
        dto.setId(movementHistory.getId());
        dto.setMovementDateAndTime(movementHistory.getMovementDateAndTime());
        dto.setProcessedAmount(movementHistory.getProcessedAmount());
        dto.setUpdatedBalance(movementHistory.getUpdatedBalance());
        dto.setTransactionType(movementHistory.getTransactionType());
        dto.setTransactionMethod(movementHistory.getTransactionMethod());
        dto.setApprovedById(movementHistory.getApprovedBy().getId());
        dto.setAccountId(movementHistory.getAccount().getId());
        dto.setAprrovedByDetails(movementHistory.getApprovedBy().getId() + " -- " +
                movementHistory.getApprovedBy().getFirstName() + " " + movementHistory.getApprovedBy().getLastName());

        return dto;
    }

    public MovementHistory convertToEntity(MovementHistoryDTO movementHistoryDTO){
        MovementHistory movementHistory = new MovementHistory();
        movementHistory.setMovementDateAndTime(movementHistoryDTO.getMovementDateAndTime());
        movementHistory.setProcessedAmount(movementHistoryDTO.getProcessedAmount());
        movementHistory.setUpdatedBalance(movementHistoryDTO.getUpdatedBalance());
        movementHistory.setTransactionType(movementHistoryDTO.getTransactionType());
        movementHistory.setTransactionMethod(movementHistoryDTO.getTransactionMethod());

        return movementHistory;
    }
}
