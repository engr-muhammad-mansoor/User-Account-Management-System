package com.example.ams.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
  
  boolean existsByContactNumber(String contactNumber);
  

  boolean existsByEmailOrContactNumberAndIdNot(String userName, String contactNumber, Long id);

  boolean existsByUserAccountIsNotNullAndId(Long id);


  boolean existsByEmail(String userName);
}
