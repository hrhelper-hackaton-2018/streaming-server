package cybereast;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CyberEastController {

  @RequestMapping("/hello")
  public CyberModel hello() {
    return new CyberModel();
  }

}
