package reactiveui

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import io.muoncore.Discovery
import io.muoncore.Muon
import io.muoncore.SingleTransportMuon
import io.muoncore.codec.Codecs
import io.muoncore.codec.json.JsonOnlyCodecs
import io.muoncore.config.AutoConfiguration
import io.muoncore.extension.amqp.AMQPMuonTransport
import io.muoncore.extension.amqp.AmqpChannelFactory
import io.muoncore.extension.amqp.AmqpConnection
import io.muoncore.extension.amqp.DefaultAmqpChannelFactory
import io.muoncore.extension.amqp.DefaultServiceQueue
import io.muoncore.extension.amqp.QueueListenerFactory
import io.muoncore.extension.amqp.ServiceQueue
import io.muoncore.extension.amqp.discovery.AmqpDiscovery
import io.muoncore.extension.amqp.discovery.ServiceCache
import io.muoncore.extension.amqp.rabbitmq09.RabbitMq09ClientAmqpConnection
import io.muoncore.extension.amqp.rabbitmq09.RabbitMq09QueueListenerFactory
import io.muoncore.transport.MuonTransport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException

@Configuration
class Application extends GrailsAutoConfiguration {



    @Bean Muon muon() {
        String serviceName = "ReactiveUI"

        AmqpConnection connection = new RabbitMq09ClientAmqpConnection("amqp://muon:microservices@localhost");
        QueueListenerFactory queueFactory = new RabbitMq09QueueListenerFactory(connection.getChannel());
        ServiceQueue serviceQueue = new DefaultServiceQueue(serviceName, connection);
        AmqpChannelFactory channelFactory = new DefaultAmqpChannelFactory(serviceName, queueFactory, connection);

        MuonTransport svc1 = new AMQPMuonTransport(
                "amqp://muon:microservices@localhost", serviceQueue, channelFactory);

        AutoConfiguration config = new AutoConfiguration();
        config.setServiceName(serviceName);
        config.setAesEncryptionKey("1234567890abcdef");

        return new SingleTransportMuon(config, createDiscovery(), svc1)
    }

    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    private static Discovery createDiscovery() throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException, IOException {

        AmqpConnection connection = new RabbitMq09ClientAmqpConnection("amqp://muon:microservices@localhost");
        QueueListenerFactory queueFactory = new RabbitMq09QueueListenerFactory(connection.getChannel());
        Codecs codecs = new JsonOnlyCodecs();

        AmqpDiscovery discovery = new AmqpDiscovery(queueFactory, connection, new ServiceCache(), codecs);
        discovery.start();
        return discovery;
    }
}