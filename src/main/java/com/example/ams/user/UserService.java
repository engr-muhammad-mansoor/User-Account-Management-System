package com.example.ams.user;

import com.example.ams.data.accounts_data.Account;
import com.example.ams.data.accounts_data.AccountDTO;
import com.example.ams.data.accounts_data.AccountRepository;

import com.example.ams.token.Token;
import com.example.ams.token.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {


    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    TokenRepository tokenRepository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        usersRepository.save(user);
    }


    public List<UsersDTO> getAllUsers() {
        List<User> userList = usersRepository.findAll();
        if (userList.isEmpty()) {
            throw new UserNotFoundException("No User Exists");
        }
        UsersDTO userDTO = new UsersDTO();
        List<UsersDTO> usersDTOList = new ArrayList<>(); // Initialize the list

        for (User user : userList) {
            usersDTOList.add(userDTO.convertToDTO(user));
        }

        return usersDTOList;
    }

    public Optional<UsersDTO> getUserById(Long id) {
        Optional<User> user = usersRepository.findById(Math.toIntExact(id));
        if (user.isEmpty() || user.get().getRole()== (Role.CUSTOMER)) {
            throw new UserNotFoundException("User not found");
        }
        UsersDTO userDTO = new UsersDTO();
        userDTO = userDTO.convertToDTO(user.get());

        return Optional.ofNullable(userDTO);
    }

    public UsersDTO createUser(UsersDTO userDTO) throws Exception {
        if (usersRepository.existsByEmail(userDTO.getEmail()) || usersRepository.existsByContactNumber(userDTO.getContactNumber())) {
            throw new Exception("UserExists");
        } else {
            User newUser = userDTO.convertToEntity(userDTO);
            newUser.setCreationDate(ZonedDateTime.now());
            newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            String roleString = userDTO.getRole();

            if (roleString.equalsIgnoreCase("CUSTOMER")) {
                newUser.setRole(Role.CUSTOMER);
            } else {
                // Handle the case where an invalid role is provided
                throw new IllegalArgumentException("Invalid role: " + roleString);
            }

            Optional<Account> userAccount = accountRepository.findById(userDTO.getAccountId());

            newUser.setUserAccount(userAccount.get());
            newUser.setModificationDate(ZonedDateTime.now());
            newUser = usersRepository.save(newUser);


            return userDTO.convertToDTO(newUser);
        }
    }

    public UsersDTO updateUser(Long id, UsersDTO updatedUserDTO) throws Exception {
        User user = null;
        user = usersRepository.findById(Math.toIntExact(id)).get();
        if (user != null) {
            if (usersRepository.existsByEmailOrContactNumberAndIdNot(updatedUserDTO.getEmail(), updatedUserDTO.getContactNumber(), updatedUserDTO.getId())) {
                throw new Exception("User Exists with credentials");
            }
            if(user.getRole()== (Role.CUSTOMER)){
                throw new Exception("User Not found");
            }
            User newUpdatedUser = updatedUserDTO.convertToEntity(updatedUserDTO);

            String roleString = updatedUserDTO.getRole();

            if (roleString.equalsIgnoreCase("ADMIN")) {
                newUpdatedUser.setRole(Role.ADMIN);
            } else if (roleString.equalsIgnoreCase("SYSTEM_USER")) {
                newUpdatedUser.setRole(Role.SYSTEM_USER);
            } else if (roleString.equalsIgnoreCase("WORKER")) {
                newUpdatedUser.setRole(Role.WORKER);
            } else if (roleString.equalsIgnoreCase("SIMPLE_EMPLOYEE")) {
                newUpdatedUser.setRole(Role.SIMPLE_EMPLOYEE);
            } else if (roleString.equalsIgnoreCase("CUSTOMER")) {
                newUpdatedUser.setRole(Role.CUSTOMER);
            } else {
                // Handle the case where an invalid role is provided
                newUpdatedUser.setRole(null);
            }
            User userAccount = user;

            newUpdatedUser.setUserAccount(null);
            newUpdatedUser.setPassword(user.getPassword());
            newUpdatedUser.setCreationDate(user.getCreationDate());
            newUpdatedUser.setModificationDate(ZonedDateTime.now());
            newUpdatedUser = usersRepository.save(newUpdatedUser);


            return updatedUserDTO.convertToDTO(newUpdatedUser);

        } else {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
    }

    public void deleteUser(Long id) throws Exception {
        Optional<User> user = usersRepository.findById(Math.toIntExact(id));
        if (user.isPresent()) {
            if (!usersRepository.existsByUserAccountIsNotNullAndId(user.get().getId())) {

                List<Token> tokens = tokenRepository.findByUserId(id);
                tokenRepository.deleteAll(tokens);
                usersRepository.deleteById(Math.toIntExact(id));
            } else throw new Exception("User has an associated account");
        } else throw new UserNotFoundException("User with id " + id + " not found.");
    }


    @Transactional
    public void associateAccount(Long userId, Long accountId) throws Exception {
        Optional<User> userOptional = usersRepository.findById(Math.toIntExact(userId));

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOptional.get();

        if (user.getUserAccount() != null) {
            throw new Exception("User already has an associated account");
        }

        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundException("Account not found");
        }

        Account account = accountOptional.get();

        if (account.getUsers() != null) {
            throw new Exception("Account is already associated with a user");
        }

        // Associate the account with the user
        account.setUsers(user);
        user.setUserAccount(account);

        // Save both user and account in a transaction
        try {
            usersRepository.save(user);
            accountRepository.save(account);
        } catch (Exception e) {
            // Handle any exception that might occur during the save operation
            throw new RuntimeException("Error associating account: " + e.getMessage());
        }
    }


    public List<UsersDTO> getAllCustomers() {
        List<User> userList = usersRepository.findAll();
        if (userList.isEmpty()) {
            throw new UserNotFoundException("No User Exists");
        }

        List<UsersDTO> customersDTOList = userList.stream()
                .filter(user -> user.getRole() == Role.CUSTOMER)
                .map(user -> new UsersDTO().convertToDTO(user))
                .collect(Collectors.toList());

        if (customersDTOList.isEmpty()) {
            throw new UserNotFoundException("No Customer Exists");
        }

        return customersDTOList;
    }


    public Optional<UsersDTO> getCustomerById(Long id) {
        Optional<User> userOptional = usersRepository.findById(Math.toIntExact(id));

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOptional.get();

        // Check if the user has the role of a customer
        if (user.getRole() != Role.CUSTOMER) {
            throw new UserNotFoundException("User is not a Customer");
        }

        UsersDTO userDTO = new UsersDTO().convertToDTO(user);

        return Optional.ofNullable(userDTO);
    }

    public UsersDTO createCustomer(UsersDTO userDTO) throws Exception {
        // Check if the email or contact number already exists
        if (usersRepository.existsByEmail(userDTO.getEmail()) || usersRepository.existsByContactNumber(userDTO.getContactNumber())) {
            throw new Exception("UserExists");
        }

        // Convert DTO to Entity
        User newUser = userDTO.convertToEntity(userDTO);

        // Set creation and modification dates
        newUser.setCreationDate(ZonedDateTime.now());
        newUser.setModificationDate(ZonedDateTime.now());

        // Encode password
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Set user role based on the provided role string
        String roleString = userDTO.getRole();
        if (roleString.equalsIgnoreCase("CUSTOMER")) {
            newUser.setRole(Role.CUSTOMER);
        } else {
            // Handle the case where an invalid role is provided
            throw new IllegalArgumentException("Invalid role: " + roleString);
        }
        Account userAccountOptional = null;


        newUser.setUserAccount(userAccountOptional);

        // Save the new user
        newUser = usersRepository.save(newUser);

        // Convert the saved user entity back to DTO and return
        return new UsersDTO().convertToDTO(newUser);
    }

    public UsersDTO updateCustomer(Long id, UsersDTO updatedUserDTO) throws Exception {
        User user = null;
        user = usersRepository.findById(Math.toIntExact(id)).get();
        if (user != null) {
            if (usersRepository.existsByEmailOrContactNumberAndIdNot(updatedUserDTO.getEmail(), updatedUserDTO.getContactNumber(), updatedUserDTO.getId())) {
                throw new Exception("User Exists with credentials");
            }
            if(!(user.getRole()== (Role.CUSTOMER))){
                throw new Exception("User Not found");
            }
            User newUpdatedUser = updatedUserDTO.convertToEntity(updatedUserDTO);

            String roleString = updatedUserDTO.getRole();

                newUpdatedUser.setRole(Role.CUSTOMER);

            User userAccount = user;

            newUpdatedUser.setUserAccount(null);
            newUpdatedUser.setPassword(user.getPassword());
            newUpdatedUser.setCreationDate(user.getCreationDate());
            newUpdatedUser.setModificationDate(ZonedDateTime.now());
            newUpdatedUser = usersRepository.save(newUpdatedUser);


            return updatedUserDTO.convertToDTO(newUpdatedUser);

        } else {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
    }

    public void deleteCustomer(Long id) throws Exception {
        Optional<User> userOptional = usersRepository.findById(Math.toIntExact(id));

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if the user has the role "CUSTOMER"
            if (user.getRole() == Role.CUSTOMER) {
                // Check if the user has an associated account
                if (!usersRepository.existsByUserAccountIsNotNullAndId(user.getId())) {
                    List<Token> tokens = tokenRepository.findByUserId(id);
                    tokenRepository.deleteAll(tokens);
                    usersRepository.deleteById(Math.toIntExact(id));
                } else {
                    // Throw an exception if the user has an associated account
                    throw new Exception("User with id " + id + " has an associated account");
                }
            } else {
                // Throw an exception if the user doesn't have the expected role
                throw new IllegalArgumentException("User with id " + id + " does not have the role 'CUSTOMER'");
            }
        } else {
            throw new UserNotFoundException("Customer with id " + id + " not found.");
        }
    }
}
