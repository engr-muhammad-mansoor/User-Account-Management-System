package com.example.ams.user;

import com.example.ams.data.accounts_data.Account;
import com.example.ams.token.Token;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "GENDER")
  private char gender;

  @Column(name = "EMAIL_ADDRESS")
  private String email;

  @Column(name = "CONTACT_NUMBER")
  private String contactNumber;

  @Column(name = "ADDRESS")
  private String address;

  @Column(name = "CREATION_DATE")
  private ZonedDateTime creationDate;

  @Column(name= "MODIFICATION_DATE")
  private ZonedDateTime modificationDate;



  @OneToOne
  @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID")
  private Account userAccount;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
