package com.example.ams.data.movement_history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class MovementHistoryController {

    @Autowired
    private MovementHistoryService movementHistoryService;



    @GetMapping("/admin/m-h/all/")
    public ResponseEntity<List<MovementHistoryDTO>> getAllMovementHistory() {
        try {
            List<MovementHistoryDTO> movementHistoryDTOList = movementHistoryService.getAllMovementHistory();
            return ResponseEntity.ok(movementHistoryDTOList);
        } catch (MovementHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/system-users/m-h/{id}")
    public ResponseEntity<MovementHistoryDTO> getMovementHistoryById(@PathVariable Long id) {
        try {
            Optional<MovementHistoryDTO> movementHistoryDTO = movementHistoryService.getMovementHistoryById(id);
            return movementHistoryDTO.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (MovementHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/system-users/history/m-h/{accountId}")
    public ResponseEntity<List<MovementHistoryDTO>> getMovementHistoryByAccountId(@PathVariable Long accountId) {
        try {
            List<MovementHistoryDTO> movementHistoryDTOList = movementHistoryService.getMovementHistoryByAccountId(accountId);
            return ResponseEntity.ok(movementHistoryDTOList);
        } catch (MovementHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @PostMapping("/system-users/system-users/add-movement-history/")
    public ResponseEntity<MovementHistoryDTO> createMovementHistory(@RequestBody MovementHistoryDTO movementHistoryDTO) {
        try {
            MovementHistoryDTO createdMovementHistoryDTO = movementHistoryService.createMovementHistory(movementHistoryDTO);
            return new ResponseEntity<>(createdMovementHistoryDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/system-users/m-h/{id}")
    public ResponseEntity<MovementHistoryDTO> updateMovementHistory(@PathVariable Long id, @RequestBody MovementHistoryDTO updatedMovementHistoryDTO) {
        try {
            MovementHistoryDTO movementHistoryDTO = movementHistoryService.updateMovementHistory(id, updatedMovementHistoryDTO);
            return ResponseEntity.ok(movementHistoryDTO);
        } catch (MovementHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/admin/history/m-h/{approvedById}")
    public ResponseEntity<List<MovementHistoryDTO>> getMovementHistoryByApprovedById(@PathVariable Long approvedById) {
        try {
            List<MovementHistoryDTO> movementHistoryDTOList = movementHistoryService.getMovementHistoryByApprovedById(approvedById);
            return ResponseEntity.ok(movementHistoryDTOList);
        } catch (MovementHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @DeleteMapping("/admin/m-h/{id}")
    public ResponseEntity<Void> deleteMovementHistory(@PathVariable Long id) {
        try {
            movementHistoryService.deleteMovementHistory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (MovementHistoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
