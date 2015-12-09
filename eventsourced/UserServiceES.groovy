package muon

@GrabResolver(name = 'jcenter', root = 'http://jcenter.bintray.com/') 
import io.muoncore.spring.annotations.EventSourceListener
import io.muoncore.spring.repository.MuonEventStoreRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import io.muoncore.spring.annotations.*
import org.springframework.context.annotation.PropertySource
import reactor.Environment
import reactor.rx.broadcast.Broadcaster
import static io.muoncore.protocol.reactivestream.server.PublisherLookup.PublisherType.HOT
import io.muoncore.Muon
import io.muoncore.spring.annotations.MuonController;
import io.muoncore.spring.annotations.MuonRequestListener;

@Grapes([
@Grab('io.muoncore:muon-transport-amqp:6.2.10'),
@Grab('io.muoncore:muon-discovery-amqp:6.2.10'),
@Grab('io.muoncore:muon-spring:6.2.10')])
@SpringBootApplication
@EnableMuon(serviceName = "UserService")
@MuonController
public class UserService {

    @Autowired MuonEventStoreRepository eventStore

    List people = []
    
    @MuonRequestListener(path = "/addPerson")
    public Map add(Map data) {
        eventStore.event("general", data)
        return [message: "Added person ${data.content}".toString()] 
    }
    
    @MuonRequestListener(path = "/getPeople")
    public List getPeople() {
        return people
    }
    
    
    
    @EventSourceListener
    public void listenToData(Map data) {
      people << data
    }
    
    public static void main(String[] args) {
        SpringApplication.run(UserService.class);
    }
}
