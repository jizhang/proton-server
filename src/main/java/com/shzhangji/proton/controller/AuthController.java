package com.shzhangji.proton.controller;

import com.shzhangji.proton.AppException;
import com.shzhangji.proton.entity.User;
import com.shzhangji.proton.form.LoginForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "auth", description = "User authentication")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@DependsOn("securityFilterChain")
public class AuthController {
  private final RememberMeServices rememberMeServices;

  @Operation(summary = "Login user")
  @PostMapping("/login")
  public CurrentUser login(@Valid @RequestBody LoginForm form, BindingResult bindingResult,
                           HttpServletRequest request, HttpServletResponse response) {

    if (request.getUserPrincipal() != null) {
      throw new AppException("Please logout first.");
    }

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

    rememberMeServices.loginSuccess(request, response, auth);
    return new CurrentUser(user.getId(), user.getNickname());
  }

  @Operation(summary = "Logout user")
  @PostMapping("/logout")
  public LogoutResponse logout(HttpServletRequest request) throws ServletException {
    request.logout();
    return new LogoutResponse();
  }

  @Operation(summary = "Get current logged-in user")
  @GetMapping("/current-user")
  public CurrentUser getCurrentUser(@AuthenticationPrincipal User user) {
    return new CurrentUser(user.getId(), user.getNickname());
  }

  @Operation(summary = "Get CSRF token")
  @GetMapping("/csrf")
  public CsrfResponse csrf(HttpServletRequest request, HttpServletResponse response) {
    var csrf = (CsrfToken) request.getAttribute("_csrf");
    var cookie = new Cookie("CSRF-TOKEN", csrf.getToken());
    cookie.setPath("/");
    response.addCookie(cookie);
    return new CsrfResponse(csrf.getToken());
  }

  public record CurrentUser(Integer id, String nickname) {}

  public record LogoutResponse() {}

  public record CsrfResponse(String token) {}
}
