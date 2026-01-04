package com.example.ams.auth;

import com.example.ams.user.Role;
import com.example.ams.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {

  private Long id;
  private String firstName;
  private String lastName;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  private String email;
  private char gender;
  private String contactNumber;
  private String address;
  private String role; // Assuming you want to include the role ID in the DTO
  boolean doesEmployeeHasAccount;
  private Long accountId;
  private ZonedDateTime creationDate;
  private ZonedDateTime modificationDate;
  private Long createdBy;

  public UsersDTO convertToDTO(User users){
    this.id = users.getId();
    this.firstName = users.getFirstName();
    this.lastName = users.getLastName();
    this.gender = users.getGender();
    this.contactNumber = users.getContactNumber();
    this.email = users.getEmail();
    this.address = users.getAddress();
    if(users.getUserAccount() == null)
    {
      this.accountId = 0L;
    }
    else this.accountId = users.getUserAccount().getId();
    this.creationDate = users.getCreationDate();
    this.role = users.getRole().name();
    this.modificationDate = users.getModificationDate();
    this.doesEmployeeHasAccount = false;
    return this;
  }

  public User convertToEntity(UsersDTO usersDTO) {
    User users = new User();
    users.setFirstName(usersDTO.getFirstName());
    users.setLastName(usersDTO.getLastName());
    users.setGender(usersDTO.getGender());
    users.setEmail(usersDTO.getEmail());
    users.setAddress(usersDTO.getAddress());
    users.setCreationDate(usersDTO.getCreationDate());
    users.setModificationDate(usersDTO.getModificationDate());
    users.setContactNumber(usersDTO.getContactNumber());

    return users;
  }
}
