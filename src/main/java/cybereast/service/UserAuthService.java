package cybereast.service;

import cybereast.model.UserModel;
import cybereast.model.UserRole;
import cybereast.model.UserTokenModel;
import cybereast.payload.UserLoginPayload;
import cybereast.payload.UserRegisterPayload;
import cybereast.repository.UserRepository;
import cybereast.repository.UserTokenRepository;
import javafx.util.Pair;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    public Pair<String, String> register(UserRegisterPayload payload) {

        if (!payload.getPassword().equals(payload.getRepeatedPassword())) {
            throw new SecurityException("Passwords are not equal!");
        }

        UserModel user = UserModel.builder()
                .login(payload.getLogin())
                .email(payload.getEmail())
                .password(payload.getPassword())
                .role(payload.getRole())
                .build();

        userRepository.save(user);

        UserTokenModel token = UserTokenModel.builder().token(RandomStringUtils.random(128)).user(user).build();
        userTokenRepository.save(token);

        return new Pair<>(token.getToken(), user.getRole().name());

    }

    public Pair<String, String> login(UserLoginPayload payload) {

        UserModel userModel = userRepository.findByEmailOrLogin(payload.getLogin(), payload.getLogin());

        if (!userModel.getPassword().equals(payload.getPassword())) {
            throw new SecurityException("Wrong password!");
        }

        UserTokenModel token = UserTokenModel.builder().token(RandomStringUtils.random(128)).user(userModel).build();
        userTokenRepository.save(token);

        return new Pair<>(token.getToken(), userModel.getRole().name());

    }

    public boolean checkRole(UserRole role, String tokenCookie) {

        UserTokenModel token = userTokenRepository.findByToken(tokenCookie);

        return token.getUser().getRole().equals(role);

    }

}
