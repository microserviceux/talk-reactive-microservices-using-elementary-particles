package muon

@GrabResolver(name = 'jcenter', root = 'http://jcenter.bintray.com/') 
import io.muoncore.spring.annotations.EventSourceListener
import io.muoncore.spring.repository.MuonEventStoreRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import io.muoncore.spring.annotations.*
import org.springframework.context.annotation.PropertySource
import io.muoncore.spring.annotations.MuonRequestListener;

@Grapes([
@Grab('io.muoncore:muon-transport-amqp:6.2.9'),
@Grab('io.muoncore:muon-discovery-amqp:6.2.9'),
@Grab('io.muoncore:muon-spring:6.2.9')])
@SpringBootApplication
@MuonController
@EnableMuon(serviceName="UserEmailAdapter")
class Application {

  int counter=0

  @EventSourceListener
  public void listenToData(Map data) {
    System.out.println("Sending EMAIL " + data);
    counter++
  }
  
    @MuonRequestListener(path = "/counter")
    public int add(Map data) {
        return counter 
    }

  static void main(def args) {  SpringApplication.run(Application)  }
}
