
<meta name="layout" content="ui"/>

<h1>Muon System Demo Dashboard [Whitespace edition]</h1>

<p>
There are currently <b>${services.size()}</b> services running in the system.
</p>

<g:if test="${eventStore}">
    <p>An Event Store is available, and calls itself ${eventStore.identifier}</p>
    <p>You can emit events <g:link action="event">here</g:link></p>
</g:if>

<g:each in="${services}">
    <p><b>${it}</b><g:link action="introspect" id="${it}">introspect</g:link> </p>
</g:each>
