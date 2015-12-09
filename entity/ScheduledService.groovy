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

/**

 Example microservice that will emit an event periodically via Muon.
 
 Demonstrates pull based event distribution as a networked implementation of an Observer pattern

**/

@Grapes([
@Grab('io.muoncore:muon-transport-amqp:6.2.9'),
@Grab('io.muoncore:muon-discovery-amqp:6.2.9'),
@Grab('io.muoncore:muon-spring:6.2.9')])
@SpringBootApplication
@EnableMuon(serviceName = "UserService")
@EnableScheduling
public class UserService {

    @Autowired
    private Muon muon;
    private Broadcaster<Map> userCreated = Broadcaster.create(Environment.initializeIfEmpty());

    @PostConstruct
    public void setupStreams() {
        muon.publishSource("userCreated", HOT, userCreated);
    }

    @Scheduled(fixedRate = 5000l)
    public void emitData() {
        System.out.println("Sending data");
        userCreated.accept([
           userName:"Awesome"
        ]);
    }

    public static void main(String[] args) {
        SpringApplication.run(UserService.class);
    }
}
