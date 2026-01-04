package com.example.ams.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {

  private Long id;
  private String firstName;
  private String lastName;
  private String userName;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  private String email;
  private char gender;
  private String contactNumber;
  private String address;
  private String role; // Assuming you want to include the role ID in the DTO
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
    if(users.getRole() == null)
    {
      this.role = "Not Availabale";
    }
    else this.role = users.getRole().name();
    this.modificationDate = users.getModificationDate();
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
