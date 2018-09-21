package cybereast.controller;

import cybereast.model.UserModel;
import cybereast.payload.UserLoginPayload;
import cybereast.payload.UserRegisterPayload;
import cybereast.service.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationController {

  @Autowired
  private UserAuthService userAuthService;

  @PostMapping(value = "/login", produces = "application/json")
  public RolePayload login(@RequestBody UserLoginPayload payload, HttpServletResponse response) {
      Pair<String, String> roleTokenPair = userAuthService.login(payload);
      response.addCookie(new Cookie("hr_helper_auth_token", roleTokenPair.getFirst()));
      return new RolePayload(roleTokenPair.getSecond());
  }

  @PostMapping(value = "/register", produces = "application/json")
  public RolePayload register(@RequestBody UserRegisterPayload payload, HttpServletResponse response) {
      Pair<String, String> roleTokenPair = userAuthService.register(payload);
      response.addCookie(new Cookie("hr_helper_auth_token", roleTokenPair.getFirst()));
      return new RolePayload(roleTokenPair.getSecond());
  }

  @AllArgsConstructor
  public static class RolePayload {
      public String role;
  }

}
