package muon

@GrabResolver(name = 'jcenter', root = 'http://jcenter.bintray.com/') 

import io.muoncore.SingleTransportMuon;
import io.muoncore.protocol.event.Event;
import io.muoncore.protocol.event.server.EventServerProtocolStack;
import io.muoncore.protocol.reactivestream.server.PublisherLookup;
import io.muoncore.spring.annotations.EnableMuon;
import io.muoncore.spring.annotations.MuonController;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.reactivestreams.*

@Grapes([
@Grab('io.muoncore:muon-transport-amqp:6.2.10'),
@Grab('io.muoncore:muon-discovery-amqp:6.2.10'),
@Grab('io.muoncore:muon-spring:6.2.10')])

/**
 * Built for a talk, this is a very basic implementation of a muon event store that is simplistic to the point of being fairly broken.
 *
 * Thrashes threads somewhat.
 *
 * Useful for simple local testing, use photon for anything else, which has correct threading semantics.
 */
@SpringBootApplication
@MuonController
@EnableMuon(serviceName = "chronos", tags = ["eventstore"])
public class EventStoreInMem {

    @Autowired public SingleTransportMuon muon;
    private static  List<Event> history = new ArrayList<>();
    
    private static List subs = []
    private static final Executor exec = Executors.newFixedThreadPool(10);

    @PostConstruct
    public void setupEventStore() {
        def theExec = exec
        def theSubs = subs
        def theHistory = history
    
        theExec.execute({
            try {
                while(true) {
                    synchronized (theExec) {
                        theExec.wait(500);
                        //drain all the queues
                        theSubs.stream().forEach({ sq->
                            synchronized (sq.queue) {
                                while(sq.queue.size() > 0) {
                                    sq.sub.onNext(sq.queue.poll());
                                }
                            }
                        });
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        muon.getProtocolStacks().registerServerProtocol(new EventServerProtocolStack({ event ->
            println "Accepting event ${event}"
            theSubs.stream().forEach({ q->
                synchronized (q.queue) {q.queue.add(event);}
            });
            synchronized (theHistory) {
                theHistory.add(event);
            }
            synchronized (exec) {
                exec.notifyAll();
            }
        }, muon.getCodecs()));

        muon.publishGeneratedSource("general", PublisherLookup.PublisherType.HOT_COLD, { subscriptionRequest ->
            System.out.println("Subscription to general stream!");
            return new Publisher() {  
            void subscribe(Subscriber subscriber) {
                Queue<Event> queue = new LinkedList<>();
                System.out.println("Replaying " + theHistory.size() + " events into the stream");
                synchronized (theHistory) {
                    theHistory.stream().forEach({ queue.add(it) });
                }
                theSubs.add([queue:queue, sub:subscriber]);
            }}
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(EventStoreInMem.class);
    }
}
