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
import io.muoncore.spring.annotations.parameterhandlers.Parameter;


/**

 Use the Muon request/ response protocol to implement an entity style microservice.
 
 This service owns the data

**/


@Grapes([
@Grab('io.muoncore:muon-transport-amqp:6.2.9'),
@Grab('io.muoncore:muon-discovery-amqp:6.2.9'),
@Grab('io.muoncore:muon-spring:6.2.9')])
@SpringBootApplication
@EnableMuon(serviceName = "UserService")
@EnableScheduling

@MuonController   // <<< note this. This enables all thespring muon helpers.
public class UserService {

    @Autowired
    private Muon muon;
    
    private Broadcaster<Map> userCreated = Broadcaster.create(Environment.initializeIfEmpty());
    public List people = []

    @PostConstruct
    public void setupStreams() {
        muon.publishSource("userCreated", HOT, userCreated);
    }

    @MuonRequestListener(path = "/addPerson")
    public Map add(Map data) {
        people << data.content
        userCreated.accept(data)
        return [message: "Added person ${data.content}".toString()] 
    }
    
    @MuonRequestListener(path = "/getPeople")
    public List getPeople() {
        println "Returning some people... ${people}"
        return people
    }

    public static void main(String[] args) {
        SpringApplication.run(UserService.class);
    }
}
