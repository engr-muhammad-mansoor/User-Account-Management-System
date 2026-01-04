package com.example.ams.data.movement_history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementHistoryRepository extends JpaRepository<MovementHistory, Long> {
    List<MovementHistory> findByApprovedById(Long approvedById);

    List<MovementHistory> findByAccountId(Long accountId);
    // Add custom query methods if needed
}
