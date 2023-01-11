package com.shzhangji.proton.repository;

import com.shzhangji.proton.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
  Optional<User> findByUsername(String username);
}
