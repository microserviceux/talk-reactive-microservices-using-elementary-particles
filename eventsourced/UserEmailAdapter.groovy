package muon

@GrabResolver(name = 'jcenter', root = 'http://jcenter.bintray.com/') 
import io.muoncore.spring.annotations.EventSourceListener
import io.muoncore.spring.repository.MuonEventStoreRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import io.muoncore.spring.annotations.*
import org.springframework.context.annotation.PropertySource

@Grapes([
@Grab('io.muoncore:muon-transport-amqp:6.2.9'),
@Grab('io.muoncore:muon-discovery-amqp:6.2.9'),
@Grab('io.muoncore:muon-spring:6.2.9')])
@SpringBootApplication
@MuonController
@EnableMuon(serviceName="UserEmailAdapter")
class Application {

  @MuonStreamListener(url = "stream://UserService/userCreated")
  public void listenToData(Map data) {
    System.out.println("Sending EMAIL " + data);
  }
  
  static void main(def args) {  SpringApplication.run(Application)  }
}
