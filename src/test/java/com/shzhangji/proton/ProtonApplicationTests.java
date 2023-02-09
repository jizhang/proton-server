package com.shzhangji.proton;

import com.shzhangji.proton.service.CustomUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class ProtonApplicationTests {
  @Autowired
  private CustomUserDetailsService userService;

  @Test
  public void testLoadUser() {
    var user = userService.loadUserByUsername("admin");
    var encodedPassword = user.getPassword().substring(8);
    var encoder = new BCryptPasswordEncoder();
    Assertions.assertTrue(encoder.matches("888888", encodedPassword));
  }
}
