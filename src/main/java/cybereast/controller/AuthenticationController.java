package cybereast.controller;

import cybereast.model.UserModel;
import cybereast.payload.UserLoginPayload;
import cybereast.payload.UserRegisterPayload;
import cybereast.service.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationController {

  @Autowired
  private UserAuthService userAuthService;

  @PostMapping(value = "/login", produces = "application/json")
  public RolePayload login(@RequestBody UserLoginPayload payload, HttpServletResponse response) {
      Pair<String, UserModel> roleTokenPair = userAuthService.login(payload);
      response.addCookie(new Cookie("hr_helper_auth_token", roleTokenPair.getFirst()));
      UserModel user = roleTokenPair.getSecond();
      return new RolePayload(user.getRole().name(), user.getEmail(), user.getUserName());
  }

  @PostMapping(value = "/register", produces = "application/json")
  public RolePayload register(@RequestBody UserRegisterPayload payload, HttpServletResponse response) {
      Pair<String, UserModel> roleTokenPair = userAuthService.register(payload);
      response.addCookie(new Cookie("hr_helper_auth_token", roleTokenPair.getFirst()));
      UserModel user = roleTokenPair.getSecond();
      return new RolePayload(user.getRole().name(), user.getEmail(), user.getUserName());
  }

  @GetMapping(value = "/me")
  public UserModel me(@CookieValue("hr_helper_auth_token") String token) {
      return userAuthService.getUser(token);
  }

  @GetMapping(value = "/user")
  public UserModel getUser(String token) {
      return userAuthService.getUser(token);
  }

  @AllArgsConstructor
  public static class RolePayload {
      public String role;
      public String email;
      public String userName;
  }

}
