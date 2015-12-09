# Reactive Microservices using Elementary Particles

This is an introduction and demo of a Muon based system, with special mention of the Photon event store.

Muon is a toolkit for building Microservices. It is designed to be polyglot, event based and enable you to build AP applications in CAP Theorum terminology.

## Structure

### ReactiveUI

A very simple grails based UI that functions as a dashboard.

Start it up using 

```
./grailsw run-app
```

Visit http://localhost:8080/dashboard to see a view of the system

### Entity

The 'classic' or entity oriented service design.

Uses RPC based communication mainly, with some streaming of event notifications.

Uses spring boot, start services using the spring boot cli

```
spring run XXX.groovy
```

Try SDKMan to download spring boot http://sdkman.io/

### Eventsource

The same simple system as above, but realsied using event sourcing.

Also built using spring boot, run the same as above
