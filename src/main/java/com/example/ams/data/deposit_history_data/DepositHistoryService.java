package com.example.ams.data.deposit_history_data;

import com.example.ams.data.accounts_data.Account;
import com.example.ams.data.accounts_data.AccountRepository;
import com.example.ams.user.User;
import com.example.ams.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class DepositHistoryService {

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Autowired

    private UserRepository usersRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<DepositHistoryDTO> getAllDepositHistory() {
        List<DepositHistory> depositHistories = depositHistoryRepository.findAll();
        if (depositHistories.isEmpty()) {
            throw new DepositHistoryNotFoundException("Deposit History not found");
        }

        List<DepositHistoryDTO> depositHistoryDTOList = new ArrayList<>();
        for (DepositHistory dh : depositHistories) {
            DepositHistoryDTO depositHistoryDTO = new DepositHistoryDTO();
            depositHistoryDTOList.add(depositHistoryDTO.convertToDTO(dh));
        }

        return depositHistoryDTOList;
    }

    public List<DepositHistoryDTO> getDepositHistoryByApprovedById(Long approvedById) {
        // Assuming you have a method in your repository to retrieve deposit history by approved by ID
        List<DepositHistory> depositHistoryList = depositHistoryRepository.findByApprovedById(approvedById);

        // Assuming you have an instance of DepositHistoryDTO, adjust accordingly
        DepositHistoryDTO depositHistoryDTO = new DepositHistoryDTO();

        // Convert the list of entities to DTOs
        List<DepositHistoryDTO> depositHistoryDTOList = depositHistoryList.stream()
                .map(depositHistoryDTO::convertToDTO)
                .collect(Collectors.toList());

        return depositHistoryDTOList;
    }

    public Optional<DepositHistoryDTO> getDepositHistoryById(Long id) {
        DepositHistory depositHistory = depositHistoryRepository.findById(id).get();
        DepositHistoryDTO depositHistoryDTO = new DepositHistoryDTO();
        depositHistoryDTO.convertToDTO(depositHistory);
        return Optional.of(depositHistoryDTO);
    }

    public DepositHistoryDTO createDepositHistory(DepositHistoryDTO depositHistoryDTO) {
        Long approvedById = depositHistoryDTO.getApprovedById();
        Long accountId = depositHistoryDTO.getAccountId();

        // Check if the approvedBy user and account exist
        Optional<User> approvedByUserOptional = usersRepository.findById(Math.toIntExact(approvedById));
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (!approvedByUserOptional.isPresent() || !accountOptional.isPresent()) {
            throw new IllegalArgumentException("Invalid approvedBy user or account");
        }

        DepositHistory newHistory = depositHistoryDTO.convertToEntity(depositHistoryDTO);

        // Set approvedBy user and account
        newHistory.setApprovedBy(approvedByUserOptional.get());
        newHistory.setAccount(accountOptional.get());

        // Save the deposit history
        DepositHistory savedHistory = depositHistoryRepository.save(newHistory);

        // Convert the saved history to a DTO
        DepositHistoryDTO savedHistoryDTO = new DepositHistoryDTO();
        savedHistoryDTO = savedHistoryDTO.convertToDTO(savedHistory);

        return savedHistoryDTO;
    }

    public DepositHistoryDTO updateDepositHistoryDTO(Long id, DepositHistoryDTO updatedDepositHistoryDTO) {
        // Check if the deposit history with the specified ID exists
        Optional<DepositHistory> depositHistoryOptional = depositHistoryRepository.findById(id);

        if (depositHistoryOptional.isPresent()) {
            DepositHistory existingHistory = depositHistoryOptional.get();

            // Check if the updated deposit history has the same ID
            if (!existingHistory.getId().equals(updatedDepositHistoryDTO.getId())) {
                throw new IllegalArgumentException("Cannot change the ID of an existing deposit history");
            }

            // Check if the approvedBy user and account exist
            Long approvedById = updatedDepositHistoryDTO.getApprovedById();
            Long accountId = updatedDepositHistoryDTO.getAccountId();

            Optional<User> approvedByUserOptional = usersRepository.findById(Math.toIntExact(approvedById));
            Optional<Account> accountOptional = accountRepository.findById(accountId);

            if (approvedByUserOptional.isEmpty() || accountOptional.isEmpty()) {
                throw new IllegalArgumentException("Invalid approvedBy user or account");
            }

            existingHistory.setApprovedBy(approvedByUserOptional.get());
            existingHistory.setAccount(accountOptional.get());

            // Save the updated deposit history
            DepositHistory updatedDepositHistory = depositHistoryRepository.save(existingHistory);

            // Convert the updated deposit history to a DTO
            DepositHistoryDTO updatedNewDepositHistoryDTO = new DepositHistoryDTO();
            updatedNewDepositHistoryDTO = updatedNewDepositHistoryDTO.convertToDTO(updatedDepositHistory);

            return updatedNewDepositHistoryDTO;
        } else {
            throw new DepositHistoryNotFoundException("Deposit history with id " + id + " not found.");
        }
    }

    public void deleteDepositHistory(Long id) {
        if (depositHistoryRepository.existsById(id)) {
            depositHistoryRepository.deleteById(id);
        } else {
            throw new DepositHistoryNotFoundException("DepositHistory with id " + id + " not found.");
        }
    }

    public List<DepositHistoryDTO> getDepositHistoryByAccountId(Long accountId) {
        List<DepositHistory> depositHistoryList = depositHistoryRepository.findByAccountId(accountId);

        // Assuming you have an instance of DepositHistoryDTO, adjust accordingly
        DepositHistoryDTO depositHistoryDTO = new DepositHistoryDTO();

        // Convert the list of entities to DTOs
        List<DepositHistoryDTO> depositHistoryDTOList = depositHistoryList.stream()
                .map(depositHistoryDTO::convertToDTO)
                .collect(Collectors.toList());

        return depositHistoryDTOList;
    }
}

