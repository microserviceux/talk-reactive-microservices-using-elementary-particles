package reactiveui

import groovy.json.JsonBuilder
import io.muoncore.Muon
import io.muoncore.protocol.event.Event
import org.springframework.beans.factory.annotation.Autowired

class DashboardController {

    @Autowired Muon muon

    def index() {
        def services = muon.discovery.knownServices*.identifier

        [services:services, eventStore:muon.discovery.knownServices.find{ it.tags.contains("eventstore")}]
    }

    def introspect() {

        def userService = muon.introspect(params.id).get()
        [       discovery: muon.discovery.findService({it.identifier == params.id}).get(),
                introspect:userService]
    }

    def data() {
        def service = params.id
        def endpoint = params.endpoint
        def uri = "request://${service}${endpoint}"

        [endpoint:endpoint, serviceName:service, url:uri]
    }

    def sendData() {

        def service = params.id
        def endpoint = params.endpoint
        def uri = "request://${service}${endpoint}"

        def content = new JsonBuilder(params.content)


        println "Requesting from ${uri} ${content}"

        def then = System.currentTimeMillis()
        def data
        if (content) {
            data = muon.request(uri, content, Object).get()
        } else {
            data = muon.request(uri, Object).get()
        }
        def latency = System.currentTimeMillis() - then

        def json = new JsonBuilder(data).toPrettyString()

        [response:json, endpoint:endpoint, content:content, serviceName:service, url:uri, latency:latency]
    }

    def event() {
        def mod = [:]
        if (params.content) {
            def content = new JsonBuilder(params.content)
            def eventType = params.eventType

            def then = System.currentTimeMillis()

            def event = new Event(eventType, UUID.randomUUID().toString(), null, muon.configuration.serviceName, content)

            muon.event(event)

            mod.message = "Event has been emitted ${eventType}"
        }
        mod
    }
}














