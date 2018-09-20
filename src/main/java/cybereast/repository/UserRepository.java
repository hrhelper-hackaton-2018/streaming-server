package cybereast.repository;

import cybereast.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {

    public UserModel findByEmailOrLogin(String email, String login);

}
