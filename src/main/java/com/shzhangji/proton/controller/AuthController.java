package com.shzhangji.proton.controller;

import com.shzhangji.proton.AppException;
import com.shzhangji.proton.form.LoginForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
  @PostMapping("/login")
  public Object login(@Valid @RequestBody LoginForm form, BindingResult bindingResult,
                      HttpServletRequest request) {
    if (bindingResult.hasErrors()) {
      throw new AppException("Invalid username or password");
    }

    return Map.of("form", form);
  }
}
