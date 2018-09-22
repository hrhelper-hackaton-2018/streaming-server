package cybereast.payload;

import cybereast.model.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterPayload {

    private String userName;
    private String email;
    private String password;
    private String repeatedPassword;
    private UserRole role;
    private String firstName;
    private String lastName;
    private Integer age;

}
