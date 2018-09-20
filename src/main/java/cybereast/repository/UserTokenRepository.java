package cybereast.repository;

import cybereast.model.UserTokenModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface UserTokenRepository extends MongoRepository<UserTokenModel, String> {

    UserTokenModel findByToken(String token);

}
