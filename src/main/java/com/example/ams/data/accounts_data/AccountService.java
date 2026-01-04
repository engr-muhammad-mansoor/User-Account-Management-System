package com.example.ams.data.accounts_data;


import com.example.ams.user.User;
import com.example.ams.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    private UserRepository usersRepository;


    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("No Accounts found");
        }
        List<AccountDTO> accountDTOList = new ArrayList<>();
        for (Account account : accounts) {
            accountDTOList.add(new AccountDTO().convertToDTO(account));
        }
        return accountDTOList;
    }

    public Optional<AccountDTO> getAccountById(Long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            return Optional.of(new AccountDTO().convertToDTO(accountOptional.get()));
        } else {
            throw new AccountNotFoundException("Account not found");
        }
    }

    public AccountDTO createAccount(AccountDTO accountDTO) {
        try {
            validateAccountForCreation(accountDTO);
            accountDTO.setCreationDate(ZonedDateTime.now());
            accountDTO.setModificationDate(null);
            Account ac = new Account();
            ac = accountDTO.convertToEntity(accountDTO);
            Account savedAccount = accountRepository.save(ac);

            return new AccountDTO().convertToDTO(savedAccount);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating account", e);
        }
    }

    public AccountDTO updateAccount(Long id, AccountDTO updatedAccountDTO) {
        // Check if the account with the specified ID exists
        Optional<Account> accountOptional = accountRepository.findById(id);

        if (accountOptional.isPresent()) {
            Account existingAccount = accountOptional.get();

            // Check if the updated account has the same ID
            if (!existingAccount.getId().equals(id)) {
                throw new IllegalArgumentException("Cannot change the ID of an existing account");
            }

            // Check if the user is trying to associate the account with a different user
            Long customerId = updatedAccountDTO.getCustomerId();
            if (customerId != null && !customerId.equals(existingAccount.getUsers().getId())) {
                throw new IllegalArgumentException("Cannot change the associated user of an existing account");
            }

            existingAccount.setTransactionPin(updatedAccountDTO.getTransactionPin());
            existingAccount.setModificationDate(ZonedDateTime.now());
            existingAccount.setAvailableBalance(existingAccount.getAvailableBalance().add(updatedAccountDTO.getInitialBalance()));


            // Save the updated account
            Account updatedAccount = accountRepository.save(existingAccount);

            // Convert the updated account to a DTO
            AccountDTO updatedNewAccountDTO = new AccountDTO();
            updatedNewAccountDTO = updatedNewAccountDTO.convertToDTO(updatedAccount);

            return updatedNewAccountDTO;
        } else {
            throw new AccountNotFoundException("Account with id " + id + " not found.");
        }
    }

    public Account updateBalance(Account account, BigDecimal newBalance) {
        BigDecimal currentBalance = account.getAvailableBalance();
        BigDecimal updatedBalance = currentBalance.add(newBalance); // Use add to update the balance
        account.setAvailableBalance(updatedBalance);
        account = accountRepository.save(account);
        return account;
    }

    public Account withDrawBalance(Account account, BigDecimal processAmount) {
        BigDecimal currentBalance = account.getAvailableBalance();

        // Check if the withdrawal amount is less than or equal to the current balance
        if (currentBalance.compareTo(processAmount) >= 0) {
            BigDecimal updatedBalance = currentBalance.subtract(processAmount);

            // Check if the updated balance is greater than or equal to zero
            if (updatedBalance.compareTo(BigDecimal.ZERO) >= 0) {
                account.setAvailableBalance(updatedBalance);
                account = accountRepository.save(account);
                return account;
            } else {
                throw new IllegalArgumentException("Withdrawal would result in negative balance");
            }
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }



    public boolean updateTransactionPin(Long accountId, Long previoustransactionPin, Long newtranactionPin){
        Account account = accountRepository.findById(accountId).get();
        if(account.getTransactionPin() == previoustransactionPin) {
            account.setTransactionPin(newtranactionPin);
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    public void deleteAccount(Long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            // Disassociate the account from users
            if (account.getUsers() != null) {
                 User user = null;
                 user = usersRepository.findById(Math.toIntExact(account.getUsers().getId())).get();
                    user.setUserAccount(null);
                    usersRepository.save(user);

            }

            // Delete the account
            accountRepository.deleteById(id);
        } else {
            throw new AccountNotFoundException("Account with id " + id + " not found.");
        }
    }


    private void validateAccountForCreation(AccountDTO accountDTO) {
        if (accountDTO.getId() != null && accountRepository.existsById(accountDTO.getId())) {
            throw new IllegalArgumentException("Account with ID " + accountDTO.getId() + " already exists");
        }
    }
}

