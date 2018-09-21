package cybereast;

import cybereast.service.StorageProperties;
import cybereast.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CyberEastApplication {

  public static void main(String[] args) {
    SpringApplication.run(CyberEastApplication.class, args);
  }

  @Bean
  CommandLineRunner init(StorageService storageService) {
    return (args) -> {
      //storageService.deleteAll();
      storageService.init();
    };
  }

}
