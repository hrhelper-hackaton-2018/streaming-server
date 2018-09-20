package cybereast.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
//@NoArgsConstructor
@Document(collection = "users_tokens")
public class UserTokenModel {

    @Id
    private String id;

    @Indexed(unique = true)
    private String token;

    @DBRef
    private UserModel user;

}

