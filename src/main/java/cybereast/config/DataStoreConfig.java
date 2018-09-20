package cybereast.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@Configuration
@EnableMongoRepositories("cybereast.repository")
public class DataStoreConfig {

//  @Bean
//  public MongoClient mongoClient() {
//    MongoClient mongo = new MongoClient("ds161039.mlab.com", 61039);
//    mongo.getCredentialsList().add(MongoCredential.createCredential("name", "db", "pwd".toCharArray()));
//    return mongo;
//  }

}
