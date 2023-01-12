package com.shzhangji.proton.controller;

import com.shzhangji.proton.AppException;
import com.shzhangji.proton.entity.User;
import com.shzhangji.proton.form.LoginForm;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {
  @PostMapping("/login")
  public CurrentUser login(@Valid @RequestBody LoginForm form, BindingResult bindingResult,
                           HttpServletRequest request) {
    if (bindingResult.hasErrors()) {
      throw new AppException("Invalid username or password");
    }

    try {
      request.login(form.getUsername(), form.getPassword());
    } catch (ServletException e) {
      throw new AppException("Invalid username or password");
    }

    var auth = (Authentication) request.getUserPrincipal();
    var user = (User) auth.getPrincipal();
    log.info("User {} logged in.", user.getUsername());

    return new CurrentUser(user.getId(), user.getNickname());
  }

  @PostMapping("/logout")
  public LogoutResponse logout(HttpServletRequest request) throws ServletException {
    request.logout();
    return new LogoutResponse();
  }

  @GetMapping("/current-user")
  public CurrentUser getCurrentUser(@AuthenticationPrincipal User user) {
    return new CurrentUser(user.getId(), user.getNickname());
  }

  public record CurrentUser(Integer id, String nickname) {}
  public record LogoutResponse() {}
}
