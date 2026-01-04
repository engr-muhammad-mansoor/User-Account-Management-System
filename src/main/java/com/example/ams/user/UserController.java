package com.example.ams.user;

import com.example.ams.data.accounts_data.AccountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/system-users/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {
        try {
            userService.changePassword(request, connectedUser);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/admin/get-all-users/")
    public ResponseEntity<List<UsersDTO>> getAllUsers() {
        List<UsersDTO> usersDTOList = userService.getAllUsers();
        return ResponseEntity.ok(usersDTOList);
    }

    @GetMapping("/admin/get-all-customers/")
    public ResponseEntity<?> getAllCustomers() {
        try {
            List<UsersDTO> customersDTOList = userService.getAllCustomers();
            return ResponseEntity.status(HttpStatus.OK).body(customersDTOList);
        }
        catch(Exception e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }}

    @GetMapping("/system-users/find-user/{id}")
    public ResponseEntity<UsersDTO> getUserById(@PathVariable Long id) {
        try {
            Optional<UsersDTO> user = userService.getUserById(id);
            return user.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsersDTO()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsersDTO());
        }
    }

    @GetMapping("/system-users/find-customer/{id}")
    public ResponseEntity<UsersDTO> getCustomerById(@PathVariable Long id) {
        try {
            Optional<UsersDTO> user = userService.getCustomerById(id);
            return user.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsersDTO()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsersDTO());
        }
    }

    @PostMapping("/system-users/add-customer/")
    public ResponseEntity<UsersDTO> createCustomer(@RequestBody UsersDTO userDTO) throws Exception {
        try {
        UsersDTO createdUserDTO = userService.createCustomer(userDTO);
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
    }
        catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }

    @PostMapping("/system-users/add-account-customer/{customerId}/{accountId}")
    public ResponseEntity<String> AssociateAccountToCustomer(@PathVariable(name = "customerId") Long customerId, @PathVariable(name = "accountId") Long accountId) {
        try {
            userService.associateAccount(customerId, accountId);
            return new ResponseEntity<>("Account associated successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error associating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/system-users/add-account-user/{userId}/{accountId}")
    public ResponseEntity<String> AssociateAccountToUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "accountId") Long accountId) {
        try {
            userService.associateAccount(userId, accountId);
            return new ResponseEntity<>("Account associated successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error associating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/admin/update-user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UsersDTO updatedUserDTO) {
        try {
            UsersDTO newUpdatedUserDTO = userService.updateUser(id, updatedUserDTO);
            return new ResponseEntity<>(newUpdatedUserDTO, HttpStatus.OK);
        } catch (UserNotFoundException e1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/system-users/update-customer/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody UsersDTO updatedUserDTO) throws Exception {
        try {
            UsersDTO newUpdatedUserDTO = userService.updateCustomer(id, updatedUserDTO);
            return new ResponseEntity<>(newUpdatedUserDTO, HttpStatus.OK);
        } catch (UserNotFoundException e1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating customer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/admin/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }

    @DeleteMapping("/admin/delete-customer/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        try {
            userService.deleteCustomer(id);
            return ResponseEntity.ok("Customer deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting customer: " + e.getMessage());
        }
    }
}
