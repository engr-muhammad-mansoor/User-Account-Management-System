package com.example.ams.data.transaction_data;

import com.example.ams.data.accounts_data.Account;
import com.example.ams.data.accounts_data.AccountRepository;
import com.example.ams.data.accounts_data.AccountService;
import com.example.ams.data.deposit_history_data.DepositHistory;
import com.example.ams.data.deposit_history_data.DepositHistoryRepository;
import com.example.ams.data.deposit_history_data.DepositHistoryService;
import com.example.ams.data.movement_history.MovementHistory;
import com.example.ams.data.movement_history.MovementHistoryRepository;
import com.example.ams.data.movement_history.MovementHistoryService;
import com.example.ams.user.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Transactional
public class TransactionService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;


    @Autowired
    private MovementHistoryRepository movementHistoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DepositHistoryService depositHistoryService;

    @Autowired
    private MovementHistoryService movementHistoryService;

    @Transactional
    public TransactionDTO depositFunds(TransactionDTO transactionDTO) throws Exception {
        Optional<Account> accountOptional = accountRepository.findById(transactionDTO.getAccountId());
        Optional<User> userOptional = userRepository.findById(Math.toIntExact(transactionDTO.getPerformedBy()));

        if (accountOptional.isPresent() && userOptional.isPresent()) {
            Account account = accountOptional.get();
            User user = userOptional.get();

            if (account.getUsers() == null) {
                throw new IllegalAccessException("Account is not associated with any user");
            }

            if (!(account.getUsers().getId() == transactionDTO.getCustomerId())) {
                throw new IllegalArgumentException("CustomerId must match AccountId");
            }

            if (!(account.getTransactionPin().equals(transactionDTO.getTransactionPin()))) {
                throw new IllegalCallerException("Invalid PIN");
            }

            account = accountService.updateBalance(account, transactionDTO.getProcessedAmount());
            DepositHistory deposit = new DepositHistory(
                    ZonedDateTime.now(),
                    transactionDTO.getProcessedAmount(),
                    account.getAvailableBalance(),
                    "DEPOSIT",
                    transactionDTO.getTransactionMethod(),
                    account,
                    user
            );
            deposit = depositHistoryRepository.save(deposit);

            MovementHistory movement = new MovementHistory(
                    ZonedDateTime.now(),
                    transactionDTO.getProcessedAmount(),
                    account.getAvailableBalance(),
                    transactionDTO.getTransactionType(),
                    transactionDTO.getTransactionMethod(),
                    account,
                    user
            );
            movement = movementHistoryRepository.save(movement);

            TransactionDTO transactionDTO2 = new TransactionDTO(
                    account.getId(),
                    account.getUsers().getId(),
                    account.getUsers().getFirstName() + " " + account.getUsers().getLastName(),
                    transactionDTO.getProcessedAmount(),
                    account.getAvailableBalance(),
                    "DEPOSIT",
                    deposit.getDepositMethod(),
                    ZonedDateTime.now(),
                    user.getId(),
                    user.getFirstName() + " " + user.getLastName()
            );

            return transactionDTO2;
        } else {
            throw new Exception("Account or User not found");
        }
    }


    @Transactional
    public TransactionDTO withdrawFunds(TransactionDTO transactionDTO) throws Exception {
        Optional<Account> accountOptional = accountRepository.findById(transactionDTO.getAccountId());
        Optional<User> userOptional = userRepository.findById(Math.toIntExact(transactionDTO.getPerformedBy()));

        if (accountOptional.isPresent() && userOptional.isPresent()) {
            Account account = accountOptional.get();
            User user = userOptional.get();

            if (account.getUsers() == null) {
                throw new IllegalAccessException("Account is not associated with any user");
            }

            if (!(account.getUsers().getId() == transactionDTO.getCustomerId())) {
                throw new IllegalArgumentException("CustomerId must match AccountId");
            }

            if (!(account.getTransactionPin().equals(transactionDTO.getTransactionPin()))) {
                throw new IllegalCallerException("Invalid PIN");
            }

            try {
                account = accountService.withDrawBalance(account, transactionDTO.getProcessedAmount());
            } catch (InsufficientResourcesException e) {
                throw new InsufficientResourcesException(e.getMessage());
            }

            MovementHistory movement = new MovementHistory(
                    ZonedDateTime.now(),
                    transactionDTO.getProcessedAmount(),
                    account.getAvailableBalance(),
                    "WITHDRAW",
                    transactionDTO.getTransactionMethod(),
                    account,
                    user
            );
            movement = movementHistoryRepository.save(movement);

            return new TransactionDTO(
                    account.getId(),
                    account.getUsers().getId(),
                    account.getUsers().getFirstName() + " " + account.getUsers().getLastName(),
                    transactionDTO.getProcessedAmount(),
                    account.getAvailableBalance(),
                    "WITHDRAW",
                    movement.getTransactionMethod(),
                    ZonedDateTime.now(),
                    user.getId(),
                    user.getFirstName() + " " + user.getLastName()
            );
        } else {
            throw new Exception("Account or User not found");
        }
    }

    public PurchaseDTO purchaseProduct(PurchaseDTO transactionDTO) throws Exception {
        Optional<Account> accountOptional = accountRepository.findById(transactionDTO.getAccountId());
        Optional<User> userOptional = userRepository.findById(Math.toIntExact(transactionDTO.getSellerId()));

        if (accountOptional.isPresent() && userOptional.isPresent()) {
            Account account = accountOptional.get();
            User user = userOptional.get();

            if (account.getUsers() == null) {
                throw new IllegalAccessException("Account is not associated with any user");
            }

            if (!(account.getUsers().getId() == transactionDTO.getCustomerId())) {
                throw new IllegalArgumentException("CustomerId must match AccountId");
            }

            if (!(account.getTransactionPin().equals(transactionDTO.getTransactionPin()))) {
                throw new IllegalCallerException("Invalid PIN");
            }

            try {
                account = accountService.withDrawBalance(account, transactionDTO.getProcessedAmount());
            } catch (InsufficientResourcesException e) {
                throw new InsufficientResourcesException(e.getMessage());
            }

            MovementHistory movement = new MovementHistory(
                    ZonedDateTime.now(),
                    transactionDTO.getProcessedAmount(),
                    account.getAvailableBalance(),
                    "PURCHASE",
                    transactionDTO.getTransactionMethod(),
                    account,
                    user
            );
            movement = movementHistoryRepository.save(movement);

            return new PurchaseDTO(
                    account.getId(),
                    account.getUsers().getId(),
                    account.getUsers().getFirstName() + account.getUsers().getLastName(),
                    transactionDTO.getProductId(),
                    transactionDTO.getProductDescription(),
                    transactionDTO.getProcessedAmount(),
                    account.getAvailableBalance(),
                    "PURCHASE",
                    movement.getTransactionMethod(),
                    ZonedDateTime.now(),
                    user.getId(),
                    user.getFirstName() + " " + user.getLastName()
            );
        } else {
            throw new Exception("Account or User not found");
        }
    }

}