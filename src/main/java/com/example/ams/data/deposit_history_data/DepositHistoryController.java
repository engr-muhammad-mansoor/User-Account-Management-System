package com.example.ams.data.deposit_history_data;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DepositHistoryController {

    @Autowired
    private DepositHistoryService depositHistoryService;

    @GetMapping("/admin/d-h/all/")
    public ResponseEntity<List<DepositHistoryDTO>> getAllDepositHistory() {
        try {
            List<DepositHistoryDTO> depositHistoryDTOList = depositHistoryService.getAllDepositHistory();
            return ResponseEntity.ok(depositHistoryDTOList);
        } catch (DepositHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/system-users/d-h/{id}")
    public ResponseEntity<DepositHistoryDTO> getDepositHistoryById(@PathVariable Long id) {
        try {
            Optional<DepositHistoryDTO> depositHistoryDTO = depositHistoryService.getDepositHistoryById(id);
            return depositHistoryDTO.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (DepositHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/system-users/history/d-h/{accountId}")
    public ResponseEntity<List<DepositHistoryDTO>> getDepositHistoryByAccountId(@PathVariable Long accountId) {
        try {
            List<DepositHistoryDTO> depositHistoryDTOList = depositHistoryService.getDepositHistoryByAccountId(accountId);
            return ResponseEntity.ok(depositHistoryDTOList);
        } catch (DepositHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @PostMapping("/system-users/system-users/add-deposit-history/")
    public ResponseEntity<DepositHistoryDTO> createDepositHistory(@RequestBody DepositHistoryDTO depositHistoryDTO) {
        try {
            DepositHistoryDTO createddepositHistoryDTO = depositHistoryService.createDepositHistory(depositHistoryDTO);
            return new ResponseEntity<>(createddepositHistoryDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/system-users/d-h/{id}")
    public ResponseEntity<DepositHistoryDTO> updateDepositHistory(@PathVariable Long id, @RequestBody DepositHistoryDTO updateddepositHistoryDTO) {
        try {
            DepositHistoryDTO depositHistoryDTO = depositHistoryService.updateDepositHistoryDTO(id, updateddepositHistoryDTO);
            return ResponseEntity.ok(depositHistoryDTO);
        } catch (DepositHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/admin/history/d-h/{approvedById}")
    public ResponseEntity<List<DepositHistoryDTO>> getDepositHistoryByApprovedById(@PathVariable Long approvedById) {
        try {
            List<DepositHistoryDTO> depositHistoryDTOList = depositHistoryService.getDepositHistoryByApprovedById(approvedById);
            return ResponseEntity.ok(depositHistoryDTOList);
        } catch (DepositHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @DeleteMapping("/admin/d-h/{id}")
    public ResponseEntity<Void> deleteDepositHistory(@PathVariable Long id) {
        try {
            depositHistoryService.deleteDepositHistory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DepositHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}

