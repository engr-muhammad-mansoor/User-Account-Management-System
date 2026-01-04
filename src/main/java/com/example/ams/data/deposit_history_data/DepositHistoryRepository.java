package com.example.ams.data.deposit_history_data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositHistoryRepository extends JpaRepository<DepositHistory, Long> {
    List<DepositHistory> findByAccountId(Long accountId);

    List<DepositHistory> findByApprovedById(Long approvedById);
    // Add custom query methods if needed
}
