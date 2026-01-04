package com.example.ams.data.movement_history;

import com.example.ams.data.accounts_data.Account;
import com.example.ams.data.accounts_data.AccountRepository;
import com.example.ams.user.User;
import com.example.ams.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovementHistoryService {

    @Autowired
    private MovementHistoryRepository movementHistoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<MovementHistoryDTO> getAllMovementHistory() {
        List<MovementHistory> movementHistoryList = movementHistoryRepository.findAll();

        if (movementHistoryList.isEmpty()) {
            // Handle the case where no movement history records are found
            throw new MovementHistoryNotFoundException("No movement history records found.");
        }

        MovementHistoryDTO movementHistoryDTO = new MovementHistoryDTO();

        List<MovementHistoryDTO> movementHistoryDTOList = movementHistoryList.stream()
                .map(movementHistoryDTO::convertToDTO)
                .collect(Collectors.toList());

        return movementHistoryDTOList;
    }
    public Optional<MovementHistoryDTO> getMovementHistoryById(Long id) {
        Optional<MovementHistory> movementHistoryOptional = movementHistoryRepository.findById(id);

        if (movementHistoryOptional.isPresent()) {
            MovementHistory movementHistory = movementHistoryOptional.get();

            MovementHistoryDTO movementHistoryDTO = new MovementHistoryDTO();

            MovementHistoryDTO resultDTO = movementHistoryDTO.convertToDTO(movementHistory);

            return Optional.of(resultDTO);
        } else {
            // Handle the case where no movement history record is found
            throw new MovementHistoryNotFoundException("Movement history record with ID " + id + " not found.");
        }
    }


    public MovementHistoryDTO createMovementHistory(MovementHistoryDTO movementHistoryDTO) {
        Long accountId = movementHistoryDTO.getAccountId();
        Long approvedById = movementHistoryDTO.getApprovedById();

        // Check if the associated Account exists
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Account with ID " + accountId + " not found");
        }

        // Check if the approvedBy User exists
        Optional<User> approvedByUserOptional = userRepository.findById(Math.toIntExact(approvedById));
        if (approvedByUserOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + approvedById + " not found");
        }

        MovementHistoryDTO resultDTO;

        MovementHistory newMovementHistory = movementHistoryDTO.convertToEntity(movementHistoryDTO);

        newMovementHistory.setAccount(accountOptional.get());
        newMovementHistory.setApprovedBy(approvedByUserOptional.get());

        newMovementHistory = movementHistoryRepository.save(newMovementHistory);

        resultDTO = movementHistoryDTO.convertToDTO(newMovementHistory);

        return resultDTO;
    }

    public MovementHistoryDTO updateMovementHistory(Long id, MovementHistoryDTO updatedMovementHistoryDTO) {
        // Check if the MovementHistory with the specified ID exists
        if (movementHistoryRepository.existsById(id)) {
            // Assuming you have an instance of MovementHistoryDTO, adjust accordingly
            MovementHistoryDTO resultDTO;

            // Convert the updated DTO to the entity
            MovementHistory updatedMovementHistory = updatedMovementHistoryDTO.convertToEntity(updatedMovementHistoryDTO);

            // Check if the associated Account exists
            Long accountId = updatedMovementHistoryDTO.getAccountId();
            Optional<Account> accountOptional = accountRepository.findById(accountId);
            if (accountOptional.isEmpty()) {
                throw new IllegalArgumentException("Account with ID " + accountId + " not found");
            }

            // Check if the approvedBy User exists
            Long approvedById = updatedMovementHistoryDTO.getApprovedById();
            Optional<User> approvedByUserOptional = userRepository.findById(Math.toIntExact(approvedById));
            if (approvedByUserOptional.isEmpty()) {
                throw new IllegalArgumentException("User with ID " + approvedById + " not found");
            }

            // Set the associated Account and approvedBy User
            updatedMovementHistory.setAccount(accountOptional.get());
            updatedMovementHistory.setApprovedBy(approvedByUserOptional.get());

            // Set the ID and save the updated MovementHistory
            updatedMovementHistory.setId(id);
            updatedMovementHistory = movementHistoryRepository.save(updatedMovementHistory);

            // Convert the entity back to DTO
            resultDTO = updatedMovementHistoryDTO.convertToDTO(updatedMovementHistory);

            return resultDTO;
        } else {
            throw new MovementHistoryNotFoundException("MovementHistory with id " + id + " not found.");
        }
    }

    public void deleteMovementHistory(Long id) {
        if (movementHistoryRepository.existsById(id)) {
            movementHistoryRepository.deleteById(id);
        } else {
            throw new MovementHistoryNotFoundException("MovementHistory with id " + id + " not found.");
        }
    }

    public List<MovementHistoryDTO> getMovementHistoryByAccountId(Long accountId) {
        List<MovementHistory> movementHistoryList = movementHistoryRepository.findByAccountId(accountId);

        MovementHistoryDTO movementHistoryDTO = new MovementHistoryDTO();

        List<MovementHistoryDTO> movementHistoryDTOList = movementHistoryList.stream()
                .map(movementHistoryDTO::convertToDTO)
                .collect(Collectors.toList());

        if (movementHistoryDTOList.isEmpty()) {
            // Handle the case where no movement history records are found for the specified account
            throw new MovementHistoryNotFoundException("No movement history records found for the account with ID " + accountId + ".");
        }

        return movementHistoryDTOList;
    }

    public List<MovementHistoryDTO> getMovementHistoryByApprovedById(Long approvedById) {
        List<MovementHistory> movementHistoryList = movementHistoryRepository.findByApprovedById(approvedById);

        MovementHistoryDTO movementHistoryDTO = new MovementHistoryDTO();

        List<MovementHistoryDTO> movementHistoryDTOList = movementHistoryList.stream()
                .map(movementHistoryDTO::convertToDTO)
                .collect(Collectors.toList());

        if (movementHistoryDTOList.isEmpty()) {
            // Handle the case where no movement history records are found for the specified approved by ID
            throw new MovementHistoryNotFoundException("No movement history records found for the user with ID " + approvedById + ".");
        }

        return movementHistoryDTOList;
    }
}

