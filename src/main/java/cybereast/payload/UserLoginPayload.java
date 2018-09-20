package cybereast.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginPayload {

    private String login;
    private String password;

}
